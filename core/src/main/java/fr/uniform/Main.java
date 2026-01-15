package fr.uniform;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.uniform.object.*;
import fr.uniform.utils.InputController;
import fr.uniform.utils.MusicController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Toutes les mesures dans le programme sont en pixels


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    //debug variable
    float timer;

    //Music
    private Music music;
    private MusicController musicController;

    //Game Environnement
    private GameEnvironnement gameEnvironnement;

    //Rectangle testRectangle;
    //FitViewport viewport;
    private ShapeRenderer shapeRenderer;
    private float screenHeight;
    private float screenWidth;
    private SpriteBatch batch;

    //Error Indiquator
    private ErrorIndiquator errorIndiquator;
    private PasseIndiquator passeIndiquator;

    //blocks

    //button
    private List<Button> buttons;
    private Button button_f;
    private Button button_d;
    private Button button_g;
    private Button button_h;
    private Button button_j;
    private Button button_k;


    //lanes
    private List<Lane> lanes;
    private Lane lane_f;
    private Lane lane_d;
    private Lane lane_g;
    private Lane lane_h;
    private Lane lane_j;
    private Lane lane_k;

    int count_block_passe = 0;

    @Override
    public void create() {


        //Indiquator
        errorIndiquator = new ErrorIndiquator(1700,800,
            Texture_File.ERROR_INDIQUATOR_WIDTH,Texture_File.ERROR_INDIQUATOR_HEIGHT,Texture_File.ERROR_INDIQUATOR);
        passeIndiquator = new PasseIndiquator(100,800,
            Texture_File.PASS_INDIQUATOR_WIDTH,Texture_File.PASS_INDIQUATOR_HEIGHT,Texture_File.PASS_INDIQUATOR);
        //Game variable set
        gameEnvironnement = new GameEnvironnement(4,200,errorIndiquator,passeIndiquator);


        setScreen(new FirstScreen());
        //testRectangle = new Rectangle();
        //viewport = new FitViewport(8, 5);
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

        button_f = new Button(lane_f.x-((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH)/2)
            ,lane_f.y-Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.TEXTURE_BUTTON_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.BUTTON_F_UNPRESS,Texture_File.BUTTON_F_PRESS);
        buttons.add(button_f);

        button_d = new Button(lane_d.x-((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH)/2)
            ,lane_d.y-Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.TEXTURE_BUTTON_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.BUTTON_D_UNPRESS,Texture_File.BUTTON_D_PRESS);
        buttons.add(button_d);
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

        lane_f.assignedButton = button_f;
        lanes.add(lane_f);
        lane_d.assignedButton = button_d;
        lanes.add(lane_d);
        lane_g.assignedButton = button_g;
        lanes.add(lane_g);
        lane_h.assignedButton = button_h;
        lanes.add(lane_h);
        lane_j.assignedButton = button_j;
        lanes.add(lane_j);
        lane_k.assignedButton = button_k;
        lanes.add(lane_k);


        musicController = new MusicController(lanes,gameEnvironnement);
        musicController.loadMusicData("level_data.json");

        music = Gdx.audio.newMusic(Gdx.files.internal(musicController.getAudioFileName()));
        long currentMillis = (long)(music.getPosition() * 1000);
        musicController.generateNotes(currentMillis);
        music.setVolume(.5f);
        music.play();
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        Gdx.input.setInputProcessor(new InputController(buttons,gameEnvironnement,lanes));
    }

    private void logic() {

        float delta = Gdx.graphics.getDeltaTime();
        button_f.timer += delta;
        button_d.timer += delta;
        button_g.timer += delta;
        button_h.timer += delta;
        button_j.timer += delta;
        button_k.timer += delta;

        //Logics Blocks fallen
        for (Lane lane : lanes) {
            List<Block> blocks = lane.blocks;

            for (Block block : blocks) {

                block.move(gameEnvironnement);
            }
        }
        /*
        timer += delta;
        if (timer > 1f) {
            timer = 0;
            lane_f.blocks.add(Block.spawnBlock(lane_f, gameEnvironnement, screenHeight));
            lane_d.blocks.add(Block.spawnBlock(lane_d, gameEnvironnement, screenHeight));
        }*/
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        /* Ordre de passage de render des items est important il permet de les mettre sur different plans
        le block dessiner en dernier sera au premier plan */
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
                    block.sprite.draw(batch);

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

        batch.end();
    }

}
