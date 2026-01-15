package fr.uniform.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;



public class ErrorIndiquator {

    public Sprite sprite;
    public Texture texture;
    public int x;
    public int y;
    public boolean visible = false;

    public ErrorIndiquator(int x, int y,int width, int height, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(width, height);

    }

    public void visible() {
        this.visible = true;
    }
    public void invisible() {
        this.visible = false;
    }
}
