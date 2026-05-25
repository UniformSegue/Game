/*
 * Copyright 2020 damios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * https://www.apache.org/licenses/LICENSE-2.0
 */

package fr.uniform.lwjgl3;

import com.badlogic.gdx.Version;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader;
import org.lwjgl.system.macosx.LibC;
import org.lwjgl.system.macosx.ObjCRuntime;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

import static org.lwjgl.system.JNI.invokePPP;
import static org.lwjgl.system.JNI.invokePPZ;
import static org.lwjgl.system.macosx.ObjCRuntime.objc_getClass;
import static org.lwjgl.system.macosx.ObjCRuntime.sel_getUid;

/**
 * Utilitaire de démarrage garantissant la compatibilité multi-plateforme.
 * - Sur macOS : S'assure que la JVM démarre avec l'argument `-XstartOnFirstThread` requis par LWJGL 3.
 * - Sur Windows : Contourne les plantages liés aux noms d'utilisateurs contenant des caractères non-latins.
 */
public class StartupHelper {

    private static final String JVM_RESTARTED_ARG = "jvmIsRestarted";

    private StartupHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * Vérifie si l'application nécessite un redémarrage de la JVM avec les bons paramètres.
     * Redirige automatiquement la sortie de la nouvelle JVM vers l'ancienne.
     *
     * @return true si une nouvelle JVM a été lancée (l'exécution actuelle doit alors être stoppée), false sinon.
     */
    public static boolean startNewJvmIfRequired() {
        return startNewJvmIfRequired(true);
    }

    /**
     * Logique principale de vérification et de redémarrage de la JVM.
     *
     * @param redirectOutput true pour relayer les logs de la nouvelle JVM vers la console actuelle.
     * @return true si une nouvelle JVM a été lancée, false sinon.
     */
    public static boolean startNewJvmIfRequired(boolean redirectOutput) {
        String osName = System.getProperty("os.name").toLowerCase(java.util.Locale.ROOT);

        if (!osName.contains("mac")) {
            if (osName.contains("windows")) {
                // Contournement Windows : LWJGL3 extrait ses DLL dans le dossier temporaire de l'utilisateur.
                // Si le nom d'utilisateur contient des caractères non-ASCII, le jeu crash au lancement.
                // Solution : on redirige temporairement l'extraction vers "ProgramData" (ou "C:\Temp\").
                String programData = System.getenv("ProgramData");
                if (programData == null) programData = "C:\\Temp\\";

                String prevTmpDir = System.getProperty("java.io.tmpdir", programData);
                String prevUser = System.getProperty("user.name", "libGDX_User");

                System.setProperty("java.io.tmpdir", programData + "/libGDX-temp");
                System.setProperty("user.name", ("User_" + prevUser.hashCode() + "_GDX" + Version.VERSION).replace('.', '_'));

                Lwjgl3NativesLoader.load();

                // Restauration des propriétés système initiales
                System.setProperty("java.io.tmpdir", prevTmpDir);
                System.setProperty("user.name", prevUser);
            }
            return false; // Pas besoin de redémarrer sur Windows ou Linux
        }

        // --- Logique spécifique à macOS ---

        // Inutile de forcer le premier thread si on utilise une image native GraalVM
        if (!System.getProperty("org.graalvm.nativeimage.imagecode", "").isEmpty()) {
            return false;
        }

        // Vérification via Objective-C : sommes-nous déjà sur le Main Thread ?
        long objc_msgSend = ObjCRuntime.getLibrary().getFunctionAddress("objc_msgSend");
        long NSThread = objc_getClass("NSThread");
        long currentThread = invokePPP(NSThread, sel_getUid("currentThread"), objc_msgSend);
        boolean isMainThread = invokePPZ(currentThread, sel_getUid("isMainThread"), objc_msgSend);

        if (isMainThread) return false;

        long pid = LibC.getpid();

        // Vérifie si l'argument a déjà été passé via les variables d'environnement
        if ("1".equals(System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid))) {
            return false;
        }

        // Sécurité anti-boucle infinie : vérifie si on a déjà tenté de redémarrer
        if ("true".equals(System.getProperty(JVM_RESTARTED_ARG))) {
            System.err.println("Erreur : Impossible de vérifier si la JVM a démarré avec -XstartOnFirstThread.");
            return false;
        }

        // --- Redémarrage de la JVM avec -XstartOnFirstThread ---

        ArrayList<String> jvmArgs = new ArrayList<>();
        String separator = System.getProperty("file.separator", "/");
        String javaExecPath = System.getProperty("java.home") + separator + "bin" + separator + "java";

        if (!(new File(javaExecPath)).exists()) {
            System.err.println("Installation Java introuvable. Ajoutez -XstartOnFirstThread manuellement !");
            return false;
        }

        // Construction de la commande de lancement
        jvmArgs.add(javaExecPath);
        jvmArgs.add("-XstartOnFirstThread");
        jvmArgs.add("-D" + JVM_RESTARTED_ARG + "=true");
        jvmArgs.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
        jvmArgs.add("-cp");
        jvmArgs.add(System.getProperty("java.class.path"));

        // Récupération de la classe Main appelante
        String mainClass = System.getenv("JAVA_MAIN_CLASS_" + pid);
        if (mainClass == null) {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            if (trace.length > 0) {
                mainClass = trace[trace.length - 1].getClassName();
            } else {
                System.err.println("Erreur : La classe Main n'a pas pu être déterminée.");
                return false;
            }
        }
        jvmArgs.add(mainClass);

        // Exécution du nouveau processus
        try {
            if (!redirectOutput) {
                ProcessBuilder processBuilder = new ProcessBuilder(jvmArgs);
                processBuilder.start();
            } else {
                Process process = (new ProcessBuilder(jvmArgs)).redirectErrorStream(true).start();
                BufferedReader processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = processOutput.readLine()) != null) {
                    System.out.println(line);
                }
                process.waitFor();
            }
        } catch (Exception e) {
            System.err.println("Une erreur est survenue lors du redémarrage de la JVM.");
            e.printStackTrace();
        }

        return true;
    }
}
