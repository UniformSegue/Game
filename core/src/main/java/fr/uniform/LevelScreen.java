package fr.uniform;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen; // IMPORTANT
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.uniform.object.*;
import fr.uniform.utils.ComboController;
import fr.uniform.utils.InputController;
import fr.uniform.utils.MusicController;
import fr.uniform.utils.VibrationObject;

import java.util.ArrayList;
import java.util.List;

public class LevelScreen implements Screen {
    // On garde une référence vers le jeu principal pour changer d'écran si besoin
    private final Game game;

    // --- Tes variables existantes ---
    private Music music;
    private MusicController musicController;
    private GameEnvironnement gameEnvironnement;
    private ShapeRenderer shapeRenderer;
    private float screenHeight, screenWidth;
    private SpriteBatch batch;
    private ErrorIndiquator errorIndiquator;
    private PasseIndiquator passeIndiquator; // Correction nom si nécessaire
    private ComboController comboController;
    private List<Button> buttons;
    private Button button_f, button_d, button_g, button_h, button_j, button_k, button_souffle;
    private List<Lane> lanes;
    private Lane lane_f, lane_d, lane_g, lane_h, lane_j, lane_k;
    private float vitesse, vitesse_turbo;
    private boolean pause; // pause var

    // Constructeur : On initialise tout ici (anciennement create)
    public LevelScreen(Game game) {
        pause = false;
        this.game = game;

        vitesse = 5;
        vitesse_turbo = 10;

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        // Initialisation des contrôleurs
        comboController = new ComboController(100, 600);
        errorIndiquator = new ErrorIndiquator(1700, 600, Texture_File.ERROR_INDIQUATOR_WIDTH, Texture_File.ERROR_INDIQUATOR_HEIGHT, Texture_File.ERROR_INDIQUATOR);
        PasseIndiquator passeIndiquator = new PasseIndiquator(100, 400, Texture_File.PASS_INDIQUATOR_WIDTH, Texture_File.PASS_INDIQUATOR_HEIGHT, Texture_File.PASS_INDIQUATOR);

        gameEnvironnement = new GameEnvironnement(vitesse, vitesse_turbo, 200, errorIndiquator, passeIndiquator, comboController);

        initLanesAndButtons(); // Méthode pour clarifier le constructeur

        musicController = new MusicController(lanes, gameEnvironnement);
        musicController.loadMusicData("level_data.json");

        music = Gdx.audio.newMusic(Gdx.files.internal(musicController.getAudioFileName()));
        long currentMillis = (long) (music.getPosition() * 1000);
        musicController.generateNotes(currentMillis);
        music.setVolume(.5f);
    }


    private void initLanesAndButtons() {
        vitesse = 5;
        vitesse_turbo = 10;
        //Indiquator
        comboController = new ComboController(100,600);
        errorIndiquator = new ErrorIndiquator(1700,600,
            Texture_File.ERROR_INDIQUATOR_WIDTH,Texture_File.ERROR_INDIQUATOR_HEIGHT,Texture_File.ERROR_INDIQUATOR);
        passeIndiquator = new PasseIndiquator(100,400,
            Texture_File.PASS_INDIQUATOR_WIDTH,Texture_File.PASS_INDIQUATOR_HEIGHT,Texture_File.PASS_INDIQUATOR);
        //Game variable set
        gameEnvironnement = new GameEnvironnement(vitesse,vitesse_turbo,200,errorIndiquator,passeIndiquator, comboController);

        shapeRenderer = new ShapeRenderer();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        batch = new SpriteBatch();


        //Lane Creation
        lanes = new ArrayList<>();
        lane_d = new Lane(430, gameEnvironnement.hauter_y_line, Texture_File.TEXTURE_LANE_WIDTH,Texture_File.TEXTURE_LANE_HEIGHT, button_d ,Texture_File.LANE_DEFAULT, new ArrayList<>());
        lane_f = new Lane(630, gameEnvironnement.hauter_y_line, Texture_File.TEXTURE_LANE_WIDTH,Texture_File.TEXTURE_LANE_HEIGHT, button_f ,Texture_File.LANE_DEFAULT, new ArrayList<>());
        lane_g = new Lane(830, gameEnvironnement.hauter_y_line, Texture_File.TEXTURE_LANE_WIDTH,Texture_File.TEXTURE_LANE_HEIGHT, button_g ,Texture_File.LANE_DEFAULT, new ArrayList<>());
        lane_h = new Lane(1030, gameEnvironnement.hauter_y_line, Texture_File.TEXTURE_LANE_WIDTH,Texture_File.TEXTURE_LANE_HEIGHT, button_h ,Texture_File.LANE_DEFAULT, new ArrayList<>());
        lane_j = new Lane(1230, gameEnvironnement.hauter_y_line, Texture_File.TEXTURE_LANE_WIDTH,Texture_File.TEXTURE_LANE_HEIGHT, button_j ,Texture_File.LANE_DEFAULT, new ArrayList<>());
        lane_k = new Lane(1430, gameEnvironnement.hauter_y_line, Texture_File.TEXTURE_LANE_WIDTH,Texture_File.TEXTURE_LANE_HEIGHT, button_k ,Texture_File.LANE_DEFAULT, new ArrayList<>());


        //Button Creation
        buttons = new ArrayList<>();

        button_d = new Button(lane_d.x-((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH)/2)
            ,lane_d.y-Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.TEXTURE_BUTTON_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.BUTTON_D_UNPRESS,Texture_File.BUTTON_D_PRESS);
        buttons.add(button_d);
        button_f = new Button(lane_f.x-((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH)/2)
            ,lane_f.y-Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.TEXTURE_BUTTON_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.BUTTON_F_UNPRESS,Texture_File.BUTTON_F_PRESS);
        buttons.add(button_f);
        button_g = new Button(lane_g.x-((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH)/2)
            ,lane_g.y-Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.TEXTURE_BUTTON_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.BUTTON_G_UNPRESS,Texture_File.BUTTON_G_PRESS);
        buttons.add(button_g);
        button_h = new Button(lane_h.x-((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH)/2)
            ,lane_h.y-Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.TEXTURE_BUTTON_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.BUTTON_H_UNPRESS,Texture_File.BUTTON_H_PRESS);
        buttons.add(button_h);
        button_j = new Button(lane_j.x-((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH)/2)
            ,lane_j.y-Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.TEXTURE_BUTTON_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.BUTTON_J_UNPRESS,Texture_File.BUTTON_J_PRESS);
        buttons.add(button_j);
        button_k = new Button(lane_k.x-((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH)/2)
            ,lane_k.y-Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.TEXTURE_BUTTON_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.BUTTON_K_UNPRESS,Texture_File.BUTTON_K_PRESS);
        buttons.add(button_k);
        button_souffle = new Button(2000,2000,Texture_File.TEXTURE_BUTTON_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.BUTTON_K_UNPRESS,Texture_File.BUTTON_K_PRESS);
        buttons.add(button_souffle);
        gameEnvironnement.setButtons(buttons);

        lane_d.assignedButton = button_d;
        lanes.add(lane_d);
        lane_f.assignedButton = button_f;
        lanes.add(lane_f);
        lane_g.assignedButton = button_g;
        lanes.add(lane_g);
        lane_h.assignedButton = button_h;
        lanes.add(lane_h);
        lane_j.assignedButton = button_j;
        lanes.add(lane_j);
        lane_k.assignedButton = button_k;
        lanes.add(lane_k);



    }

