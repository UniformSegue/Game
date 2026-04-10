package fr.uniform.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen; // IMPORTANT
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import fr.uniform.GAME_SPEC;
import fr.uniform.GameEnvironnement;
import fr.uniform.Texture_File;
import fr.uniform.object.game.*;
import fr.uniform.object.menu.Background;
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
    private Background background;
    private int nombre_lane;
    private boolean souffle;

    //Pour Overlay
    private InputController inputController;
    private PauseOverlay pauseMenu;
    private Skin skin;
    private String level_data;

    //Debug
    private Label debugLabel;

    // Constructeur : On initialise tout ici (anciennement create)
    public LevelScreen(Game game, float vitesse, float vitesse_turbo, String level_data, int nombre_lane,boolean souffle) {

        pause = false;
        this.game = game;
        this.vitesse = vitesse;
        this.vitesse_turbo = vitesse_turbo;
        this.nombre_lane = nombre_lane;
        this.souffle = souffle;
        this.level_data = level_data; // On sauvegarde la donnée pour pouvoir recommencer
        this.skin = new Skin(Gdx.files.internal("ui/pixthulhu-ui.json"));

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        initLanesAndButtons(); // Méthode pour clarifier le constructeur

        musicController = new MusicController(lanes, gameEnvironnement);
        musicController.loadMusicData(level_data);

        music = Gdx.audio.newMusic(Gdx.files.internal(musicController.getAudioFileName()));
        long currentMillis = (long) (music.getPosition() * 1000);
        musicController.generateNotes(currentMillis);

        Preferences prefs = Gdx.app.getPreferences("UniformGameOptions");
        float volumeEnregistre = prefs.getFloat("musicVolume", 0.5f);

        music.setVolume(volumeEnregistre);
        System.out.println(volumeEnregistre);
        //Background
        background = new Background(0,0, GAME_SPEC.width,GAME_SPEC.height,Texture_File.BACKGROUND);


        pauseMenu = new PauseOverlay(game, this, skin, vitesse, vitesse_turbo, level_data, nombre_lane, souffle);

        // On initialise l'InputController ici plutôt que dans show()
        inputController = new InputController(this, buttons, gameEnvironnement, lanes);


        //Debug
        Label.LabelStyle debugStyle = new Label.LabelStyle(skin.getFont("font"), Color.YELLOW);
        debugLabel = new Label("Calcul de la RAM...", debugStyle);

        // On le place en haut à droite
        debugLabel.setPosition(1920 - 400, 1080 - 50);
    }


    private void initLanesAndButtons() {

        //Indiquator
        comboController = new ComboController(100,600);
        errorIndiquator = new ErrorIndiquator(1700,600,
            Texture_File.ERROR_INDIQUATOR_WIDTH,Texture_File.ERROR_INDIQUATOR_HEIGHT,Texture_File.ERROR_INDIQUATOR);
        passeIndiquator = new PasseIndiquator(100,400,
            Texture_File.PASS_INDIQUATOR_WIDTH,Texture_File.PASS_INDIQUATOR_HEIGHT,Texture_File.PASS_INDIQUATOR);
        //Game variable set
        gameEnvironnement = new GameEnvironnement(vitesse,vitesse_turbo,200,errorIndiquator,passeIndiquator, comboController, false, souffle,nombre_lane);

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

        if(nombre_lane == 4){
            lane_d.playabled();
            lane_k.playabled();
        }

    }

    @Override
    public void show() {
        // Cette méthode est appelée quand l'écran devient l'écran actif
        music.play();
        Gdx.input.setInputProcessor(inputController);
    }

    @Override
    public void render(float delta) {

        if (!pause){
            logic(delta);
        }

        draw();

        if (pause) {
            pauseMenu.render(delta);
        }
    }

    private void logic(float delta) {
        // Mise à jour des timers de boutons
        for(Button b : buttons) b.timer += delta;

        // Logique des blocs qui tombent
        for (Lane lane : lanes) {
            if (lane == null ) continue;
            for (Block block : lane.blocks) {
                if (block ==null) continue;
                block.move(gameEnvironnement);
                VibrationObject.VibrateBlock(block);
            }
        }
    }

    private void draw() {

        long javaRam = Gdx.app.getJavaHeap() / (1024 * 1024);
        long nativeRam = Gdx.app.getNativeHeap() / (1024 * 1024);
        int fps = Gdx.graphics.getFramesPerSecond();

        debugLabel.setText("FPS: " + fps + " | Java: " + javaRam + " MB | Images (Native): " + nativeRam + " MB");

        batch.begin();
        //Background
        background.sprite.draw(batch);


        debugLabel.draw(batch, 1f);

        //Render Lane
        for (Lane lane : lanes) {
            if (lane.playabled) {

                batch.draw(lane.texture, lane.x, lane.y, lane.width, lane.height);

            }
        }

        //Render Buttons
        for (Button button : buttons) {
            if(button == null) continue;
            if(!button.visibled)continue;
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
                if (block ==null) continue;
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



        batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    public void togglePause() {
        pause = !pause;

        if (pause) {
            music.pause();
            // Le menu Pause prend le contrôle de la souris
            Gdx.input.setInputProcessor(pauseMenu.getStage());
        } else {
            music.play();
            // Le jeu reprend le contrôle du clavier
            Gdx.input.setInputProcessor(inputController);
        }
    }

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
        System.out.println("Nettoyage de la RAM de l'ancien niveau...");

        // Les outils de dessin
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();

        //La musique
        if (music != null) {
            music.stop();
            music.dispose();
        }

        //L'interface de pause
        if (pauseMenu != null) pauseMenu.dispose();
        if (skin != null) skin.dispose();

    }
}
