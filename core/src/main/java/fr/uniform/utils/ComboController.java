package fr.uniform.utils;

import fr.uniform.Texture_File;
import fr.uniform.object.game.ComboIndiquator;

/**
 * Contrôleur gérant l'affichage du compteur de combo en jeu.
 * Supervise l'indicateur multiplicateur ("x") ainsi que l'affichage dynamique des dizaines et des unités.
 */
public class ComboController {

    public ComboIndiquator comboIndiquatorCroix;
    public ComboIndiquator comboIndiquatorPremier;
    public ComboIndiquator comboIndiquatorDeuxieme;

    private int x;
    private int y;

    public ComboController(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Initialise les indicateurs visuels du combo en les plaçant correctement.
     * Doit être appelé avant la première mise à jour de l'affichage.
     */
    public void setIndiquator() {
        // Indicateur du signe multiplicateur ("x" correspondant à l'index 10 dans la spritesheet)
        comboIndiquatorCroix = new ComboIndiquator(x, y, Texture_File.COMBO_ALL_NUMBERS);
        comboIndiquatorCroix.changeTexture(10);

        // Indicateur du premier chiffre (dizaines, ou unités si le combo est < 10)
        comboIndiquatorPremier = new ComboIndiquator(x + Texture_File.COMBO_WIDTH_TEXTURE, y, Texture_File.COMBO_ALL_NUMBERS);
        comboIndiquatorPremier.changeTexture(0);

        // Indicateur du second chiffre (unités, affiché uniquement si le combo est >= 10)
        comboIndiquatorDeuxieme = new ComboIndiquator(x + 2 * Texture_File.COMBO_WIDTH_TEXTURE, y, Texture_File.COMBO_ALL_NUMBERS);
        comboIndiquatorDeuxieme.changeTexture(0);
    }

    /**
     * Met à jour les textures pour afficher la valeur de combo actuelle.
     * Bascule automatiquement entre un affichage à 1 ou 2 chiffres.
     *
     * @param number Le combo actuel du joueur (limité visuellement à 99).
     */
    public void updateNumber(int number) {
        if (number < 10) {
            comboIndiquatorPremier.changeTexture(number);
            comboIndiquatorDeuxieme.visible = false;
        } else {
            int dizaine = number / 10;
            int unite = number % 10; // Utilisation du modulo pour isoler rapidement les unités

            comboIndiquatorPremier.changeTexture(dizaine);

            comboIndiquatorDeuxieme.visible = true;
            comboIndiquatorDeuxieme.changeTexture(unite);
        }
    }

    /**
     * Rend tous les éléments du combo visibles à l'écran.
     */
    public void enabledComboIndiquator() {
        if (comboIndiquatorCroix != null) comboIndiquatorCroix.visible = true;
        if (comboIndiquatorPremier != null) comboIndiquatorPremier.visible = true;
        if (comboIndiquatorDeuxieme != null) comboIndiquatorDeuxieme.visible = true;
    }

    /**
     * Masque complètement l'affichage du combo.
     */
    public void disableComboIndiquator() {
        if (comboIndiquatorCroix != null) comboIndiquatorCroix.visible = false;
        if (comboIndiquatorPremier != null) comboIndiquatorPremier.visible = false;
        if (comboIndiquatorDeuxieme != null) comboIndiquatorDeuxieme.visible = false;
    }
}
