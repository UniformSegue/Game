package fr.uniform.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.uniform.Texture_File;

public class ComboIndiquator {

    public Sprite sprite;
    public Texture texture;
    private TextureRegion textureCroix;
    public TextureRegion[][] textureRegionAll;
    public TextureRegion[] numbersTexture;
    public int x;
    public int y;
    public boolean visible = false;

    public ComboIndiquator(int x, int y,Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.textureRegionAll = TextureRegion.split(texture, 16, 16);
        this.numbersTexture = new TextureRegion[10];

        int index = 0;

        for (TextureRegion[] textureRegions : textureRegionAll) {
            for (int j = 0; j < textureRegions.length; j++) {
                if (index < 10) {
                    numbersTexture[index] = textureRegions[j];
                } else if (index == 10) {
                    textureCroix = textureRegions[j];
                }
                index++;
            }
        }
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

    public void changeTexture(int number) {
        switch (number) {
            case 0:
                this.sprite = new Sprite(numbersTexture[0]);
                this.sprite.setPosition(x, y);
                this.sprite.setSize(Texture_File.COMBO_WIDTH_TEXTURE, Texture_File.COMBO_HEIGHT_TEXTURE);
                break;

            case 1:
                this.sprite = new Sprite(numbersTexture[1]);
                this.sprite.setPosition(x, y);
                this.sprite.setSize(Texture_File.COMBO_WIDTH_TEXTURE, Texture_File.COMBO_HEIGHT_TEXTURE);
                break;
            case 2:
                this.sprite = new Sprite(numbersTexture[2]);
                this.sprite.setPosition(x, y);
                this.sprite.setSize(Texture_File.COMBO_WIDTH_TEXTURE, Texture_File.COMBO_HEIGHT_TEXTURE);
                break;
            case 3:
                this.sprite = new Sprite(numbersTexture[3]);
                this.sprite.setPosition(x, y);
                this.sprite.setSize(Texture_File.COMBO_WIDTH_TEXTURE, Texture_File.COMBO_HEIGHT_TEXTURE);
                break;
            case 4:
                this.sprite = new Sprite(numbersTexture[4]);
                this.sprite.setPosition(x, y);
                this.sprite.setSize(Texture_File.COMBO_WIDTH_TEXTURE, Texture_File.COMBO_HEIGHT_TEXTURE);
                break;

            case 5:
                this.sprite = new Sprite(numbersTexture[5]);
                this.sprite.setPosition(x, y);
                this.sprite.setSize(Texture_File.COMBO_WIDTH_TEXTURE, Texture_File.COMBO_HEIGHT_TEXTURE);
                break;
            case 6:
                this.sprite = new Sprite(numbersTexture[6]);
                this.sprite.setPosition(x, y);
                this.sprite.setSize(Texture_File.COMBO_WIDTH_TEXTURE, Texture_File.COMBO_HEIGHT_TEXTURE);
                break;
            case 7:
                this.sprite = new Sprite(numbersTexture[7]);
                this.sprite.setPosition(x, y);
                this.sprite.setSize(Texture_File.COMBO_WIDTH_TEXTURE, Texture_File.COMBO_HEIGHT_TEXTURE);
                break;
            case 8:
                this.sprite = new Sprite(numbersTexture[8]);
                this.sprite.setPosition(x, y);
                this.sprite.setSize(Texture_File.COMBO_WIDTH_TEXTURE, Texture_File.COMBO_HEIGHT_TEXTURE);
                break;
            case 9:
                this.sprite = new Sprite(numbersTexture[9]);
                this.sprite.setPosition(x, y);
                this.sprite.setSize(Texture_File.COMBO_WIDTH_TEXTURE, Texture_File.COMBO_HEIGHT_TEXTURE);
                break;
            case 10:
                this.sprite = new Sprite(textureCroix);
                this.sprite.setPosition(x, y);
                this.sprite.setSize(Texture_File.COMBO_WIDTH_TEXTURE, Texture_File.COMBO_HEIGHT_TEXTURE);
                break;

        }

    }
}
