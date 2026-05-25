package fr.uniform.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
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

/**
 * Écran de préparation avant le lancement d'un niveau.
 * Permet au joueur de choisir la piste musicale, la difficulté, et d'activer/désactiver le contrôle au souffle.
 * Gère à la fois les niveaux intégrés (Mode Normal) et les niveaux importés (Mode Custom).
 */
public class EcranOptionsGame implements Screen {

    private final Game game;
    private final Stage stage;
    private final Skin skin;
    private Background background;
    private SpriteBatch batch;
    private Sprite ensimLogo;

    private boolean souffleActive = false;
    private String difficulte = "Moyen";
    private String musiqueChoisie = "";

    private boolean isCustomLevel = false;

    /**
     * Classe utilitaire pour formater l'affichage des musiques dans le menu déroulant (SelectBox).
     */
    public static class MusiqueItem {
        public String nomAffichage;
        public String nomFichier;

        public MusiqueItem(String nomAffichage, String nomFichier) {
            this.nomAffichage = nomAffichage;
            this.nomFichier = nomFichier;
        }

        @Override
        public String toString() {
            return nomAffichage;
        }
    }

    /**
     * Constructeur pour le Mode Normal (Niveaux intégrés).
     */
    public EcranOptionsGame(Game game) {
        this(game, null, null);
    }

