package fr.uniform.object.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Ocarina {

    public float x,y;
    public float width,height;
    public Sprite sprite;

    public Ocarina(float x, float y, float width, float height, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = new Sprite(texture);

        sprite.setPosition(x, y);
        sprite.setSize(width, height);
    }
}
