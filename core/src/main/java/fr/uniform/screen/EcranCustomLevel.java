package fr.uniform.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences; // <-- NOUVEAU
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.uniform.GAME_SPEC;
import fr.uniform.Texture_File;
import fr.uniform.object.menu.Background;

import javax.swing.JFileChooser;
import java.io.File;

public class EcranCustomLevel implements Screen {

    private final Game game;
    private final Stage stage;
    private final Skin skin;
    private Background background;
    private SpriteBatch batch;
    private Sprite ensimLogo;

    private Label labelChemin;
    private SelectBox<EcranOptionsGame.MusiqueItem> menuMusique;
    private String musiqueChoisie = "";
    private String dossierCustomChemin = "";

    private Array<EcranOptionsGame.MusiqueItem> listeMusiquesCustom = new Array<>();

    // --- NOUVEAU : L'outil de sauvegarde ---
    private Preferences prefs;

    public EcranCustomLevel(Game game) {
        this.game = game;
        batch = new SpriteBatch();

        // Initialisation de la sauvegarde (on utilise le même fichier que pour tes options)
        prefs = Gdx.app.getPreferences("UniformGameOptions");

        background = new Background(0, 0, GAME_SPEC.width, GAME_SPEC.height, Texture_File.BACKGROUND);
        ensimLogo = new Sprite(Texture_File.ENSIM_LOGO);
        ensimLogo.setSize(534, 134);
        ensimLogo.setPosition(1920 - 534, 45);

        stage = new Stage(new FitViewport(GAME_SPEC.width, GAME_SPEC.height));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/pixthulhu-ui.json"));
        skin.getFont("font").getData().markupEnabled = true;
        skin.getFont("title").getData().markupEnabled = true;

        Table tableRetour = new Table();
        tableRetour.setFillParent(true);
        tableRetour.top().left();
        TextButton btnRetour = new TextButton("Retour", skin);
        btnRetour.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
        tableRetour.add(btnRetour).pad(20).width(320).height(120);
        stage.addActor(tableRetour);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titre = new Label("[WHITE]NIVEAUX PERSONNALISES[]", skin, "title");
        labelChemin = new Label("[WHITE]Aucun dossier selectionne[]", skin);

        TextButton btnOuvrirDossier = new TextButton("Choisir un dossier", skin);
        menuMusique = new SelectBox<>(skin);
        TextButton btnPlay = new TextButton("JOUER", skin);

        btnOuvrirDossier.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ouvrirExplorateurFichiers();
            }
        });

        menuMusique.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (menuMusique.getSelected() != null) {
                    musiqueChoisie = menuMusique.getSelected().nomFichier;
                }
            }
        });

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!musiqueChoisie.isEmpty() && dossierCustomChemin != null && !dossierCustomChemin.isEmpty()) {
                    System.out.println("Envoi vers Options Custom avec le dossier : " + dossierCustomChemin);
                    game.setScreen(new EcranOptionsGame(game, dossierCustomChemin, listeMusiquesCustom));
                    dispose();
                } else {
                    labelChemin.setText("[RED]Veuillez d'abord choisir un dossier contenant un niveau ![]");
                }
            }
        });

        table.add(titre).padBottom(50).row();
        table.add(btnOuvrirDossier).width(700).height(120).padBottom(20).row();
        table.add(labelChemin).padBottom(40).row();

        menuMusique.getStyle().font.getData().setScale(1.5f);
        table.add(menuMusique).width(600).padBottom(60).row();

        table.add(btnPlay).width(300).height(100);

        // --- NOUVEAU : Chargement automatique au démarrage ---
        // On vérifie si un chemin avait été sauvegardé précédemment
        String cheminSauvegarde = prefs.getString("lastCustomDirectory", "");
        if (!cheminSauvegarde.isEmpty()) {
            // Si oui, on fait comme si le joueur venait de le choisir dans l'explorateur !
            chargerDossierCustom(cheminSauvegarde);
        }
    }

    private void ouvrirExplorateurFichiers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Sélectionnez le dossier contenant music.json");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                // --- PETIT BONUS ---
                // Si on a déjà un chemin sauvegardé, on ouvre l'explorateur directement dans ce dossier !
                String cheminSauvegarde = prefs.getString("lastCustomDirectory", "");
                if (!cheminSauvegarde.isEmpty()) {
                    chooser.setCurrentDirectory(new File(cheminSauvegarde));
                }

                int resultat = chooser.showOpenDialog(null);

                if (resultat == JFileChooser.APPROVE_OPTION) {
                    final File dossierSelect = chooser.getSelectedFile();

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            chargerDossierCustom(dossierSelect.getAbsolutePath());
                        }
                    });
                }
            }
        }).start();
    }

    private void chargerDossierCustom(String cheminDossier) {
        dossierCustomChemin = cheminDossier;
        labelChemin.setText("[WHITE]Dossier : " + cheminDossier + "[]");

        FileHandle fichierMusicJson = Gdx.files.absolute(cheminDossier + "/music.json");
        listeMusiquesCustom.clear();

        if (fichierMusicJson.exists()) {
            try {
                JsonReader reader = new JsonReader();
                JsonValue jsonBase = reader.parse(fichierMusicJson);

                JsonValue noms = jsonBase.get("musicname");
                JsonValue fichiers = jsonBase.get("musicfile");

                if (noms != null && fichiers != null) {
                    for (int i = 0; i < noms.size; i++) {
                        String nom = noms.getString(i);
                        String cheminCompletDuNiveau = cheminDossier + "/" + fichiers.getString(i);
                        listeMusiquesCustom.add(new EcranOptionsGame.MusiqueItem(nom, cheminCompletDuNiveau));
                    }
                }


                // Si tout s'est bien passé et que le dossier est valide, on le sauvegarde pour la prochaine fois !
                prefs.putString("lastCustomDirectory", cheminDossier);
                prefs.flush(); // On enregistre sur le disque dur

            } catch (Exception e) {
                System.err.println("Erreur de lecture du music.json custom !");
                listeMusiquesCustom.add(new EcranOptionsGame.MusiqueItem("Erreur JSON", ""));
            }
        } else {
            labelChemin.setText("[RED]Erreur : Aucun music.json trouvé dans ce dossier ![]");
        }

        menuMusique.setItems(listeMusiquesCustom);

        if (listeMusiquesCustom.size > 0 && !listeMusiquesCustom.first().nomFichier.isEmpty()) {
            musiqueChoisie = listeMusiquesCustom.first().nomFichier;
        } else {
            musiqueChoisie = "";
        }
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