    @Override
    public void show() {
        // Cette méthode est appelée quand l'écran devient l'écran actif
        music.play();
        Gdx.input.setInputProcessor(new InputController(buttons, gameEnvironnement, lanes));
    }

    @Override
    public void render(float delta) {

        if (!pause){
            // Logique de mise à jour
            logic(delta);
        }

        // Dessin
        draw();
    }

    private void logic(float delta) {
        // Mise à jour des timers de boutons
        for(Button b : buttons) b.timer += delta;

        // Logique des blocs qui tombent
        for (Lane lane : lanes) {
            for (Block block : lane.blocks) {
                block.move(gameEnvironnement);
                VibrationObject.VibrateBlock(block);
            }
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        batch.begin();

        //Render Lane
        for (Lane lane : lanes) {
            if (lane.playabled) {

                batch.draw(lane.texture, lane.x, lane.y, lane.width, lane.height);

            }
        }

        //Render Buttons
        for (Button button : buttons) {

            if (!button.clicked) {
                batch.draw(button.texture_unpressed, button.x, button.y, button.width, button.height);
            } else {
                batch.draw(button.texture_pressed, button.x, button.y, button.width, button.height);

            }
        }

        //Render Blocks
        int count = 0;
        for (Lane lane : lanes){
            List<Block> blocks = lane.blocks;
            for (Block block : blocks) {

                if (block.visible) {
                    //Regle opacite du block a Texture_File.TEXTURE_OPACITY
                    block.sprite_press.draw(batch);
                    block.sprite_default.draw(batch);
                    if (gameEnvironnement.turbo){
                        block.sprite_turbo.draw(batch);
                    }

                }
            }
        }

        //Render Indiquator
        if (errorIndiquator.visible) {
            errorIndiquator.sprite.draw(batch);
        }
        if(passeIndiquator.visible){
            passeIndiquator.sprite.draw(batch);
        }

        //render Combo
        if (comboController.comboIndiquatorCroix.visible) {
            comboController.comboIndiquatorCroix.sprite.draw(batch);
        }
        if (comboController.comboIndiquatorPremier.visible) {
            comboController.comboIndiquatorPremier.sprite.draw(batch);
        }
        if (comboController.comboIndiquatorDeuxieme.visible) {
            comboController.comboIndiquatorDeuxieme.sprite.draw(batch);
        }

        //Render Logo Ensim
        Sprite ensimLogo = new Sprite(new Texture(Gdx.files.internal("ensim_withoutbg.png")));
        int width_ensim = 387;
        int height_ensim = 162;
        ensimLogo.setSize(width_ensim,height_ensim);
        ensimLogo.setPosition(1920-width_ensim,1080-height_ensim-50);
        ensimLogo.draw(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void resume() {
        music.play();
    }

    @Override
    public void hide() {
        // Appelé quand on change d'écran
        music.stop();
    }

    @Override
    public void dispose() {
        // Libération de la mémoire
        batch.dispose();
        shapeRenderer.dispose();
        music.dispose();
    }
}
