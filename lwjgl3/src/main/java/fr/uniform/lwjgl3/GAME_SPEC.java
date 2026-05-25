package fr.uniform.lwjgl3;

/**
 * Interface contenant les spécifications globales et les dimensions de référence du jeu
 * (Spécifique au lanceur Desktop/LWJGL3).
 * Centralise la résolution virtuelle (1920x1080) utilisée pour garantir un affichage
 * correct et un ratio 16:9 sur tous les écrans d'ordinateur.
 */
public interface GAME_SPEC {

    /** Largeur de référence de la fenêtre du jeu en pixels. */
    int width = 1920;

    /** Hauteur de référence de la fenêtre du jeu en pixels. */
    int height = 1080;
}
