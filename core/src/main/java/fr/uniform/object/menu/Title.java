package fr.uniform.object.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Gère l'affichage du titre du jeu (logo ou texte graphique) dans les menus.
 * Encapsule un Sprite avec sa position et ses dimensions pour faciliter son rendu.
 */
public class Title {

    // --- Propriétés spatiales ---
    public float x;
    public float y;
    public float width;
    public float height;

    // --- Rendu visuel ---
    public Sprite sprite;

    /**
     * Initialise l'élément visuel du titre.
     *
     * @param x       Position X du coin inférieur gauche.
     * @param y       Position Y du coin inférieur gauche.
     * @param width   Largeur de l'image du titre.
     * @param height  Hauteur de l'image du titre.
     * @param texture La texture du titre à afficher.
     */
    public Title(float x, float y, float width, float height, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
        this.sprite.setSize(width, height);
    }
}
