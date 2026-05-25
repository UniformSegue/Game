package fr.uniform.object.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.uniform.Texture_File;

/**
 * Composant visuel représentant un caractère (chiffre ou signe multiplicateur)
 * pour l'affichage dynamique du combo en jeu.
 * Gère le découpage de la spritesheet associée et la mise à jour de la texture affichée.
 */
public class ComboIndiquator {

    public Sprite sprite;
    public Texture texture;

    // Régions découpées de la texture principale
    private TextureRegion textureCroix;
    public TextureRegion[] numbersTexture;

    public int x;
    public int y;
    public boolean visible = false;

    /**
     * Constructeur de l'indicateur.
     * Découpe la spritesheet pour extraire les chiffres de 0 à 9 et la croix multiplicatrice.
     */
    public ComboIndiquator(int x, int y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;

        // Découpage de la texture en une grille de 16x16 pixels
        TextureRegion[][] textureRegionAll = TextureRegion.split(texture, 16, 16);
        this.numbersTexture = new TextureRegion[10];

        int index = 0;

        // Parcours du tableau 2D pour mapper les index aux bons caractères
        for (TextureRegion[] textureRegions : textureRegionAll) {
            for (int j = 0; j < textureRegions.length; j++) {
                if (index < 10) {
                    numbersTexture[index] = textureRegions[j]; // Chiffres de 0 à 9
                } else if (index == 10) {
                    textureCroix = textureRegions[j]; // Le 11ème élément (index 10) est la croix "x"
                }
                index++;
            }
        }

        // Initialisation du sprite avec une région par défaut
        this.sprite = new Sprite(numbersTexture[1]);
        this.sprite.setPosition(x, y);
        this.sprite.setSize(Texture_File.COMBO_WIDTH_TEXTURE, Texture_File.COMBO_HEIGHT_TEXTURE);
    }

    public void visible() {
        this.visible = true;
    }

    public void invisible() {
        this.visible = false;
    }

    /**
     * Change le caractère affiché par le sprite.
     * Cette méthode est optimisée : elle ne recrée pas le sprite, elle modifie juste sa région de texture.
     * * @param number Le chiffre (0-9) à afficher, ou 10 pour afficher le signe multiplicateur (croix).
     */
    public void changeTexture(int number) {
        if (number >= 0 && number <= 9) {
            this.sprite.setRegion(numbersTexture[number]);
        } else if (number == 10) {
            this.sprite.setRegion(textureCroix);
        }
    }
}
