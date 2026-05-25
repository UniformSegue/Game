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

import java.util.Objects;

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

    // NOUVEAU : On garde en mémoire si on est en mode "Custom" ou "Normal"
    private boolean isCustomLevel = false;

    // NOUVEAU : La classe devient "public" pour pouvoir être utilisée dans EcranCustomLevel
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

    // --- CONSTRUCTEUR 1 : Appelé par le MenuScreen (Mode Normal) ---
    public EcranOptionsGame(Game game) {

        this(game, null, null);
    }

    // --- CONSTRUCTEUR 2 : Appelé par EcranCustomLevel (Mode Custom) ---
    public EcranOptionsGame(Game game, String dossierCustomChemin, Array<MusiqueItem> musiquesCustom) {
        this.game = game;

        // Si le dossier n'est pas null, on sait qu'on est en mode custom !
        if (dossierCustomChemin != null) {
            this.isCustomLevel = true;
        }

        background = new Background(0,0, GAME_SPEC.width,GAME_SPEC.height, Texture_File.BACKGROUND);
        batch = new SpriteBatch();

        ensimLogo = new Sprite(Texture_File.ENSIM_LOGO);
        ensimLogo.setSize(534,134);
        ensimLogo.setPosition(1920-534,45);

        float zoom = 1.5f;
        stage = new Stage(new FitViewport(1920 / zoom, 1080 / zoom));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/pixthulhu-ui.json"));
        skin.getFont("font").getData().markupEnabled = true;
        skin.getFont("subtitle").getData().markupEnabled = true;
        skin.getFont("title").getData().markupEnabled = true;

        Table tableRetour = new Table();
        tableRetour.setFillParent(true);
        tableRetour.top().left();

        TextButton btnRetour = new TextButton("Retour", skin);
        btnRetour.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Si on était en mode custom, on retourne au menu custom, sinon au menu normal
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

        final TextButton btnFacile = new TextButton("Facile", skin);
        final TextButton btnMoyen = new TextButton("Moyen", skin);
        final TextButton btnDifficile = new TextButton("Difficile", skin);

        TextButton.TextButtonStyle styleButtonBase = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        TextButton.TextButtonStyle styleButtonClique = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        styleButtonBase.fontColor = Color.WHITE;
        styleButtonClique.fontColor = Color.RED;
        btnMoyen.setStyle(styleButtonClique);

        ClickListener difficulteListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnFacile.setStyle(styleButtonBase);
                btnMoyen.setStyle(styleButtonBase);
                btnDifficile.setStyle(styleButtonBase);

                TextButton boutonClique = (TextButton) event.getListenerActor();
                boutonClique.setStyle(styleButtonClique);
                difficulte = boutonClique.getText().toString();
            }
        };

        btnFacile.addListener(difficulteListener);
        btnMoyen.addListener(difficulteListener);
        btnDifficile.addListener(difficulteListener);

        SelectBox<MusiqueItem> menuMusique = new SelectBox<>(skin);

        // --- NOUVEAU : Sélection de la source des musiques ---
        Array<MusiqueItem> listeMusiques;
        if (isCustomLevel) {
            listeMusiques = musiquesCustom; // On utilise la liste fournie par l'explorateur
        } else {
            listeMusiques = lireFichierJsonMusic(); // On utilise la liste de base
        }

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
                boutonPlayClicked();
            }
        });

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

    private int getVitesseDepuisJson(String nomFichier, String niveauDifficulte) {
        try {
            JsonReader reader = new JsonReader();
            // NOUVEAU : On gère les chemins absolus pour les niveaux custom !
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
        if (niveauDifficulte.equals("Facile")) return 10;
        if (niveauDifficulte.equals("Difficile")) return 20;
        return 15;
    }

    private void boutonPlayClicked() {
        int vitesse = getVitesseDepuisJson(musiqueChoisie, difficulte);
        int vitesse_turbo = vitesse + 5;

        // ATTENTION : J'ai ajouté isCustomLevel à la fin.
        // Tu devras modifier le constructeur de LevelScreen pour qu'il l'accepte !
        // game.setScreen(new LevelScreen(game, vitesse, vitesse_turbo, musiqueChoisie, 6, souffleActive, isCustomLevel));
        int nombre_lane = 6;
        if (difficulte.equals("Facile")) {nombre_lane = 4;}
        // En attendant que tu modifies LevelScreen, je laisse ton ancienne ligne :
        if(this.isCustomLevel){
            game.setScreen(new LevelScreen(game, vitesse, vitesse_turbo, musiqueChoisie, nombre_lane, souffleActive));
        }else{
            game.setScreen(new LevelScreen(game, vitesse, vitesse_turbo, "music\\"+musiqueChoisie, nombre_lane, souffleActive));

        }


        dispose();
    }

    @Override public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        batch.begin(); background.sprite.draw(batch); ensimLogo.draw(batch); batch.end();
        stage.act(delta); stage.draw();
    }
    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
    @Override public void show() {} @Override public void pause() {} @Override public void resume() {} @Override public void hide() {}
}
