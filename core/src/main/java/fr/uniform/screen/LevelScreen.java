package fr.uniform.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.uniform.GAME_SPEC;
import fr.uniform.GameEnvironnement;
import fr.uniform.Texture_File;
import fr.uniform.object.game.*;
import fr.uniform.object.menu.Background;
import fr.uniform.object.menu.Ocarina;
import fr.uniform.utils.ComboController;
import fr.uniform.utils.InputController;
import fr.uniform.utils.MusicController;

import java.util.ArrayList;
import java.util.List;

/**
 * Écran principal de jeu.
 * Gère la boucle de gameplay : défilement des notes, synchronisation musicale,
 * détection des inputs, calcul du combo et affichage de l'environnement.
 */
public class LevelScreen implements Screen {
    private final Game game;

    // Gestion de la musique et de l'environnement
    private Music music;
    private MusicController musicController;
    private GameEnvironnement gameEnvironnement;
    private float vitesse;
    private float vitesse_turbo;
    private int nombre_lane;
    private boolean souffle;
    private String level_data;

    // Rendu visuel
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private float screenHeight, screenWidth;
    private Background background;
    private Ocarina ocarina;
    private Skin skin;

    // Éléments de jeu (Pistes, Boutons, Indicateurs)
    private ErrorIndiquator errorIndiquator;
    private PasseIndiquator passeIndiquator;
    private ComboController comboController;
    private List<Button> buttons;
    private Button button_f, button_d, button_g, button_h, button_j, button_k, button_souffle;
    private List<Lane> lanes;
    private Lane lane_f, lane_d, lane_g, lane_h, lane_j, lane_k;

    // Contrôleurs et UI
    private InputController inputController;
    private PauseOverlay pauseMenu;
    private boolean pause;
    private Label debugLabel;

    // État de fin de niveau
    private boolean isLevelFinished = false;
    private int maxComboJoueur = 0;

    // Compte à rebours initial
    private boolean isCountingDown = true;
    private float countdownTimer = 3.0f;
    private Stage hudStage;
    private Label countdownLabel;

