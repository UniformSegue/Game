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

public class EcranOptions implements Screen {

    private final Game game;
    private final Stage stage;
    private final Skin skin;
    private SpriteBatch batch;
    private Background background;
    private Sprite ensimLogo;

    // Pour sauvegarder le volume sur l'ordinateur du joueur
    private Preferences prefs;

    public EcranOptions(Game game) {
        this.game = game;
        batch = new SpriteBatch();

        // On récupère ou on crée le fichier de sauvegarde des options
        prefs = Gdx.app.getPreferences("UniformGameOptions");
        // On récupère le volume sauvegardé (par défaut 0.5f, soit 50%)
        float volumeSauvegarde = prefs.getFloat("musicVolume", 0.5f);

        background = new Background(0, 0, GAME_SPEC.width, GAME_SPEC.height, Texture_File.BACKGROUND);

        //Logo ENSIM
        ensimLogo = new Sprite(Texture_File.ENSIM_LOGO);
        int width_ensim = 534;
        int height_ensim = 134;
        ensimLogo.setSize(width_ensim,height_ensim);
        ensimLogo.setPosition(1920-width_ensim,45);
        stage = new Stage(new FitViewport(GAME_SPEC.width, GAME_SPEC.height));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/pixthulhu-ui.json"));
        skin.getFont("font").getData().markupEnabled = true;
        skin.getFont("title").getData().markupEnabled = true;

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // --- CRÉATION DES ÉLÉMENTS ---

        Label titre = new Label("OPTIONS", skin, "title");

        // Le texte qui affichera le pourcentage
        final Label labelVolume = new Label("Volume de la musique : " + (int)(volumeSauvegarde * 100) + "%[]", skin);

        // Le SLIDER (Min: 0, Max: 1, Pas: 0.05, Vertical: false)
        final Slider sliderVolume = new Slider(0f, 1f, 0.05f, false, skin);
        sliderVolume.setValue(volumeSauvegarde); // On place le curseur là où le joueur l'avait laissé

        TextButton btnRetour = new TextButton("Retour", skin);

        // --- ACTIONS ---

        // Quand le joueur bouge le curseur
        sliderVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float nouveauVolume = sliderVolume.getValue();

                //On met à jour le texte en temps réel
                labelVolume.setText("Volume de la musique : " + (int)(nouveauVolume * 100) + "%[]");

                //On sauvegarde la nouvelle valeur dans les Préférences
                prefs.putFloat("musicVolume", nouveauVolume);
                prefs.flush(); // IMPORTANT : C'est ce qui force l'écriture sur le disque dur !

            }
        });

        // Quand on clique sur Retour
        btnRetour.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        final CheckBox checkFullscreen = new CheckBox("Plein ecran", skin);

        // On vérifie si le jeu est DÉJÀ en plein écran pour cocher la case par défaut
        checkFullscreen.setChecked(Gdx.graphics.isFullscreen());

        //L'action quand on clique dessus
        checkFullscreen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean isChecked = checkFullscreen.isChecked();

                if (isChecked) {

                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                } else {

                    Gdx.graphics.setWindowedMode(1920, 1080);
                }
            }
        });


        // --- PLACEMENT DANS LA TABLE ---
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
