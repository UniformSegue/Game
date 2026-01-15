package fr.uniform.object;

import com.badlogic.gdx.graphics.Texture;
import fr.uniform.Texture_File;

import java.util.List;

public class Lane {

    public int x;
    public int y;
    public int width;
    public int height;
    public Button assignedButton;
    public Texture texture;
    public boolean playabled;
    public List<Block> blocks;

    public Lane(int x,int y, int width, int height, Button assignedButton, Texture texture, List<Block> blocks) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.assignedButton = assignedButton;
        this.texture = texture;
        this.playabled = true;
        this.blocks = blocks;

    }

    public void playabled(){
        this.playabled = !playabled;
    }
}
