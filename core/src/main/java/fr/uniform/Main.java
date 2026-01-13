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
    private Block block_red;
    private List<Block> blocks;

    @Override
    public void create() {
        setScreen(new FirstScreen());
        testRectangle = new Rectangle();
        viewport = new FitViewport(8, 5);
        shapeRenderer = new ShapeRenderer();
        block_blue = new Block(50,50,100,100,Color.BLUE);
        block_blue = new Block(100,100,100,100,Color.BLUE);
        blocks = new ArrayList<Block>();
        blocks.add(block_blue);
        blocks.add(block_red);
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
            //bucketSprite.translateX(speed * delta);
            block.setX(block.getX() + 1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            block.setX(block.getX() - 1);
        }

        if (Gdx.input.isTouched()) {
            //touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            //viewport.unproject(touchPos);
            //bucketSprite.setCenterX(touchPos.x);
        }
    }

    private void logic() {
        ScreenUtils.clear(Color.WHITE);


    }

    private void draw() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


        shapeRenderer.setColor(Color.BLUE);

        // 5. Dessiner le rectangle
        // rect(x, y, largeur, hauteur)
        shapeRenderer.rect(block.getX(), block.getY(), block.getWidth(), block.getHeight());

        // 6. Terminer le dessin (très important !)
        shapeRenderer.end();



    }
}
