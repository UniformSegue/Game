package fr.uniform.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.uniform.GAME_SPEC;
import fr.uniform.Texture_File;
import fr.uniform.object.menu.Background;

/**
 * Écran de configuration des paramètres du jeu.
 * Gère les préférences utilisateur telles que le volume audio et le mode d'affichage (plein écran).
 */
public class EcranOptions implements Screen {

    private final Game game;
    private final Stage stage;
    private final Skin skin;
    private SpriteBatch batch;
    private Background background;
    private Sprite ensimLogo;

    private Preferences prefs;

    public EcranOptions(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();

        // Chargement des préférences utilisateur pour la gestion du volume
        this.prefs = Gdx.app.getPreferences("UniformGameOptions");
        float volumeSauvegarde = prefs.getFloat("musicVolume", 0.5f);

        // Initialisation du fond et des éléments visuels
        this.background = new Background(0, 0, GAME_SPEC.width, GAME_SPEC.height, Texture_File.BACKGROUND);

        this.ensimLogo = new Sprite(Texture_File.ENSIM_LOGO);
        int width_ensim = 534;
        int height_ensim = 134;
        this.ensimLogo.setSize(width_ensim, height_ensim);
        this.ensimLogo.setPosition(1920 - width_ensim, 45);

        // Configuration de la scène (Stage) et de l'interface
        this.stage = new Stage(new FitViewport(GAME_SPEC.width, GAME_SPEC.height));
        Gdx.input.setInputProcessor(stage);

        this.skin = new Skin(Gdx.files.internal("ui/pixthulhu-ui.json"));
        this.skin.getFont("font").getData().markupEnabled = true;
        this.skin.getFont("title").getData().markupEnabled = true;

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Déclaration des éléments de l'interface utilisateur
        Label titre = new Label("OPTIONS", skin, "title");
        final Label labelVolume = new Label("Volume de la musique : " + (int)(volumeSauvegarde * 100) + "%[]", skin);

        // Configuration du curseur de volume (Slider)
        final Slider sliderVolume = new Slider(0f, 1f, 0.05f, false, skin);
        sliderVolume.setValue(volumeSauvegarde);

        TextButton btnRetour = new TextButton("Retour", skin);
        final CheckBox checkFullscreen = new CheckBox("Plein ecran", skin);
        checkFullscreen.setChecked(Gdx.graphics.isFullscreen());

        // --- GESTION DES ÉVÉNEMENTS ---

        // Mise à jour dynamique du volume et sauvegarde
        sliderVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float nouveauVolume = sliderVolume.getValue();
                labelVolume.setText("Volume de la musique : " + (int)(nouveauVolume * 100) + "%[]");

                prefs.putFloat("musicVolume", nouveauVolume);
                prefs.flush();
            }
        });

        // Bascule entre le mode plein écran et fenêtré
        checkFullscreen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (checkFullscreen.isChecked()) {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                } else {
                    Gdx.graphics.setWindowedMode(1920, 1080);
                }
            }
        });

        // Retour au menu principal
        btnRetour.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        // --- ASSEMBLAGE DE LA TABLE ---
        table.add(titre).padBottom(100).row();

        labelVolume.setFontScale(2f);
        table.add(labelVolume).padBottom(20).row();

        table.add(sliderVolume).width(550).padBottom(50).row();
        table.add(checkFullscreen).padBottom(40).row();
        table.add(btnRetour).width(300).height(100);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        batch.begin();
        background.sprite.draw(batch);
        ensimLogo.draw(batch);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
