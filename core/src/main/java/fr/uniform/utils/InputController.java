package fr.uniform.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import fr.uniform.GameEnvironnement;
import fr.uniform.object.Block;
import fr.uniform.object.Button;
import fr.uniform.object.Lane;

import java.util.List;

public class InputController extends InputAdapter {

    private List<Button> buttons;
    private GameEnvironnement gameEnvironnement;
    private List<Lane> lanes;

    public InputController(List<Button> buttons, GameEnvironnement gameEnvironnement, List<Lane> lanes) {
        this.buttons = buttons;
        this.gameEnvironnement = gameEnvironnement;
        this.lanes = lanes;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.F) {

            Button button_f = buttons.get(0);
            button_f.clicked();
            checkButton(lanes.get(0),button_f);


            return true;
        }
        if (keycode == Input.Keys.D) {

            Button button_d = buttons.get(1);
            button_d.clicked();
            checkButton(lanes.get(1),button_d);

            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.F) {
            Button button_f = buttons.get(0);
            button_f.unclicked();
            return true;
        }
        if (keycode == Input.Keys.D) {
            Button button_f = buttons.get(1);
            button_f.unclicked();
            return true;
        }
        return false;
    }

    private boolean checkButton(Lane lane,Button button){

        if (button.timer < 0.2f) {
            return true; // On bloque l'action
        }

        Block target = getLowestActiveBlock(lane);

        if (target != null) {

            boolean success = target.checkBlockClicked(gameEnvironnement);
            if (success) {
                button.timer = 0;
                target.visible = false;
            }
        }
        return false;
    }

    private Block getLowestActiveBlock(Lane lane) {
        Block lowest = null;
        float lowestY = Float.MAX_VALUE;

        List<Block> blocks = lane.blocks;
        for (Block block : blocks) {

            if (block.visible && block.fallen) {

                if (block.sprite.getY() < lowestY) {
                    lowestY = block.sprite.getY();
                    lowest = block;
                }
            }
        }

        return lowest;
    }


}
