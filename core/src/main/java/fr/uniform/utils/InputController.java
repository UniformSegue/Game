package fr.uniform.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import fr.uniform.GameEnvironnement;
import fr.uniform.object.game.Block;
import fr.uniform.object.game.Button;
import fr.uniform.object.game.Lane;
import fr.uniform.screen.LevelScreen;

import java.util.List;

/**
 * Contrôleur gérant les entrées clavier du joueur.
 * Intercepte les touches pressées et relâchées pour activer les boutons des pistes,
 * gérer la mécanique de souffle, déclencher le turbo ou mettre le jeu en pause.
 */
public class InputController extends InputAdapter {

    private LevelScreen levelScreen;
    private final GameEnvironnement gameEnvironnement;
    private final List<Lane> lanes;
    private List<Button> buttons;

    // Références directes aux boutons pour un accès rapide
    private final Button button_d, button_f, button_g, button_h, button_j, button_k, button_souffle;

    /**
     * Initialise le contrôleur d'entrées.
     *
     * @param levelScreen       L'écran de jeu actuel (pour la gestion de la pause).
     * @param buttons           La liste des boutons associés aux pistes.
     * @param gameEnvironnement L'environnement de jeu contenant les variables globales (turbo, souffle).
     * @param lanes             La liste des pistes contenant les blocs (notes).
     */
    public InputController(LevelScreen levelScreen, List<Button> buttons, GameEnvironnement gameEnvironnement, List<Lane> lanes) {
        this.levelScreen = levelScreen;
        this.buttons = buttons;
        this.gameEnvironnement = gameEnvironnement;
        this.lanes = lanes;

        // Assignation selon l'ordre défini dans l'initialisation du LevelScreen
        this.button_d = buttons.get(0);
        this.button_f = buttons.get(1);
        this.button_g = buttons.get(2);
        this.button_h = buttons.get(3);
        this.button_j = buttons.get(4);
        this.button_k = buttons.get(5);
        this.button_souffle = buttons.get(6);
    }

    @Override
    public boolean keyDown(int keycode) {

        // --- GESTION DE LA PAUSE ---
        if (keycode == Input.Keys.ESCAPE) {
            levelScreen.togglePause();
            return true;
        }

        // --- GESTION DU SOUFFLE ---
        if (keycode == Input.Keys.Q) {
            button_souffle.clicked();
            return true;
        }

        // Si le mode souffle est activé, on bloque les inputs classiques tant que la condition n'est pas remplie
        if (gameEnvironnement != null && gameEnvironnement.souffleActive) {
            if (!gameEnvironnement.checkSouffle()) {
                return false;
            }
        }

        // --- GESTION DES TOUCHES DE JEU (LANES) ---
        if (keycode == Input.Keys.D) {
            button_d.clicked();
            checkButton(lanes.get(0), button_d);
            return true;
        }
        if (keycode == Input.Keys.F) {
            button_f.clicked();
            checkButton(lanes.get(1), button_f);
            return true;
        }
        if (keycode == Input.Keys.G) {
            button_g.clicked();
            checkButton(lanes.get(2), button_g);
            return true;
        }
        if (keycode == Input.Keys.H) {
            button_h.clicked();
            checkButton(lanes.get(3), button_h);
            return true;
        }
        if (keycode == Input.Keys.J) {
            button_j.clicked();
            checkButton(lanes.get(4), button_j);
            return true;
        }
        if (keycode == Input.Keys.K) {
            button_k.clicked();
            checkButton(lanes.get(5), button_k);
            return true;
        }

        // --- GESTION DU TURBO ---
        if (keycode == Input.Keys.T) {
            if (gameEnvironnement != null) {
                gameEnvironnement.startTurbo();
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        // --- RELÂCHEMENT DU SOUFFLE ---
        if (keycode == Input.Keys.Q) {
            button_souffle.unclicked();
            return true;
        }

        // --- RELÂCHEMENT DES TOUCHES DE JEU ---
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

    /**
     * Vérifie si un bouton peut être pressé (mécanisme anti-spam)
     * et réinitialise son délai si une note a été validée.
     *
     * @param lane   La piste correspondante à vérifier.
     * @param button Le bouton qui vient d'être pressé.
     */
    private void checkButton(Lane lane, Button button) {
        // Cooldown de 0.2s pour éviter de spammer la touche et casser le système de combo
        if (button.timer < 0.2f) {
            return;
        }

        Block target = getLowestActiveBlock(lane);

        if (target != null) {
            // Si le bloc est devenu invisible (donc potentiellement validé ou détruit)
            boolean success = !target.visible;
            if (success) {
                button.timer = 0; // Réinitialisation du cooldown du bouton
            }
        }
    }

    /**
     * Parcourt les blocs d'une piste pour trouver celui qui est le plus bas à l'écran.
     * Utile pour la détection de collision afin de cibler la première note qui arrive.
     *
     * @param lane La piste sur laquelle chercher.
     * @return Le bloc actif le plus bas, ou null s'il n'y en a aucun.
     */
    private Block getLowestActiveBlock(Lane lane) {
        Block lowest = null;
        float lowestY = Float.MAX_VALUE;

        List<Block> blocks = lane.blocks;
        for (Block block : blocks) {
            if (block == null) continue;

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