    public LevelScreen(Game game, float vitesse, float vitesse_turbo, String level_data, int nombre_lane, boolean souffle) {
        this.game = game;
        this.vitesse = vitesse;
        this.vitesse_turbo = vitesse_turbo;
        this.nombre_lane = nombre_lane;
        this.souffle = souffle;
        this.level_data = level_data;
        this.pause = false;

        this.skin = new Skin(Gdx.files.internal("ui/pixthulhu-ui.json"));
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

        initLanesAndButtons();

        // Initialisation de la logique musicale via le fichier JSON
        this.musicController = new MusicController(lanes, gameEnvironnement);
        this.musicController.loadMusicData(level_data);

        String audioFileName = musicController.getAudioFileName();
        if (audioFileName == null) {
            throw new RuntimeException("Erreur : Impossible de récupérer le nom de la musique depuis le JSON.");
        }

        // Détermination de la source du fichier audio (Niveau de base vs Niveau Custom)
        FileHandle levelFileAbsolu = Gdx.files.absolute(level_data);
        FileHandle musicFileHandle;

        if (levelFileAbsolu.exists()) {
            // Niveau personnalisé (chargement depuis le système de fichiers local)
            musicFileHandle = levelFileAbsolu.parent().child(audioFileName);
        } else {
            // Niveau intégré (chargement depuis les assets internes du .jar)
            FileHandle levelFileInterne = Gdx.files.internal(level_data);
            musicFileHandle = levelFileInterne.parent().child(audioFileName);
        }

        this.music = Gdx.audio.newMusic(musicFileHandle);

        // Synchronisation des notes par rapport au début de la musique
        long currentMillis = (long) (music.getPosition() * 1000);
        musicController.generateNotes(currentMillis);

        // Application du volume sauvegardé
        Preferences prefs = Gdx.app.getPreferences("UniformGameOptions");
        music.setVolume(prefs.getFloat("musicVolume", 0.5f));

        music.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                isLevelFinished = true;
            }
        });

        // Mise en place du décor
        this.background = new Background(0, 0, GAME_SPEC.width, GAME_SPEC.height, Texture_File.BACKGROUND);
        this.ocarina = new Ocarina(300, -400, Texture_File.TEXTURE_OCARINA_WIDTH, Texture_File.TEXTURE_OCARINA_HEIGHT, Texture_File.OCARINA);

        this.pauseMenu = new PauseOverlay(game, this, skin, vitesse, vitesse_turbo, level_data, nombre_lane, souffle);
        this.inputController = new InputController(this, buttons, gameEnvironnement, lanes);

        // Affichage de debug (FPS & RAM)
        Label.LabelStyle debugStyle = new Label.LabelStyle(skin.getFont("font"), Color.YELLOW);
        this.debugLabel = new Label("Calcul de la RAM...", debugStyle);
        this.debugLabel.setPosition(1920 - 400, 1080 - 50);

        // Configuration de l'interface du compte à rebours
        this.hudStage = new Stage(new FitViewport(GAME_SPEC.width, GAME_SPEC.height));
        this.countdownLabel = new Label("3", skin, "title");
        this.countdownLabel.setFontScale(4f);
        this.countdownLabel.setSize(GAME_SPEC.width, GAME_SPEC.height);
        this.countdownLabel.setAlignment(Align.center);
        this.hudStage.addActor(countdownLabel);
    }

    /**
     * Initialise les pistes (lanes), les boutons de contrôle et l'environnement de jeu.
     */
    private void initLanesAndButtons() {
        comboController = new ComboController(100, 600);
        errorIndiquator = new ErrorIndiquator(1700, 600, Texture_File.ERROR_INDIQUATOR_WIDTH, Texture_File.ERROR_INDIQUATOR_HEIGHT, Texture_File.ERROR_INDIQUATOR);
        passeIndiquator = new PasseIndiquator(100, 400, Texture_File.PASS_INDIQUATOR_WIDTH, Texture_File.PASS_INDIQUATOR_HEIGHT, Texture_File.PASS_INDIQUATOR);

        gameEnvironnement = new GameEnvironnement(vitesse, vitesse_turbo, 230, errorIndiquator, passeIndiquator, comboController, false, souffle, nombre_lane);

        lanes = new ArrayList<>();
        // Décalage en X pour l'espacement des pistes
        lane_d = new Lane(430, gameEnvironnement.hauter_y_line, Texture_File.TEXTURE_LANE_WIDTH, Texture_File.TEXTURE_LANE_HEIGHT, button_d, Texture_File.LANE_DEFAULT, new ArrayList<>());
        lane_f = new Lane(630, gameEnvironnement.hauter_y_line, Texture_File.TEXTURE_LANE_WIDTH, Texture_File.TEXTURE_LANE_HEIGHT, button_f, Texture_File.LANE_DEFAULT, new ArrayList<>());
        lane_g = new Lane(830, gameEnvironnement.hauter_y_line, Texture_File.TEXTURE_LANE_WIDTH, Texture_File.TEXTURE_LANE_HEIGHT, button_g, Texture_File.LANE_DEFAULT, new ArrayList<>());
        lane_h = new Lane(1030, gameEnvironnement.hauter_y_line, Texture_File.TEXTURE_LANE_WIDTH, Texture_File.TEXTURE_LANE_HEIGHT, button_h, Texture_File.LANE_DEFAULT, new ArrayList<>());
        lane_j = new Lane(1230, gameEnvironnement.hauter_y_line, Texture_File.TEXTURE_LANE_WIDTH, Texture_File.TEXTURE_LANE_HEIGHT, button_j, Texture_File.LANE_DEFAULT, new ArrayList<>());
        lane_k = new Lane(1430, gameEnvironnement.hauter_y_line, Texture_File.TEXTURE_LANE_WIDTH, Texture_File.TEXTURE_LANE_HEIGHT, button_k, Texture_File.LANE_DEFAULT, new ArrayList<>());

        buttons = new ArrayList<>();

        // Placement dynamique des boutons en bas des pistes
        button_d = new Button(lane_d.x - ((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH) / 2), lane_d.y - Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.TEXTURE_BUTTON_WIDTH, Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.BUTTON_D_UNPRESS, Texture_File.BUTTON_D_PRESS);
        buttons.add(button_d);
        button_f = new Button(lane_f.x - ((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH) / 2), lane_f.y - Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.TEXTURE_BUTTON_WIDTH, Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.BUTTON_F_UNPRESS, Texture_File.BUTTON_F_PRESS);
        buttons.add(button_f);
        button_g = new Button(lane_g.x - ((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH) / 2), lane_g.y - Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.TEXTURE_BUTTON_WIDTH, Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.BUTTON_G_UNPRESS, Texture_File.BUTTON_G_PRESS);
        buttons.add(button_g);
        button_h = new Button(lane_h.x - ((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH) / 2), lane_h.y - Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.TEXTURE_BUTTON_WIDTH, Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.BUTTON_H_UNPRESS, Texture_File.BUTTON_H_PRESS);
        buttons.add(button_h);
        button_j = new Button(lane_j.x - ((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH) / 2), lane_j.y - Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.TEXTURE_BUTTON_WIDTH, Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.BUTTON_J_UNPRESS, Texture_File.BUTTON_J_PRESS);
        buttons.add(button_j);
        button_k = new Button(lane_k.x - ((Texture_File.TEXTURE_BUTTON_WIDTH - Texture_File.TEXTURE_LANE_WIDTH) / 2), lane_k.y - Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.TEXTURE_BUTTON_WIDTH, Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.BUTTON_K_UNPRESS, Texture_File.BUTTON_K_PRESS);
        buttons.add(button_k);

        button_souffle = new Button(2000, 2000, Texture_File.TEXTURE_BUTTON_WIDTH, Texture_File.TEXTURE_BUTTON_HEIGHT, Texture_File.BUTTON_K_UNPRESS, Texture_File.BUTTON_K_PRESS);
        buttons.add(button_souffle);

        gameEnvironnement.setButtons(buttons);

        // Association des boutons à leurs pistes respectives
        lane_d.assignedButton = button_d; lanes.add(lane_d);
        lane_f.assignedButton = button_f; lanes.add(lane_f);
        lane_g.assignedButton = button_g; lanes.add(lane_g);
        lane_h.assignedButton = button_h; lanes.add(lane_h);
        lane_j.assignedButton = button_j; lanes.add(lane_j);
        lane_k.assignedButton = button_k; lanes.add(lane_k);

        // Configuration "Facile" : On désactive les deux pistes extrêmes
        if (nombre_lane == 4) {
            lane_d.playabled(); // Désactive la lane (supposant que la méthode gère ce basculement)
            lane_k.playabled();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputController);
    }

    @Override
    public void render(float delta) {
        // 1. Vérification de la condition de victoire/fin
        if (isLevelFinished) {
            terminerNiveau();
            return;
        }

        // 2. Gestion du compte à rebours initial
        if (isCountingDown) {
            countdownTimer -= delta;

            if (countdownTimer > 0) {
                countdownLabel.setText(String.valueOf((int) Math.ceil(countdownTimer)));
            } else if (countdownTimer > -1.0f) {
                countdownLabel.setText("GO !");
                countdownLabel.setColor(Color.GREEN);
            } else {
                isCountingDown = false;
                countdownLabel.setVisible(false);
                music.play(); // Lancement de la musique à la fin du compte à rebours
            }

            draw(); // Dessine le décor en fond
            hudStage.act(delta);
            hudStage.draw();
            return;
        }

        // 3. Boucle de gameplay principale
        if (!pause) {
            logic(delta);
        }

        draw();

        // 4. Affichage du menu pause (par-dessus le jeu)
        if (pause) {
            pauseMenu.render(delta);
        }
    }

    /**
     * Gère la transition vers l'écran des scores à la fin de la musique.
     */
    private void terminerNiveau() {
        maxComboJoueur = gameEnvironnement.maxcombo;
        System.out.println("Niveau terminé ! Transition vers l'écran des scores...");
        game.setScreen(new EcranFinNiveau(game, maxComboJoueur, (int) vitesse, (int) vitesse_turbo, level_data, nombre_lane, souffle));
        this.dispose();
    }

    /**
     * Met à jour la logique interne du jeu : mouvement des blocs, chronomètres des boutons.
     */
    private void logic(float delta) {
        for (Button b : buttons) b.timer += delta;

        for (Lane lane : lanes) {
            if (lane == null) continue;
            for (Block block : lane.blocks) {
                if (block == null) continue;
                block.move(gameEnvironnement);
            }
        }
    }

    /**
     * Rendu graphique de tous les éléments du jeu.
     */
    private void draw() {
        // Profiling mémoire et performance
        long javaRam = Gdx.app.getJavaHeap() / (1024 * 1024);
        long nativeRam = Gdx.app.getNativeHeap() / (1024 * 1024);
        int fps = Gdx.graphics.getFramesPerSecond();
        debugLabel.setText("FPS: " + fps + " | Java: " + javaRam + " MB | Images (Native): " + nativeRam + " MB");

        batch.begin();
        background.sprite.draw(batch);
        ocarina.sprite.draw(batch);

        // Décommenter pour afficher les statistiques de debug
        // debugLabel.draw(batch, 1f);

        // Dessin des pistes (lanes) actives
        for (Lane lane : lanes) {
            if (lane.playabled) {
                batch.draw(lane.texture, lane.x, lane.y, lane.width, lane.height);
            }
        }

        // Dessin des boutons de contrôle
        for (Button button : buttons) {
            if (button == null || !button.visibled) continue;

            if (!button.clicked) {
                batch.draw(button.texture_unpressed, button.x, button.y, button.width, button.height);
            } else {
                batch.draw(button.texture_pressed, button.x, button.y, button.width, button.height);
            }
        }

        // Dessin des notes (blocs)
        for (Lane lane : lanes) {
            for (Block block : lane.blocks) {
                if (block == null) continue;
                if (block.visible) {
                    block.sprite_press.draw(batch);
                    block.sprite_default.draw(batch);
                    if (gameEnvironnement.turbo) {
                        block.sprite_turbo.draw(batch);
                    }
                }
            }
        }

        // Dessin des retours visuels (Erreur, Réussite, Multiplicateur de combo)
        if (errorIndiquator.visible) errorIndiquator.sprite.draw(batch);
        if (passeIndiquator.visible) passeIndiquator.sprite.draw(batch);

        if (comboController.comboIndiquatorCroix.visible) comboController.comboIndiquatorCroix.sprite.draw(batch);
        if (comboController.comboIndiquatorPremier.visible) comboController.comboIndiquatorPremier.sprite.draw(batch);
        if (comboController.comboIndiquatorDeuxieme.visible) comboController.comboIndiquatorDeuxieme.sprite.draw(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        if (hudStage != null) {
            hudStage.getViewport().update(width, height, true);
        }
    }

    /**
     * Bascule l'état de pause du jeu, stoppant ou reprenant la musique et la détection des inputs.
     */
    public void togglePause() {
        if (isCountingDown) return; // Impossible de mettre en pause pendant le décompte

        pause = !pause;

        if (pause) {
            music.pause();
            Gdx.input.setInputProcessor(pauseMenu.getStage());
        } else {
            music.play();
            Gdx.input.setInputProcessor(inputController);
        }
    }

    @Override
    public void pause() {
        if (!isCountingDown) {
            music.pause();
        }
    }

    @Override
    public void resume() {
        if (!isCountingDown && !pause) {
            music.play();
        }
    }

    @Override
    public void hide() {
        music.stop();
    }

    @Override
    public void dispose() {
        System.out.println("Nettoyage de la RAM de l'ancien niveau...");

        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (hudStage != null) hudStage.dispose();

        if (music != null) {
            music.stop();
            music.dispose();
        }

        if (pauseMenu != null) pauseMenu.dispose();
        if (skin != null) skin.dispose();
    }
}
