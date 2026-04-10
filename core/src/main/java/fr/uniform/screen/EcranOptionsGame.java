package fr.uniform.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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

    // Variables pour stocker les choix du joueur
    private boolean souffleActive = false;
    private String difficulte = "Moyen"; // Valeur par défaut
    private String musiqueChoisie = "";  // Contiendra le nom du fichier (ex: music1.json)

    // Petite classe pour lier le nom affiché au fichier réel
    private static class MusiqueItem {
        public String nomAffichage;
        public String nomFichier;

        public MusiqueItem(String nomAffichage, String nomFichier) {
            this.nomAffichage = nomAffichage;
            this.nomFichier = nomFichier;
        }

        // C'est cette méthode que la SelectBox utilise pour afficher le texte
        @Override
        public String toString() {
            return nomAffichage;
        }
    }

    public EcranOptionsGame(Game game) {
        this.game = game;
        //Background
        background = new Background(0,0, GAME_SPEC.width,GAME_SPEC.height, Texture_File.BACKGROUND);
        batch = new SpriteBatch();

        //Logo ENSIM
        ensimLogo = new Sprite(Texture_File.ENSIM_LOGO);
        int width_ensim = 534;
        int height_ensim = 134;
        ensimLogo.setSize(width_ensim,height_ensim);
        ensimLogo.setPosition(1920-width_ensim,45);


        //Initialisation de la scène avec le ZOOM (on divise par 1.5 pour grossir les éléments)
        float zoom = 1.5f;
        stage = new Stage(new FitViewport(1920 / zoom, 1080 / zoom));
        Gdx.input.setInputProcessor(stage);

        //Chargement du style
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
                // On retourne au MenuScreen
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        // On ajoute le bouton avec un peu de marge (padding) pour ne pas être collé au bord
        tableRetour.add(btnRetour).pad(20).width(220).height(100);
        stage.addActor(tableRetour);
        // ---------------------------------------------------------


        //Création de la table principale (centrée)
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // --- CRÉATION DES WIDGETS ---

        //La case à cocher pour le souffle
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

        // --- Les 3 boutons de difficulté ---
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
                System.out.println("Difficulté sélectionnée : " + difficulte);
            }
        };

        btnFacile.addListener(difficulteListener);
        btnMoyen.addListener(difficulteListener);
        btnDifficile.addListener(difficulteListener);

        //Le menu déroulant des musiques
        SelectBox<MusiqueItem> menuMusique = new SelectBox<>(skin);
        Array<MusiqueItem> listeMusiques = lireFichierJsonMusic();
        menuMusique.setItems(listeMusiques);

        if (listeMusiques.size > 0) {
            musiqueChoisie = listeMusiques.first().nomFichier;
        }

        menuMusique.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MusiqueItem selection = menuMusique.getSelected();
                musiqueChoisie = selection.nomFichier;
                System.out.println("Musique stockée : " + musiqueChoisie);
            }
        });

        //Le bouton JOUER à la fin
        TextButton btnPlay = new TextButton("JOUER", skin);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boutonPlayClicked();
            }
        });

        // Label Style
        Label.LabelStyle textWhite = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        textWhite.fontColor = Color.WHITE;

        // --- PLACEMENT DANS LA TABLE CENTRÉE ---
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


        // Le bouton JOUER en bas
        table.add(btnPlay).colspan(3).padTop(50).width(300).height(80);
    }

    private Array<MusiqueItem> lireFichierJsonMusic() {
        Array<MusiqueItem> liste = new Array<>();
        try {
            JsonReader reader = new JsonReader();
            JsonValue jsonBase = reader.parse(Gdx.files.internal("music.json"));

            JsonValue noms = jsonBase.get("musicname");
            JsonValue fichiers = jsonBase.get("musicfile");

            if (noms != null && fichiers != null) {
                for (int i = 0; i < noms.size; i++) {
                    liste.add(new MusiqueItem(
                        noms.getString(i),
                        fichiers.getString(i)
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur avec le fichier music.json");
            liste.add(new MusiqueItem("Erreur", "erreur.json"));
        }
        return liste;
    }

    private void boutonPlayClicked() {
        System.out.println("--- LANCEMENT DU JEU ---");
        System.out.println("Souffle activé : " + souffleActive);
        System.out.println("Difficulté : " + difficulte);
        System.out.println("Fichier Musique : " + musiqueChoisie);
        int vitesse = 10;
        int vitesse_turbo = 10;
        //Temporaire difficulter
        if (Objects.equals(difficulte, "Facile")) {vitesse=10;}
        if (Objects.equals(difficulte, "Moyen")) {vitesse=15;}
        if (Objects.equals(difficulte, "Difficile")) {vitesse=20;}
        game.setScreen(new LevelScreen(game, vitesse,vitesse_turbo,musiqueChoisie,6,souffleActive));

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
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
