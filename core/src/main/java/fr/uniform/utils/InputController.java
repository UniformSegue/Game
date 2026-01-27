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

        if (keycode == Input.Keys.D) {

            Button button_d = buttons.get(0);
            button_d.clicked();
            checkButton(lanes.get(0),button_d);

            return true;
        }
        if (keycode == Input.Keys.F) {

            Button button_f = buttons.get(1);
            button_f.clicked();
            checkButton(lanes.get(1),button_f);

            return true;
        }

        if (keycode == Input.Keys.G) {

            Button button_g = buttons.get(2);
            button_g.clicked();
            checkButton(lanes.get(2),button_g);

            return true;
        }
        if (keycode == Input.Keys.H) {

            Button button_h = buttons.get(3);
            button_h.clicked();
            checkButton(lanes.get(3),button_h);

            return true;
        }
        if (keycode == Input.Keys.J) {

            Button button_j = buttons.get(4);
            button_j.clicked();
            checkButton(lanes.get(4),button_j);

            return true;
        }
        if (keycode == Input.Keys.K) {

            Button button_k = buttons.get(5);
            button_k.clicked();
            checkButton(lanes.get(5),button_k);

            return true;
        }
        if (keycode == Input.Keys.T) {

            gameEnvironnement.startTurbo();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.D) {
            Button button_d = buttons.get(0);
            button_d.unclicked();
            return true;
        }
        if (keycode == Input.Keys.F) {
            Button button_f = buttons.get(1);
            button_f.unclicked();
            return true;
        }

        if (keycode == Input.Keys.G) {
            Button button_g = buttons.get(2);
            button_g.unclicked();
            return true;
        }
        if (keycode == Input.Keys.H) {
            Button button_h = buttons.get(3);
            button_h.unclicked();
            return true;
        }
        if (keycode == Input.Keys.J) {
            Button button_j = buttons.get(4);
            button_j.unclicked();
            return true;
        }
        if (keycode == Input.Keys.K) {
            Button button_k = buttons.get(5);
            button_k.unclicked();
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

                if (block.sprite_default.getY() < lowestY) {
                    lowestY = block.sprite_default.getY();
                    lowest = block;
                }
            }
        }

        return lowest;
    }


}
