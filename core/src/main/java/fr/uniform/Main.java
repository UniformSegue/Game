package fr.uniform;

import com.badlogic.gdx.Game;

public class Main extends Game{
    @Override
    public void create() {

        this.setScreen(new LevelScreen(this));
    }
}
