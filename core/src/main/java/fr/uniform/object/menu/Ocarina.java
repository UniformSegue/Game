package fr.uniform.object.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Gère l'affichage visuel de l'ocarina dans l'interface du jeu (menus ou décor).
 * Encapsule un Sprite avec sa position et ses dimensions pour faciliter son rendu.
 */
public class Ocarina {

    // --- Propriétés spatiales ---
    public float x;
    public float y;
    public float width;
    public float height;

    // --- Rendu visuel ---
    public Sprite sprite;

    /**
     * Initialise l'élément visuel de l'ocarina.
     *
     * @param x       Position X du coin inférieur gauche.
     * @param y       Position Y du coin inférieur gauche.
     * @param width   Largeur de l'image de l'ocarina.
     * @param height  Hauteur de l'image de l'ocarina.
     * @param texture La texture de l'ocarina à afficher.
     */
    public Ocarina(float x, float y, float width, float height, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
        this.sprite.setSize(width, height);
    }
}
