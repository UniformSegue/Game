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

/**
 * Interface superposée (Overlay) représentant le menu de pause.
 * Assombrit l'écran de jeu et fournit les contrôles pour reprendre,
 * recommencer ou quitter le niveau en cours.
 */
public class PauseOverlay {

    private final Stage stage;
    private final Skin skin;
    private final Texture textureFond; // Ajouté pour gérer correctement la mémoire de la texture générée

    public PauseOverlay(final Game game, final LevelScreen levelScreen, Skin skin,
                        final float vitesse, final float vitesse_turbo,
                        final String level_data, final int nombre_lane, final boolean souffle) {

        this.skin = skin;
        this.stage = new Stage(new FitViewport(GAME_SPEC.width, GAME_SPEC.height));

        // Génération d'un fond semi-transparent pour assombrir l'écran de jeu
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.7f));
        pixmap.fill();

        this.textureFond = new Texture(pixmap);
        pixmap.dispose();

        Image fondAssombri = new Image(textureFond);
        fondAssombri.setFillParent(true);
        stage.addActor(fondAssombri);

        // Structure de mise en page pour centrer les boutons
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Déclaration des boutons
        TextButton btnReprendre = new TextButton("Reprendre", skin);
        TextButton btnRecommencer = new TextButton("Recommencer", skin);
        TextButton btnQuitter = new TextButton("Quitter", skin);

        // --- GESTION DES ÉVÉNEMENTS ---

        btnReprendre.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                levelScreen.togglePause();
            }
        });

        btnRecommencer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelScreen(game, vitesse, vitesse_turbo, level_data, nombre_lane, souffle));
                levelScreen.dispose();
            }
        });

        btnQuitter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                levelScreen.dispose();
            }
        });

        // Agencement vertical des boutons
        table.add(btnReprendre).width(300).height(100).padBottom(20).row();
        table.add(btnRecommencer).width(400).height(100).padBottom(20).row();
        table.add(btnQuitter).width(300).height(100);
    }

    /**
     * Met à jour et dessine l'interface de pause.
     * * @param delta Le temps écoulé (en secondes) depuis le dernier rendu.
     */
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * Libère les ressources mémoire utilisées par l'overlay.
     */
    public void dispose() {
        stage.dispose();
        if (textureFond != null) {
            textureFond.dispose();
        }
    }
}
