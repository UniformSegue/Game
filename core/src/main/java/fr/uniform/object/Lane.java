package fr.uniform.object;

import com.badlogic.gdx.graphics.Texture;
import fr.uniform.Texture_File;

public class Lane {

    public int x;
    public int y;
    public int width;
    public int height;
    public Button assignedButton;
    public Texture texture;
    public boolean playabled;

    public Lane(int x, int width, int height, Button assignedButton, Texture texture) {
        this.x = x;
        this.y = 200;
        this.width = width;
        this.height = height;
        this.assignedButton = assignedButton;
        this.texture = texture;
        this.playabled = true;
    }

    public void playabled(){
        this.playabled = !playabled;
    }
}
