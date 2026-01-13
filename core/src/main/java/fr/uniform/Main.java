package fr.uniform;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.uniform.object.Block;

import java.util.ArrayList;
import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    Rectangle testRectangle;
    FitViewport viewport;
    private ShapeRenderer shapeRenderer;
    private Block block_blue;
    private List<Block> blocks;
    private float screenHeight;
    private float screenWidth;

    @Override
    public void create() {
        setScreen(new FirstScreen());
        testRectangle = new Rectangle();
        viewport = new FitViewport(8, 5);
        shapeRenderer = new ShapeRenderer();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        //Block Creation
        block_blue = new Block(0,(int)screenHeight - 100,100,100,Color.BLUE,false);
        blocks = new ArrayList<Block>();
        blocks.add(block_blue);

    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {

            if(!block_blue.fallen) {

                if (!(block_blue.x > screenWidth- block_blue.width)) {
                    block_blue.x = block_blue.x + 1;
                }
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

            if(!block_blue.fallen) {
                if (!(block_blue.x < 0)) {
                    block_blue.x = block_blue.x - 1;
                    }
            }

        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Block block_add = new Block(block_blue.x, block_blue.y, 100,100,Color.RED,true);;
            blocks.add(block_add);
        }

        if (Gdx.input.isTouched()) {
            //touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            //viewport.unproject(touchPos);
            //bucketSprite.setCenterX(touchPos.x);
        }
    }

    private void logic() {
        ScreenUtils.clear(Color.BLACK);

        for (Block block : blocks) {
            if(block.fallen) {
                block.y = block.y - 1;
                if (block.y < 0 - block.height) {
                    block.visible = false;
                }
            }

        }

    }

    private void draw() {


        for (Block block : blocks) {
            if (block.visible) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(block.color);
                shapeRenderer.rect(block.x, block.y, block.width, block.height);
                shapeRenderer.end();
            }
        }






    }
}
