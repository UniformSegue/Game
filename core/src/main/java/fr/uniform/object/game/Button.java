package fr.uniform.object.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Représente un récepteur d'input situé en bas d'une piste (Lane).
 * Gère son état interactif (pressé ou relâché), son rendu visuel associé,
 * et sa zone de collision (hitbox) pour la validation des notes.
 */
public class Button {

    // --- Propriétés spatiales ---
    public int x;
    public int y;
    public int width;
    public int height;

    // --- États du bouton ---
    public boolean clicked;
    public boolean visibled;

    /** Chronomètre interne, mis à jour dans la boucle de jeu principale pour la gestion des délais. */
    public float timer;

    // --- Rendu et Hitbox ---
    public Texture texture_unpressed;
    public Texture texture_pressed;
    public Rectangle collision;

    /**
     * Constructeur d'un bouton récepteur.
     */
    public Button(int x, int y, int width, int height, Texture texture_unpressed, Texture texture_pressed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.texture_unpressed = texture_unpressed;
        this.texture_pressed = texture_pressed;

        this.clicked = false;
        this.visibled = true;
        this.timer = 0f;

        this.collision = new Rectangle(x, y, width, height);
    }

    /**
     * Passe l'état du bouton en "pressé", ce qui change la texture affichée lors du rendu
     * et permet la validation des blocs qui entrent en collision.
     */
    public void clicked() {
        this.clicked = true;
    }

    /**
     * Réinitialise l'état du bouton en "relâché".
     */
    public void unclicked() {
        this.clicked = false;
    }
}
