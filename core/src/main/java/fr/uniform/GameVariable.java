package fr.uniform;

/**
 * Interface contenant les variables et constantes de configuration par défaut du jeu.
 * Centralise les valeurs de base de la physique et du gameplay, utilisées si
 * aucune autre configuration spécifique (comme la difficulté d'un niveau JSON) n'est fournie.
 */
public interface GameVariable {

    /** * Vitesse de défilement standard par défaut.
     * Sert de valeur de repli pour la vitesse de chute des blocs (notes).
     */
    int VITESSE = 10;

    /** * Vitesse de défilement accélérée par défaut.
     * Utilisée lorsque le joueur active la mécanique de Turbo.
     */
    int VITESSE_TURBO = 15;
}
