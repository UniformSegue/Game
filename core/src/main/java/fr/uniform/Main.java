package fr.uniform;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.uniform.object.Block;
import fr.uniform.object.Button;
import fr.uniform.object.Lane;

import java.util.ArrayList;
import java.util.List;

//Toutes les mesures dans le programme sont en pixels


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    //Game Environnement
    private GameEnvironnement gameEnvironnement;

    //Rectangle testRectangle;
    //FitViewport viewport;
    private ShapeRenderer shapeRenderer;
    private float screenHeight;
    private float screenWidth;
    private SpriteBatch batch;

    //blocks
    private List<Block> blocks;

    //button
    private List<Button> buttons;
    private Button button_f;


    //lanes
    private List<Lane> lanes;
    private Lane lane_f;

    @Override
    public void create() {
        //Game variable set
        gameEnvironnement = new GameEnvironnement(4.0f);

        setScreen(new FirstScreen());
        //testRectangle = new Rectangle();
        //viewport = new FitViewport(8, 5);
        shapeRenderer = new ShapeRenderer();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        batch = new SpriteBatch();

        //Block Creation
        blocks = new ArrayList<>();

        //Lane Creation
        lanes = new ArrayList<>();

        lane_f = new Lane(500,Texture_File.TEXTURE_LANE_WIDTH,Texture_File.TEXTURE_LANE_HEIGHT, button_f ,Texture_File.LANE_DEFAULT);

        //Button Creation
        buttons = new ArrayList<>();

        button_f = new Button(lane_f.x-((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH)/2)
            ,lane_f.y-Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.TEXTURE_BUTTON_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,Texture_File.BUTTON_F_UNPRESS,Texture_File.BUTTON_F_PRESS);
        buttons.add(button_f);

        lane_f.assignedButton = button_f;
        lanes.add(lane_f);




    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            button_f.clicked();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            blocks.add(Block.spawnBlock(lane_f,gameEnvironnement,screenHeight));

        }
    }

    private void logic() {

        float delta = Gdx.graphics.getDeltaTime();

        //Logics Blocks fallen
        for (Block block : blocks) {

            if (block.visible) {
                if (block.fallen) {
                    block.checkBlockClicked();
                    block.sprite.translateY(-block.vitesse);
                    if (block.sprite.getY() < -block.height) {

                        block.visible = false;
                    }
                }
            }
        }

    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        /* Ordre de passage de render des items est important il permet de les mettre sur different plans
        le block dessiner en dernier sera au premier plan */
        //Render Lane
        for (Lane lane : lanes) {
            if (lane.playabled) {
                batch.begin();
                batch.draw(lane.texture, lane.x, lane.y, lane.width, lane.height);
                batch.end();
            }
        }

        //Render Buttons
        for (Button button : buttons) {
            batch.begin();
            if (!button.clicked) {
                batch.draw(button.texture_unpressed, button.x, button.y, button.width, button.height);
            } else {
                batch.draw(button.texture_pressed, button.x, button.y, button.width, button.height);
                button_f.clicked();
            }
            batch.end();
        }

        //Render Blocks
        for (Block block : blocks) {
            if (block.visible) {
                batch.begin();
                //Regle opacite du block a Texture_File.TEXTURE_OPACITY
                block.sprite.draw(batch);
                batch.end();
            }
        }

    }




}
