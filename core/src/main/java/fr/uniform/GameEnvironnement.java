package fr.uniform;

import com.badlogic.gdx.Gdx;
import fr.uniform.object.Block;
import fr.uniform.object.Button;
import fr.uniform.object.ErrorIndiquator;
import fr.uniform.object.PasseIndiquator;

import java.util.ArrayList;
import java.util.List;

public class GameEnvironnement {

    public boolean gameOver;
    public boolean turbo;
    public float block_vitesse_pixel_per_frame;
    public float block_vitesse_pixel_per_ms;
    public int combo;
    public float timer_button_f;
    public float timer_button_d;
    public int hauter_y_line;

    private ErrorIndiquator errorIndiquator;
    private PasseIndiquator passeIndiquator;
    public List<Button> buttons;

    public GameEnvironnement(float vitesse, int hauter_y_line, ErrorIndiquator errorIndiquator, PasseIndiquator passeIndiquator) {
        this.turbo = false;

        this.block_vitesse_pixel_per_frame = vitesse;
        this.block_vitesse_pixel_per_ms = (vitesse * 60) / 1000;
        this.errorIndiquator = errorIndiquator;
        this.passeIndiquator = passeIndiquator;
        this.hauter_y_line = hauter_y_line;

    }

    public void ComboManager(boolean resultClicked) {

        if(resultClicked) {
            combo++;
            this.errorIndiquator.invisible();
            this.passeIndiquator.visible();
            System.out.println("Combo: " + combo);
        }else{
            combo = 0;
            this.errorIndiquator.visible();
            this.passeIndiquator.invisible();

        }
    }


}
