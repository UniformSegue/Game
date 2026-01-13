package fr.uniform.object;

import com.badlogic.gdx.graphics.Color;

public class Block {

    public int x;
    public int y;
    public int width;
    public int height;
    public Color color;


    public Block(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

}
