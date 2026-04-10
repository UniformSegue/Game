package fr.uniform.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TestMenui implements Screen {

    private Game game;

    Skin skin;
    Stage stage;

    public TestMenui(Game game) {

        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        stage.act(v);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
