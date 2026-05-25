package fr.uniform.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.uniform.Main;

/** * Lanceur principal pour la version Bureau (Desktop) du jeu.
 * Utilise le backend LWJGL3 de LibGDX pour créer la fenêtre et gérer le contexte OpenGL.
 */
public class Lwjgl3Launcher {

    public static void main(String[] args) {
        // Gère la compatibilité système requise (notamment pour le thread principal sous macOS)
        if (StartupHelper.startNewJvmIfRequired()) return;

        createApplication();
    }

    /**
     * Instancie et lance l'application LibGDX avec la configuration définie.
     */
    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    /**
     * Définit les paramètres de la fenêtre de jeu, les limites de framerate et les options de rendu.
     * @return La configuration LWJGL3 prête à être utilisée.
     */
    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        // Titre affiché sur la bordure de la fenêtre
        configuration.setTitle("Ocarina Hero");

        // --- Paramètres d'affichage et de performance ---

        // Active la synchronisation verticale pour éviter les déchirures d'image (screen tearing)
        configuration.useVsync(true);

        // Limite le jeu à 60 FPS constants (essentiel pour la stabilité de la physique et des rythmes)
        configuration.setForegroundFPS(60);

        // Applique la résolution virtuelle globale définie dans GAME_SPEC
        configuration.setWindowedMode(GAME_SPEC.width, GAME_SPEC.height);

        // --- Ressources graphiques et système ---

        // Définition des icônes de la fenêtre (barre des tâches, etc.) selon différentes résolutions
        configuration.setWindowIcon("logo128.png", "logo64.png", "logo32.png", "logo16.png");

        // Améliore la compatibilité OpenGL (notamment pour les drivers Windows capricieux
        // ou l'émulation sur Apple Silicon) en utilisant ANGLE.
        configuration.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.ANGLE_GLES20, 0, 0);

        return configuration;
    }
}
