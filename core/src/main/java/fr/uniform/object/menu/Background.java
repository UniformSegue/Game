package fr.uniform.object.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Gère l'affichage de l'image de fond (background) pour les différents écrans du jeu.
 * Encapsule un Sprite avec sa position et ses dimensions pour faciliter son rendu.
 */
public class Background {

    // --- Propriétés spatiales ---
    public float x;
    public float y;
    public float width;
    public float height;

    // --- Rendu visuel ---
    public Sprite sprite;

    /**
     * Initialise un nouvel arrière-plan.
     *
     * @param x       Position X du coin inférieur gauche.
     * @param y       Position Y du coin inférieur gauche.
     * @param width   Largeur de l'arrière-plan (généralement la largeur de l'écran).
     * @param height  Hauteur de l'arrière-plan (généralement la hauteur de l'écran).
     * @param texture La texture de l'image de fond à afficher.
     */
    public Background(float x, float y, float width, float height, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
        this.sprite.setSize(width, height);
    }
}
