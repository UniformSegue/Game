package fr.uniform.object.game;

import com.badlogic.gdx.graphics.Texture;
import java.util.List;

/**
 * Représente une piste verticale sur laquelle les notes (blocs) descendent.
 * Gère ses dimensions, sa texture de fond, les blocs qu'elle contient,
 * ainsi que le bouton de réception associé en bas de l'écran.
 */
public class Lane {

    // --- Propriétés spatiales ---
    public int x;
    public int y;
    public int width;
    public int height;

    // --- Éléments de jeu associés ---
    public Button assignedButton;
    public Texture texture;
    public List<Block> blocks;

    // --- État ---
    /** * Indique si la piste est active et jouable.
     * Utile pour gérer les différents modes de difficulté (ex: 4 touches vs 6 touches).
     */
    public boolean playabled;

    /**
     * Initialise une nouvelle piste de jeu.
     */
    public Lane(int x, int y, int width, int height, Button assignedButton, Texture texture, List<Block> blocks) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.assignedButton = assignedButton;
        this.texture = texture;
        this.blocks = blocks;

        this.playabled = true;
    }

    /**
     * Bascule l'état de la piste (active/inactive).
     * Gère automatiquement la visibilité du bouton de réception associé.
     */
    public void playabled() {
        this.playabled = !this.playabled;

        // Sécurité : on vérifie que le bouton n'est pas null avant de modifier sa propriété
        if (this.assignedButton != null) {
            this.assignedButton.visibled = this.playabled;
        }
    }
}
