package fr.uniform.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import fr.uniform.GameEnvironnement;

import java.util.List;

public class Button {

    public int x;
    public int y;
    public int width;
    public int height;
    public boolean clicked;
    public Texture texture_unpressed;
    public Texture texture_pressed;
    public Rectangle collision;
    public float timer;



    public Button(int x, int y, int width, int height, Texture texture_unpressed, Texture texture_pressed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture_unpressed = texture_unpressed;
        this.texture_pressed = texture_pressed;
        this.clicked = false;
        this.collision = new Rectangle(x, y, width, height);

    }

    public void clicked() {

        this.clicked = true;
    }
    public void unclicked() {

        this.clicked = false;
    }

}