    /**
     * Constructeur principal gérant le Mode Normal et le Mode Custom.
     * * @param dossierCustomChemin Chemin du dossier personnalisé (null si mode normal).
     * @param musiquesCustom Liste des musiques personnalisées (null si mode normal).
     */
    public EcranOptionsGame(Game game, String dossierCustomChemin, Array<MusiqueItem> musiquesCustom) {
        this.game = game;

        if (dossierCustomChemin != null) {
            this.isCustomLevel = true;
        }

        this.background = new Background(0, 0, GAME_SPEC.width, GAME_SPEC.height, Texture_File.BACKGROUND);
        this.batch = new SpriteBatch();

        this.ensimLogo = new Sprite(Texture_File.ENSIM_LOGO);
        this.ensimLogo.setSize(534, 134);
        this.ensimLogo.setPosition(1920 - 534, 45);

        float zoom = 1.5f;
        this.stage = new Stage(new FitViewport(1920 / zoom, 1080 / zoom));
        Gdx.input.setInputProcessor(stage);

        this.skin = new Skin(Gdx.files.internal("ui/pixthulhu-ui.json"));
        this.skin.getFont("font").getData().markupEnabled = true;
        this.skin.getFont("subtitle").getData().markupEnabled = true;
        this.skin.getFont("title").getData().markupEnabled = true;

        // Bouton de retour contextuel (Menu Principal ou Menu Custom)
        Table tableRetour = new Table();
        tableRetour.setFillParent(true);
        tableRetour.top().left();

        TextButton btnRetour = new TextButton("Retour", skin);
        btnRetour.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isCustomLevel) {
                    game.setScreen(new EcranCustomLevel(game));
                } else {
                    game.setScreen(new MenuScreen(game));
                }
                dispose();
            }
        });

        tableRetour.add(btnRetour).pad(20).width(220).height(100);
        stage.addActor(tableRetour);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Configuration de la CheckBox pour le contrôle au souffle
        final CheckBox checkSouffle = new CheckBox("Activer le souffle", skin);
        CheckBox.CheckBoxStyle styleWhiteCheckBox = new CheckBox.CheckBoxStyle(skin.get(CheckBox.CheckBoxStyle.class));
        styleWhiteCheckBox.fontColor = Color.WHITE;
        checkSouffle.setStyle(styleWhiteCheckBox);

        checkSouffle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                souffleActive = checkSouffle.isChecked();
            }
        });

        // Configuration des boutons de difficulté
        final TextButton btnFacile = new TextButton("Facile", skin);
        final TextButton btnMoyen = new TextButton("Moyen", skin);
        final TextButton btnDifficile = new TextButton("Difficile", skin);

        TextButton.TextButtonStyle styleButtonBase = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        TextButton.TextButtonStyle styleButtonClique = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        styleButtonBase.fontColor = Color.WHITE;
        styleButtonClique.fontColor = Color.RED;
        btnMoyen.setStyle(styleButtonClique); // Moyen sélectionné par défaut

        ClickListener difficulteListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Réinitialisation visuelle des boutons
                btnFacile.setStyle(styleButtonBase);
                btnMoyen.setStyle(styleButtonBase);
                btnDifficile.setStyle(styleButtonBase);

                // Application du style actif sur le bouton cliqué
                TextButton boutonClique = (TextButton) event.getListenerActor();
                boutonClique.setStyle(styleButtonClique);
                difficulte = boutonClique.getText().toString();
            }
        };

        btnFacile.addListener(difficulteListener);
        btnMoyen.addListener(difficulteListener);
        btnDifficile.addListener(difficulteListener);

        // Configuration du menu de sélection musical
        SelectBox<MusiqueItem> menuMusique = new SelectBox<>(skin);

        Array<MusiqueItem> listeMusiques = isCustomLevel ? musiquesCustom : lireFichierJsonMusic();
        menuMusique.setItems(listeMusiques);

        if (listeMusiques.size > 0) {
            musiqueChoisie = listeMusiques.first().nomFichier;
        }

        menuMusique.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MusiqueItem selection = menuMusique.getSelected();
                musiqueChoisie = selection.nomFichier;
            }
        });

        TextButton btnPlay = new TextButton("JOUER", skin);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                lancerNiveau();
            }
        });

        // Assemblage de l'interface
        Label.LabelStyle textWhite = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        textWhite.fontColor = Color.WHITE;

        Label musique = new Label("Choix de la musique :", skin);
        musique.setStyle(textWhite);
        musique.setFontScale(2f);

        table.add(musique).colspan(3).padTop(10).padBottom(10).row();
        menuMusique.setScale(2f);
        table.add(menuMusique).colspan(3).width(600).padBottom(10).row();

        Label difficulteLabel = new Label("Difficulte :", skin);
        difficulteLabel.setStyle(textWhite);
        difficulteLabel.setFontScale(2f);

        table.add(difficulteLabel).colspan(3).padBottom(0).row();
        table.add(btnFacile).pad(40).width(250).height(100);
        table.add(btnMoyen).pad(40).width(250).height(100);
        table.add(btnDifficile).pad(40).width(250).height(100).row();

        table.add(checkSouffle).colspan(3).padBottom(30).row();
        table.add(btnPlay).colspan(3).padTop(50).width(300).height(80);
    }

    /**
     * Lit la liste des musiques intégrées au jeu depuis les assets internes.
     * * @return Un tableau d'items musicaux formatés pour l'interface.
     */
    private Array<MusiqueItem> lireFichierJsonMusic() {
        Array<MusiqueItem> liste = new Array<>();
        try {
            JsonReader reader = new JsonReader();
            JsonValue jsonBase = reader.parse(Gdx.files.internal("music/music.json"));

            JsonValue noms = jsonBase.get("musicname");
            JsonValue fichiers = jsonBase.get("musicfile");

            if (noms != null && fichiers != null) {
                for (int i = 0; i < noms.size; i++) {
                    liste.add(new MusiqueItem(noms.getString(i), fichiers.getString(i)));
                }
            }
        } catch (Exception e) {
            liste.add(new MusiqueItem("Erreur", "erreur.json"));
        }
        return liste;
    }

    /**
     * Récupère la vitesse de défilement (scroll speed) associée à la difficulté choisie
     * directement dans le fichier JSON de la beatmap.
     * * @param nomFichier Chemin du fichier JSON du niveau.
     * @param niveauDifficulte "Facile", "Moyen" ou "Difficile".
     * @return La vitesse entière correspondante (valeur par défaut en cas d'erreur).
     */
    private int getVitesseDepuisJson(String nomFichier, String niveauDifficulte) {
        try {
            JsonReader reader = new JsonReader();
            FileHandle fichierJson = isCustomLevel ? Gdx.files.absolute(nomFichier) : Gdx.files.internal(nomFichier);

            JsonValue jsonBase = reader.parse(fichierJson);
            JsonValue tabDifficulte = jsonBase.get("difficulty");

            if (tabDifficulte != null && tabDifficulte.isArray() && tabDifficulte.size >= 3) {
                if (niveauDifficulte.equals("Facile")) return tabDifficulte.getInt(0);
                if (niveauDifficulte.equals("Moyen")) return tabDifficulte.getInt(1);
                if (niveauDifficulte.equals("Difficile")) return tabDifficulte.getInt(2);
            }
        } catch (Exception e) {
            System.err.println("Erreur de lecture de la difficulté dans le fichier : " + nomFichier);
        }

        // Valeurs de secours si la lecture du JSON échoue
        if (niveauDifficulte.equals("Facile")) return 10;
        if (niveauDifficulte.equals("Difficile")) return 20;
        return 15;
    }

    /**
     * Calcule les paramètres finaux et initialise l'écran de jeu (LevelScreen).
     */
    private void lancerNiveau() {
        int vitesse = getVitesseDepuisJson(musiqueChoisie, difficulte);
        int vitesse_turbo = vitesse + 5;

        // Adaptation du nombre de pistes selon la difficulté
        int nombre_lane = difficulte.equals("Facile") ? 4 : 6;

        if (this.isCustomLevel) {
            game.setScreen(new LevelScreen(game, vitesse, vitesse_turbo, musiqueChoisie, nombre_lane, souffleActive));
        } else {
            // Utilisation d'un slash / pour garantir la compatibilité multi-plateforme dans LibGDX
            game.setScreen(new LevelScreen(game, vitesse, vitesse_turbo, "music/" + musiqueChoisie, nombre_lane, souffleActive));
        }

        dispose();
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
