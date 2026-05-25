package fr.uniform.object.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import fr.uniform.GameEnvironnement;
import fr.uniform.Texture_File;

/**
 * Représente un bloc (ou une note) qui tombe sur une piste (Lane).
 * Gère le rendu visuel, la physique de chute, et la détection de collision
 * avec les boutons (incluant les mécaniques de notes simples et maintenues).
 */
public class Block {

    // --- Propriétés spatiales et physiques ---
    public int x;
    public float y;
    public int width;
    public int height;
    public float vitesse;
    public boolean visible;
    public boolean fallen;

    // --- Textures et Sprites ---
    public Texture texture_base;
    public Texture texture_turbo;
    public Texture texture_press;
    private Texture texture_press_dessus;
    public Sprite sprite_default;
    public Sprite sprite_turbo;
    public Sprite sprite_press;

    // --- Collisions et Logique de jeu ---
    public Rectangle collision_base;
    public Rectangle collision_up;
    private final Lane lane;
    private static float taille_collision_reduce = 0;

    public boolean pass_clicked;
    public float press_height;
    public int press_time;
    private boolean clicked_base;
    public boolean pass_combo;

    /**
     * Constructeur d'un bloc.
     */
    public Block(int x, float y, int width, int height, int press_time, boolean fallen, float vitesse,
                 Texture texture_base, Texture texture_turbo, Texture texture_press, Lane lane) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.press_time = press_time;

        this.visible = true;
        this.fallen = fallen;
        this.vitesse = vitesse;
        this.lane = lane;

        this.texture_base = texture_base;
        this.texture_turbo = texture_turbo;
        this.texture_press = texture_press;

        // Initialisation des sprites
        this.sprite_default = new Sprite(this.texture_base);
        this.sprite_default.setSize(width, height);
        this.sprite_default.setPosition(x, y);
        this.sprite_default.setAlpha(Texture_File.TEXTURE_OPACITY);

        this.sprite_turbo = new Sprite(this.texture_turbo);
        this.sprite_turbo.setSize(Texture_File.TEXTURE_BLOCK_TURBO_WIDTH, Texture_File.TEXTURE_BLOCK_TURBO_HEIGHT);
        this.sprite_turbo.setPosition(x, y);
        this.sprite_turbo.setAlpha(0);

        this.sprite_press = new Sprite(this.texture_press);
        this.sprite_press.setPosition(x, y);
        this.sprite_press.setSize(width, 1);
        this.sprite_press.setAlpha(Texture_File.TEXTURE_OPACITY);

        // États de validation
        this.pass_clicked = false;
        this.pass_combo = false;
        this.clicked_base = false;

        // Hitboxes (Zones de collision)
        this.collision_base = new Rectangle(x, y, (float) width, (float) height - taille_collision_reduce);
        this.collision_up = new Rectangle(x, y, (float) width, 1f);
    }

    /**
     * Vérifie si le bloc entre en collision avec le bouton de sa piste,
     * et traite l'entrée du joueur selon s'il s'agit d'une note simple ou maintenue.
     */
    public void checkBlockClicked(GameEnvironnement gameEnvironnement) {
        if (!visible || pass_combo) return;

        // Mise à jour de la position des hitboxes
        this.collision_base.setPosition(this.x, this.y + taille_collision_reduce);
        this.collision_up.setPosition(this.x, this.y + press_height + 100);

        // --- Logique pour les notes maintenues (Hold notes) ---
        if (this.press_time > 0) {
            // 1. Validation du début de la note
            if (!clicked_base && this.lane.assignedButton.clicked) {
                if (this.collision_base.overlaps(this.lane.assignedButton.collision)) {
                    clicked_base = true;
                }
            }
            // 2. Validation de la fin de la note
            if (clicked_base && this.lane.assignedButton.clicked) {
                if (this.collision_up.overlaps(this.lane.assignedButton.collision) && this.visible) {
                    destroy(true, gameEnvironnement);
                }
            }
        }

        // --- Logique pour les notes simples (Tap notes) ---
        if (this.press_time == 0) {
            if (this.lane.assignedButton.clicked) {
                if (this.collision_base.overlaps(this.lane.assignedButton.collision)) {
                    pass_clicked = true;
                    destroy(true, gameEnvironnement);
                }
            }
        }
    }

    /**
     * Instancie un nouveau bloc sur une piste donnée.
     * Utilise le pattern Factory Method.
     */
    public static Block spawnBlock(Lane lane, GameEnvironnement gameEnvironnement, int y, Note note) {
        if (lane.playabled) {
            int blockX = lane.x - ((Texture_File.TEXTURE_BLOCK_DEFAULT_HEIGHT - Texture_File.TEXTURE_LANE_WIDTH) / 2);

            Block block = new Block(blockX, y, Texture_File.TEXTURE_BLOCK_DEFAULT_WIDTH, Texture_File.TEXTURE_BUTTON_HEIGHT,
                note.time_press, true, gameEnvironnement.vitesse_actuelle_pixel_per_frame,
                Texture_File.BLOCK_DEFAULT, Texture_File.BLOCK_TURBO, Texture_File.BLOCK_PRESS, lane);

            block.calculHeightPress(gameEnvironnement);
            return block;
        }
        return null;
    }

    /**
     * Détruit le bloc visuellement et l'enregistre dans le gestionnaire de combo.
     * @param status True si le bloc a été validé correctement par le joueur, False sinon (miss).
     */
    public void destroy(boolean status, GameEnvironnement gameEnvironnement) {
        if (pass_combo) return;

        if (visible) {
            gameEnvironnement.ComboManager(status, this);
            pass_combo = true;
        }

        visible = false;
        fallen = false;
        vitesse = 0;
    }

    /**
     * Gère la descente du bloc à chaque frame et vérifie s'il sort de l'écran (Miss).
     */
    public void move(GameEnvironnement gameEnvironnement) {
        if (!this.visible) return;

        checkBlockClicked(gameEnvironnement);
        turbo(gameEnvironnement);

        if (this.visible && this.fallen) {
            this.y -= this.vitesse;
            this.sprite_default.setY(this.y);
            this.sprite_press.setY(this.y + height);

            // Le joueur a raté le début de la note (Miss classique)
            if (this.sprite_default.getY() < -100 && !this.clicked_base) {
                this.visible = false;
                gameEnvironnement.ComboManager(false, this);
            }
            // Le joueur a commencé une note maintenue mais l'a relâchée trop tôt
            if (this.sprite_press.getY() < -press_height && this.clicked_base) {
                this.visible = false;
                gameEnvironnement.ComboManager(false, this);
            }
        }
    }

    public void updateVitesse(float vitesse) {
        this.vitesse = vitesse;
    }

    /**
     * Met à jour l'affichage de l'effet turbo si celui-ci est activé dans l'environnement.
     */
    public void turbo(GameEnvironnement gameEnvironnement) {
        sprite_turbo.setPosition(this.x, this.y);

        if (gameEnvironnement.turbo) {
            sprite_turbo.setAlpha(Texture_File.TEXTURE_OPACITY_TURBO);
        } else {
            sprite_turbo.setAlpha(0);
        }
    }

    /**
     * Calcule la hauteur visuelle de la "traîne" d'une note maintenue
     * en fonction de sa durée et de la vitesse de défilement actuelle.
     */
    public void calculHeightPress(GameEnvironnement gameEnvironnement) {
        press_height = gameEnvironnement.vitesse_actuelle_pixel_per_ms * press_time;
        sprite_press.setSize(Texture_File.TEXTURE_BLOCK_DEFAULT_WIDTH, press_height);
    }
}
