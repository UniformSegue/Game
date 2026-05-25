package fr.uniform.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.uniform.GAME_SPEC;
import fr.uniform.Texture_File;
import fr.uniform.object.menu.Background;

public class EcranFinNiveau implements Screen {

    private final Game game;
    private final Stage stage;
    private final Skin skin;
    private Background background;
    private SpriteBatch batch;

    private int vitesse;
    private int vitesse_turbo;
    private String musiqueChoisie;
    private int nombre_lane;
    private boolean souffleActive;

    // --- Les textures des médailles ---
    private Texture texOr;
    private Texture texArgent;
    private Texture texBronze;

    public EcranFinNiveau(Game game, int maxComboJoueur, int vitesse, int vitesse_turbo, String musiqueChoisie, int nombre_lane, boolean souffleActive) {
        this.game = game;

        this.vitesse = vitesse;
        this.vitesse_turbo = vitesse_turbo;
        this.musiqueChoisie = musiqueChoisie;
        this.nombre_lane = nombre_lane;
        this.souffleActive = souffleActive;

        batch = new SpriteBatch();
        background = new Background(0, 0, GAME_SPEC.width, GAME_SPEC.height, Texture_File.BACKGROUND);

        stage = new Stage(new FitViewport(GAME_SPEC.width, GAME_SPEC.height));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/pixthulhu-ui.json"));
        skin.getFont("font").getData().markupEnabled = true;
        skin.getFont("title").getData().markupEnabled = true;
        skin.getFont("subtitle").getData().markupEnabled = true;

        // Chargement des images (assure-toi qu'elles sont bien dans ton dossier assets)
        texOr = new Texture(Gdx.files.internal("or.png"));
        texArgent = new Texture(Gdx.files.internal("argent.png"));
        texBronze = new Texture(Gdx.files.internal("bronze.png"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // --- CALCUL DU MAX COMBO ---
        int totalNotes = getTotalNotesFromJson(musiqueChoisie);

        // On calcule le combo max (Total des notes - 1)
        int maxComboPossible = Math.max(0, totalNotes - 1);
        // Le combo ne peut pas dépasser 99
        maxComboPossible = Math.min(99, maxComboPossible);

        System.out.println("Combo maximum possible : " + maxComboPossible + " | Combo du joueur : " + maxComboJoueur);

        // --- CHOIX DE LA MÉDAILLE ---
        Texture textureMedailleGagnee = null;
        String texteSiPasDeMedaille = "";

        if (maxComboPossible > 0) {
            if (maxComboJoueur >= maxComboPossible) {
                textureMedailleGagnee = texOr;
            } else if (maxComboJoueur >= maxComboPossible / 2) {
                textureMedailleGagnee = texArgent;
            } else if (maxComboJoueur >= maxComboPossible / 3) {
                textureMedailleGagnee = texBronze;
            } else {
                texteSiPasDeMedaille = "[RED]NON CLASSE[]";
            }
        } else {
            texteSiPasDeMedaille = "[RED]Erreur niveau[]";
        }

        // --- CRÉATION DES ÉLÉMENTS VISUELS ---
        Label titre = new Label("[WHITE]NIVEAU TERMINE ![]", skin, "title");

        Label statsCombo = new Label("[WHITE]Meilleur Combo : " + maxComboJoueur + "[]", skin);
        statsCombo.setFontScale(1.5f);

        // Sous-table pour aligner le texte "Médaille" avec l'image
        Table tableMedaille = new Table();
        Label labelMedaille = new Label("[WHITE]Medaille : []", skin);
        labelMedaille.setFontScale(2f);
        tableMedaille.add(labelMedaille).padRight(20);

        if (textureMedailleGagnee != null) {
            Image imgMedaille = new Image(textureMedailleGagnee);
            tableMedaille.add(imgMedaille).width(128).height(128);
        } else {
            Label pasDeMedaille = new Label(texteSiPasDeMedaille, skin);
            pasDeMedaille.setFontScale(2f);
            tableMedaille.add(pasDeMedaille);
        }

        // --- CRÉATION DES BOUTONS ---
        TextButton btnRejouer = new TextButton("REJOUER", skin);
        TextButton btnMenu = new TextButton("MENU PRINCIPAL", skin);

        btnRejouer.getLabel().setFontScale(1.5f);
        btnMenu.getLabel().setFontScale(1.5f);

        btnRejouer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelScreen(game, EcranFinNiveau.this.vitesse, EcranFinNiveau.this.vitesse_turbo, EcranFinNiveau.this.musiqueChoisie, EcranFinNiveau.this.nombre_lane, EcranFinNiveau.this.souffleActive));
                dispose();
            }
        });

        btnMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        // --- PLACEMENT DANS LA TABLE PRINCIPALE ---
        table.add(titre).padBottom(60).colspan(2).row();
        table.add(statsCombo).padBottom(20).colspan(2).row();

        table.add(tableMedaille).padBottom(80).colspan(2).row();

        table.add(btnRejouer).width(430).height(150).padRight(50);
        table.add(btnMenu).width(700).height(150);
    }

    /**
     * Lit le JSON et compte uniquement les notes qui seront réellement jouées.
     */
    private int getTotalNotesFromJson(String cheminFichier) {
        try {
            FileHandle file = Gdx.files.absolute(cheminFichier);

            if (!file.exists()) {
                file = Gdx.files.internal(cheminFichier);
            }

            if (file.exists()) {
                JsonReader reader = new JsonReader();
                JsonValue jsonBase = reader.parse(file);
                JsonValue notes = jsonBase.get("notes");

                if (notes != null && notes.isArray()) {
                    int notesValides = 0;

                    // On parcourt chaque note du fichier JSON
                    for (int i = 0; i < notes.size; i++) {
                        JsonValue note = notes.get(i);
                        int lane = note.getInt("lane");

                        // --- NOUVEAU ---
                        // Si on est en mode "Facile" (4 lanes), on ignore les notes des lanes 0 et 5 !
                        if (nombre_lane == 4 && (lane == 0 || lane == 5)) {
                            continue; // On passe à la note suivante sans l'ajouter au compteur
                        }

                        notesValides++; // On compte la note
                    }
                    return notesValides;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur de lecture du total des notes : " + e.getMessage());
        }
        return 0;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        batch.begin();
        background.sprite.draw(batch);
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

        if (texOr != null) texOr.dispose();
        if (texArgent != null) texArgent.dispose();
        if (texBronze != null) texBronze.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
