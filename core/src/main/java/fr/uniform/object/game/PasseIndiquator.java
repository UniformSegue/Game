package fr.uniform.object.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Indicateur visuel de réussite (ex: "Perfect", "Good" ou note validée).
 * Gère l'affichage d'un sprite à des coordonnées précises lorsque le joueur réussit à valider un bloc.
 */
public class PasseIndiquator {

    // --- Rendu visuel ---
    public Sprite sprite;
    public Texture texture;

    // --- Propriétés spatiales et état ---
    public int x;
    public int y;
    public boolean visible = false;

    /**
     * Initialise l'indicateur de réussite.
     *
     * @param x       Position X à l'écran.
     * @param y       Position Y à l'écran.
     * @param width   Largeur du sprite.
     * @param height  Hauteur du sprite.
     * @param texture Texture représentant la réussite à afficher.
     */
    public PasseIndiquator(int x, int y, int width, int height, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;

        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
        this.sprite.setSize(width, height);
    }

    /**
     * Rend l'indicateur de réussite visible à l'écran.
     */
    public void visible() {
        this.visible = true;
    }

    /**
     * Masque l'indicateur de réussite de l'écran.
     */
    public void invisible() {
        this.visible = false;
    }
}
