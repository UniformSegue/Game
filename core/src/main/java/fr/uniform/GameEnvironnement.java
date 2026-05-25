package fr.uniform;

import fr.uniform.object.game.Block;
import fr.uniform.object.game.Button;
import fr.uniform.object.game.ErrorIndiquator;
import fr.uniform.object.game.PasseIndiquator;
import fr.uniform.utils.ComboController;

import java.util.List;

/**
 * Gestionnaire de l'environnement global d'une partie.
 * Centralise l'état du jeu (vitesses, mode turbo, combo actuel, score maximum)
 * et fait le lien entre la logique métier et les retours visuels (indicateurs UI).
 */
public class GameEnvironnement {

    // --- État global du jeu ---
    public boolean gameOver;
    public boolean turbo;
    public int combo;
    public int maxcombo = 0;

    // --- Configuration du niveau ---
    public int hauter_y_line;
    public boolean souffleActive;
    public int nombre_lane;
    public boolean soundButton;

    // --- Gestion des vitesses (Pixels par frame et par ms) ---
    public float vitesse_actuelle_pixel_per_frame;
    public float vitesse_actuelle_pixel_per_ms;

    public float block_vitesse_pixel_per_frame;
    public float block_vitesse_pixel_per_ms;

    public float block_vitesse_turbo_pixel_per_frame;
    public float block_vitesse_turbo_pixel_per_ms;

    // --- Variables de temporisation (potentiellement WIP) ---
    public float timer_button_f;
    public float timer_button_d;

    // --- Composants UI et Entrées ---
    private ErrorIndiquator errorIndiquator;
    private PasseIndiquator passeIndiquator;
    ComboController comboController;
    public List<Button> buttons;

    /**
     * Initialise l'environnement de la partie.
     * Calcule automatiquement les vitesses de défilement en fonction des FPS cibles (60 par défaut).
     */
    public GameEnvironnement(float vitesse, float vitesse_turbo, int hauter_y_line,
                             ErrorIndiquator errorIndiquator, PasseIndiquator passeIndiquator,
                             ComboController comboController, boolean soundButton,
                             boolean souffleActive, int nombre_lane) {

        this.turbo = false;

        // Configuration des vitesses normales
        this.block_vitesse_pixel_per_frame = vitesse;
        this.block_vitesse_pixel_per_ms = (vitesse * 60) / 1000;

        // Configuration des vitesses turbo
        this.block_vitesse_turbo_pixel_per_frame = vitesse_turbo;
        this.block_vitesse_turbo_pixel_per_ms = (vitesse_turbo * 60) / 1000;

        // Vitesse actuelle appliquée au jeu (normale au démarrage)
        this.vitesse_actuelle_pixel_per_frame = block_vitesse_pixel_per_frame;
        this.vitesse_actuelle_pixel_per_ms = block_vitesse_pixel_per_ms;

        // Assignation des composants et paramètres
        this.errorIndiquator = errorIndiquator;
        this.passeIndiquator = passeIndiquator;
        this.comboController = comboController;
        this.hauter_y_line = hauter_y_line;
        this.souffleActive = souffleActive;
        this.soundButton = soundButton;
        this.nombre_lane = nombre_lane;

        // Préparation de l'affichage du combo
        comboController.setIndiquator();
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    /**
     * Gère la réussite ou l'échec d'une note, met à jour le combo et les indicateurs visuels.
     *
     * @param resultClicked true si le joueur a validé la note, false s'il l'a ratée.
     * @param block         Le bloc concerné par l'action.
     */
    public void ComboManager(boolean resultClicked, Block block) {
        if (resultClicked) {
            // Le joueur a réussi
            if (combo < 99) {
                combo++;
                changeMaxCombo(combo);
            }

            comboController.enabledComboIndiquator();
            comboController.updateNumber(combo);

            this.errorIndiquator.invisible();
            this.passeIndiquator.visible();
        } else {
            // Le joueur a raté, le combo retombe à zéro
            combo = 0;
            comboController.disableComboIndiquator();

            this.errorIndiquator.visible();
            this.passeIndiquator.invisible();
        }
    }

    /**
     * Active le mode Turbo, accélérant la vitesse de défilement des notes.
     */
    public void startTurbo() {
        this.turbo = true;
        this.vitesse_actuelle_pixel_per_frame = block_vitesse_turbo_pixel_per_frame;
        this.vitesse_actuelle_pixel_per_ms = block_vitesse_turbo_pixel_per_ms;
    }

    /**
     * Désactive le mode Turbo, ramenant la vitesse de défilement à la normale.
     */
    public void endTurbo() {
        this.turbo = false;
        this.vitesse_actuelle_pixel_per_frame = block_vitesse_pixel_per_frame;
        this.vitesse_actuelle_pixel_per_ms = block_vitesse_pixel_per_ms;
    }

    /**
     * Vérifie si le bouton assigné à l'action de souffle est actuellement pressé.
     * L'index 6 correspond au bouton_souffle défini dans l'InputController.
     *
     * @return true si le joueur souffle/appuie sur le bouton dédié.
     */
    public boolean checkSouffle() {
        return buttons.get(6).clicked;
    }

    /**
     * Met à jour le score de combo maximum de la partie si la valeur actuelle le dépasse.
     *
     * @param comboChange La valeur actuelle du combo à comparer.
     */
    private void changeMaxCombo(int comboChange) {
        if (maxcombo < comboChange) {
            maxcombo = comboChange;
        }
    }
}
