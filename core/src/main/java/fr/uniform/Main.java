package fr.uniform;

import com.badlogic.gdx.Game;

import fr.uniform.screen.EcranFinNiveau;
import fr.uniform.screen.MenuScreen;

public class Main extends Game{
    @Override
    public void create() {
        Texture_File.init(); // On charge toutes les textures
        this.setScreen(new MenuScreen(this));

    }

    @Override
    public void dispose() {
        super.dispose();
        Texture_File.dispose();
    }
}
