package fr.uniform;

/**
 * Interface contenant les spécifications globales et les constantes du jeu.
 * Centralise les dimensions de référence (résolution virtuelle) utilisées pour l'affichage,
 * notamment pour configurer les caméras et les Viewports (ex: FitViewport)
 * afin de garantir un affichage homogène sur tous les écrans.
 */
public interface GAME_SPEC {

    /** Largeur de référence du jeu en pixels (Format Full HD). */
    int width = 1920;

    /** Hauteur de référence du jeu en pixels (Format Full HD). */
    int height = 1080;
}
