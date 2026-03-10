package fr.uniform.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import fr.uniform.GameEnvironnement;
import fr.uniform.object.Block;
import fr.uniform.object.Button;
import fr.uniform.object.Lane;

import java.util.List;

public class InputController extends InputAdapter {

    private List<Button> buttons;
    private final GameEnvironnement gameEnvironnement;
    private final List<Lane> lanes;
    private final Button button_d, button_f, button_g, button_h, button_j, button_k, button_souffle;


    public InputController(List<Button> buttons, GameEnvironnement gameEnvironnement, List<Lane> lanes) {
        this.buttons = buttons;
        this.gameEnvironnement = gameEnvironnement;
        this.lanes = lanes;
        button_d = buttons.get(0);
        button_f = buttons.get(1);
        button_g = buttons.get(2);
        button_h = buttons.get(3);
        button_j = buttons.get(4);
        button_k = buttons.get(5);
        button_souffle = buttons.get(6);
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.Q) {

            button_souffle.clicked();
            return true;
        }

        if (gameEnvironnement != null) {
            if(!gameEnvironnement.checkSouffle()){
                return false;
            }
        }
        if (keycode == Input.Keys.D) {

            button_d.clicked();
            checkButton(lanes.get(0),button_d);

            return true;
        }
        if (keycode == Input.Keys.F) {

            button_f.clicked();
            checkButton(lanes.get(1),button_f);

            return true;
        }

        if (keycode == Input.Keys.G) {

            button_g.clicked();
            checkButton(lanes.get(2),button_g);

            return true;
        }
        if (keycode == Input.Keys.H) {

            button_h.clicked();
            checkButton(lanes.get(3),button_h);

            return true;
        }
        if (keycode == Input.Keys.J) {
            button_j.clicked();
            checkButton(lanes.get(4),button_j);

            return true;
        }
        if (keycode == Input.Keys.K) {

            button_k.clicked();
            checkButton(lanes.get(5),button_k);

            return true;
        }

        if (keycode == Input.Keys.T) {

            assert gameEnvironnement != null;
            gameEnvironnement.startTurbo();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.Q) {
            button_souffle.unclicked();
            return true;
        }

        if (keycode == Input.Keys.D) {
            button_d.unclicked();
            return true;
        }
        if (keycode == Input.Keys.F) {
            button_f.unclicked();
            return true;
        }

        if (keycode == Input.Keys.G) {
            button_g.unclicked();
            return true;
        }
        if (keycode == Input.Keys.H) {
            button_h.unclicked();
            return true;
        }
        if (keycode == Input.Keys.J) {
            button_j.unclicked();
            return true;
        }
        if (keycode == Input.Keys.K) {
            button_k.unclicked();
            return true;
        }


        return false;
    }

    private void checkButton(Lane lane, Button button){

        if (button.timer < 0.2f) {
            return; // On bloque l'action
        }

        Block target = getLowestActiveBlock(lane);

        if (target != null) {

            boolean success = !target.visible;
            if (success) {
                button.timer = 0;

            }
        }
    }

    private Block getLowestActiveBlock(Lane lane) {
        Block lowest = null;
        float lowestY = Float.MAX_VALUE;

        List<Block> blocks = lane.blocks;
        for (Block block : blocks) {

            if (block.visible && block.fallen) {

                if (block.sprite_default.getY() < lowestY) {
                    lowestY = block.sprite_default.getY();
                    lowest = block;
                }
            }
        }

        return lowest;
    }


}
