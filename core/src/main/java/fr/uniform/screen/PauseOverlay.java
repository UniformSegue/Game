package fr.uniform.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.uniform.GAME_SPEC;
import fr.uniform.screen.LevelScreen;
import fr.uniform.screen.MenuScreen;

public class PauseOverlay {

    private final Stage stage;
    private final Skin skin;

    public PauseOverlay(final Game game, final LevelScreen levelScreen, Skin skin,
                        final float vitesse, final float vitesse_turbo,
                        final String level_data, final int nombre_lane, final boolean souffle) {

        this.skin = skin;
        stage = new Stage(new FitViewport(GAME_SPEC.width, GAME_SPEC.height));

        //Création d'un fond noir semi-transparent (Opacité 70%)
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.7f));
        pixmap.fill();
        Image fondAssombri = new Image(new Texture(pixmap));
        fondAssombri.setFillParent(true);
        stage.addActor(fondAssombri);
        pixmap.dispose(); // On libère la mémoire du Pixmap

        //Création de la table pour centrer les boutons
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        //Création des boutons
        TextButton btnReprendre = new TextButton("Reprendre", skin);
        TextButton btnRecommencer = new TextButton("Recommencer", skin);
        TextButton btnQuitter = new TextButton("Quitter", skin);

        // --- ACTIONS DES BOUTONS ---

        btnReprendre.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                levelScreen.togglePause(); // Enlève la pause
            }
        });

        btnRecommencer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // On relance un nouveau LevelScreen avec les mêmes paramètres
                game.setScreen(new LevelScreen(game, vitesse, vitesse_turbo, level_data, nombre_lane, souffle));
                levelScreen.dispose(); // On détruit l'ancien niveau
            }
        });

        btnQuitter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Retour au menu principal
                game.setScreen(new MenuScreen(game));
                levelScreen.dispose();
            }
        });

        //Placement dans la table
        table.add(btnReprendre).width(300).height(100).padBottom(20).row();
        table.add(btnRecommencer).width(400).height(100).padBottom(20).row();
        table.add(btnQuitter).width(300).height(100);
    }

    // Fonction pour dessiner ce menu
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    public void dispose() {
        stage.dispose();
    }
}
