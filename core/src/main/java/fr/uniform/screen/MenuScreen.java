package fr.uniform.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.uniform.GAME_SPEC;
import fr.uniform.Texture_File;
import fr.uniform.object.menu.Background;
import fr.uniform.object.menu.Title;

public class MenuScreen implements Screen {

    private final Game game;
    private final Stage stage;
    private final Skin skin;
    private SpriteBatch batch;

    private Background background;
    private Title title;
    private Sprite ensimLogo;

    public MenuScreen(Game game) {
        this.game = game;

        // --- DÉCOR ET LOGOS ---
        ensimLogo = new Sprite(Texture_File.ENSIM_LOGO);
        int width_ensim = 534;
        int height_ensim = 134;
        ensimLogo.setSize(width_ensim, height_ensim);
        ensimLogo.setPosition(1920 - width_ensim, 45);

        batch = new SpriteBatch();
        background = new Background(0, 0, GAME_SPEC.width, GAME_SPEC.height, Texture_File.BACKGROUND);
        title = new Title(640, 720, Texture_File.TEXTURE_TITLE_WIDTH, Texture_File.TEXTURE_TITLE_HEIGHT, Texture_File.TITLE_DEFAULT);

        // --- SCENE2D ET UI ---
        stage = new Stage(new FitViewport(GAME_SPEC.width, GAME_SPEC.height));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/pixthulhu-ui.json"));
        skin.getFont("font").getData().markupEnabled = true;
        skin.getFont("subtitle").getData().markupEnabled = true;
        skin.getFont("title").getData().markupEnabled = true;

        // Création de la table centrale unique
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // --- CRÉATION DES BOUTONS ---
        TextButton btnPlay = new TextButton("[WHITE]JOUER[]", skin);
        TextButton btnCustomLevel = new TextButton("[WHITE]CUSTOM LEVEL[]", skin); // Le nouveau bouton
        TextButton btnOption = new TextButton("[WHITE]OPTIONS[]", skin);
        TextButton btnQuit = new TextButton("[WHITE]QUITTER[]", skin);

        // --- ACTIONS DES BOUTONS ---
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new EcranOptionsGame(game));
                dispose();
            }
        });

        btnCustomLevel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Menu Custom Level ouvert !");
                // TODO: game.setScreen(new EcranCustomLevel(game));
            }
        });

        btnOption.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new EcranOptions(game));
            }
        });

        btnQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Fermeture du jeu...");
                Gdx.app.exit();
            }
        });

        // --- PLACEMENT DANS LA TABLE ---
        // On met tout le monde à la même taille de police
        btnPlay.getLabel().setFontScale(1.5f);
        btnCustomLevel.getLabel().setFontScale(1.5f);
        btnOption.getLabel().setFontScale(1.5f);
        btnQuit.getLabel().setFontScale(1.5f);

        // On les empile les uns sur les autres avec un petit espace (padBottom)
        table.add(btnPlay).width(320).height(120).padTop(250).padBottom(30).row();
        table.add(btnCustomLevel).width(620).height(120).padBottom(30).row();
        table.add(btnOption).width(380).height(120).padBottom(30).row();
        table.add(btnQuit).width(380).height(120);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        batch.begin();
        background.sprite.draw(batch);
        title.sprite.draw(batch);
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
