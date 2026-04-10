package fr.uniform;

import fr.uniform.object.game.Block;
import fr.uniform.object.game.Button;
import fr.uniform.object.game.ErrorIndiquator;
import fr.uniform.object.game.PasseIndiquator;
import fr.uniform.utils.ComboController;

import java.util.List;

public class GameEnvironnement {

    public boolean gameOver;
    public boolean turbo;
    public float vitesse_actuelle_pixel_per_frame;
    public float vitesse_actuelle_pixel_per_ms;
    public float block_vitesse_pixel_per_frame;
    public float block_vitesse_pixel_per_ms;
    public float block_vitesse_turbo_pixel_per_frame;
    public float block_vitesse_turbo_pixel_per_ms;
    public int combo;
    public float timer_button_f;
    public float timer_button_d;
    public int hauter_y_line;
    public boolean souffleActive;
    public int nombre_lane;

    private ErrorIndiquator errorIndiquator;
    private PasseIndiquator passeIndiquator;
    ComboController comboController;
    public List<Button> buttons;

    //Sound Config
    public boolean soundButton;


    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public GameEnvironnement(float vitesse, float vitesse_turbo, int hauter_y_line, ErrorIndiquator errorIndiquator, PasseIndiquator passeIndiquator, ComboController comboController,boolean soundButton, boolean souffleActive, int nombre_lane) {
        this.turbo = false;
        this.block_vitesse_turbo_pixel_per_frame = vitesse_turbo;
        this.block_vitesse_pixel_per_frame = vitesse;
        this.block_vitesse_pixel_per_ms = (vitesse * 60) / 1000;
        this.block_vitesse_turbo_pixel_per_ms = (vitesse_turbo * 60) / 1000;
        this.vitesse_actuelle_pixel_per_frame = block_vitesse_pixel_per_frame;
        this.vitesse_actuelle_pixel_per_ms = block_vitesse_pixel_per_ms;
        this.errorIndiquator = errorIndiquator;
        this.passeIndiquator = passeIndiquator;
        this.comboController = comboController;
        this.hauter_y_line = hauter_y_line;
        this.souffleActive = souffleActive;
        this.soundButton = soundButton;
        this.nombre_lane = nombre_lane;

        comboController.setIndiquator();

    }

    public void ComboManager(boolean resultClicked, Block block) {


        if(resultClicked) {
            if(combo <99) {combo++;}

            comboController.enabledComboIndiquator();
            comboController.updateNumber(combo);
            this.errorIndiquator.invisible();
            this.passeIndiquator.visible();
            System.out.println("Combo: " + combo);
        }else{
            combo = 0;
            comboController.disableComboIndiquator();
            this.errorIndiquator.visible();
            this.passeIndiquator.invisible();

        }
    }

    public void startTurbo() {
        this.turbo = true;
        vitesse_actuelle_pixel_per_frame = block_vitesse_turbo_pixel_per_frame;
        vitesse_actuelle_pixel_per_ms = block_vitesse_turbo_pixel_per_ms;

    }
    public void endTurbo() {
        this.turbo = false;
        vitesse_actuelle_pixel_per_frame = block_vitesse_pixel_per_frame;
        vitesse_actuelle_pixel_per_ms = block_vitesse_pixel_per_ms;
    }


    public boolean checkSouffle() {
        return buttons.get(6).clicked;
    }

}
