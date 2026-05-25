package fr.uniform.object.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Indicateur visuel d'erreur (ex: "Miss" ou mauvaise manipulation).
 * Gère l'affichage d'un sprite à des coordonnées précises lorsque le joueur rate une note.
 */
public class ErrorIndiquator {

    // --- Rendu visuel ---
    public Sprite sprite;
    public Texture texture;

    // --- Propriétés spatiales et état ---
    public int x;
    public int y;
    public boolean visible = false;

    /**
     * Initialise l'indicateur d'erreur.
     *
     * @param x       Position X à l'écran.
     * @param y       Position Y à l'écran.
     * @param width   Largeur du sprite.
     * @param height  Hauteur du sprite.
     * @param texture Texture représentant l'erreur à afficher.
     */
    public ErrorIndiquator(int x, int y, int width, int height, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;

        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
        this.sprite.setSize(width, height);
    }

    /**
     * Rend l'indicateur d'erreur visible à l'écran.
     */
    public void visible() {
        this.visible = true;
    }

    /**
     * Masque l'indicateur d'erreur de l'écran.
     */
    public void invisible() {
        this.visible = false;
    }
}
