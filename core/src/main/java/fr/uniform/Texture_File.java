package fr.uniform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Texture_File {

    // --- LES CONSTANTES (Tailles, Multiplicateurs, etc.) ---
    public static final int multiplicateur_texture = 5;
    public static final int multiplicateur_texture_button = 5;

    public static final int COMBO_WIDTH_BASE = 16;
    public static final int COMBO_HEIGHT_BASE = 16;
    public static final int COMBO_WIDTH_TEXTURE = COMBO_WIDTH_BASE * (multiplicateur_texture+1);
    public static final int COMBO_HEIGHT_TEXTURE = COMBO_HEIGHT_BASE * (multiplicateur_texture+1);

    public static final int ERROR_INDIQUATOR_WIDTH = 32 * multiplicateur_texture;
    public static final int ERROR_INDIQUATOR_HEIGHT = 32 * multiplicateur_texture;
    public static final int PASS_INDIQUATOR_WIDTH = 32 * multiplicateur_texture;
    public static final int PASS_INDIQUATOR_HEIGHT = 32 * multiplicateur_texture;

    public static final int TEXTURE_BLOCK_DEFAULT_WIDTH = 32 * multiplicateur_texture_button;
    public static final int TEXTURE_BLOCK_DEFAULT_HEIGHT = 32 * multiplicateur_texture_button;
    public static final int TEXTURE_BLOCK_TURBO_WIDTH = 32 * multiplicateur_texture_button;
    public static final int TEXTURE_BLOCK_TURBO_HEIGHT = 64 * multiplicateur_texture_button;
    public static final float TEXTURE_OPACITY = 1f;
    public static final float TEXTURE_OPACITY_TURBO = 0.3f;
    public static final int TEXTURE_BLOCK_PRESS_WIDTH = 2 * multiplicateur_texture;

    public static final int TEXTURE_BUTTON_WIDTH = 32 * multiplicateur_texture_button;
    public static final int TEXTURE_BUTTON_HEIGHT = 32 * multiplicateur_texture_button;

    public static final int multiplicateur_texture_lane = 15;
    public static final int TEXTURE_LANE_WIDTH = 2 * multiplicateur_texture_lane;
    public static final int TEXTURE_LANE_HEIGHT = 256 * multiplicateur_texture_lane;

    public static final int TEXTURE_TITLE_WIDTH = 128 * multiplicateur_texture;
    public static final int TEXTURE_TITLE_HEIGHT = 64 * multiplicateur_texture;
    public static final int TEXTURE_TITLE_LEVEL_WIDTH = 128 * multiplicateur_texture;
    public static final int TEXTURE_TITLE_LEVEL_HEIGHT = 64 * multiplicateur_texture;


    public static final int multiplicateur_texture_ocarina = 17;
    public static final int TEXTURE_OCARINA_WIDTH = 64 * (multiplicateur_texture_ocarina + 4 ) ;
    public static final int TEXTURE_OCARINA_HEIGHT = 64 * multiplicateur_texture_ocarina;

    // --- LES TEXTURES ---
    public static Texture COMBO_ALL_NUMBERS;
    public static Texture ERROR_INDIQUATOR;
    public static Texture PASS_INDIQUATOR;
    public static Texture BLOCK_PRESS;
    public static Texture BLOCK_DEFAULT;
    public static Texture BLOCK_TURBO;

    public static Texture BUTTON_F_UNPRESS;
    public static Texture BUTTON_F_PRESS;
    public static Texture BUTTON_D_UNPRESS;
    public static Texture BUTTON_D_PRESS;
    public static Texture BUTTON_G_UNPRESS;
    public static Texture BUTTON_G_PRESS;
    public static Texture BUTTON_H_UNPRESS;
    public static Texture BUTTON_H_PRESS;
    public static Texture BUTTON_J_UNPRESS;
    public static Texture BUTTON_J_PRESS;
    public static Texture BUTTON_K_UNPRESS;
    public static Texture BUTTON_K_PRESS;

    public static Texture LANE_DEFAULT;
    public static Texture TITLE_DEFAULT;
    public static Texture TITLE_LEVEL_DEFAULT;
    public static Texture BUTTON_PLAY_DEFAULT;
    public static Texture BUTTON_OPTION_DEFAULT;
    public static Texture BACKGROUND;
    public static Texture OCARINA;

    public static Texture ENSIM_LOGO;


    // --- MÉTHODE D'INITIALISATION ---
    // Cette méthode va charger toutes les images d'un coup.
    public static void init() {
        COMBO_ALL_NUMBERS = new Texture("all_number.png");
        ERROR_INDIQUATOR = new Texture(Gdx.files.internal("failed.png"));
        PASS_INDIQUATOR = new Texture(Gdx.files.internal("pass.png"));
        BLOCK_PRESS = new Texture(Gdx.files.internal("continuenote.png"));
        BLOCK_DEFAULT = new Texture(Gdx.files.internal("block_default.png"));
        BLOCK_TURBO = new Texture(Gdx.files.internal("block_turbo.png"));

        BUTTON_F_UNPRESS = new Texture(Gdx.files.internal("button_unpress.png")); //f_
        BUTTON_F_PRESS = new Texture(Gdx.files.internal("button_press.png"));
        BUTTON_D_UNPRESS = new Texture(Gdx.files.internal("button_unpress.png"));//d_
        BUTTON_D_PRESS = new Texture(Gdx.files.internal("button_press.png"));
        BUTTON_G_UNPRESS = new Texture(Gdx.files.internal("button_unpress.png"));//g_
        BUTTON_G_PRESS = new Texture(Gdx.files.internal("button_press.png"));
        BUTTON_H_UNPRESS = new Texture(Gdx.files.internal("button_unpress.png"));//h_
        BUTTON_H_PRESS = new Texture(Gdx.files.internal("button_press.png"));
        BUTTON_J_UNPRESS = new Texture(Gdx.files.internal("button_unpress.png"));//j_
        BUTTON_J_PRESS = new Texture(Gdx.files.internal("button_press.png"));
        BUTTON_K_UNPRESS = new Texture(Gdx.files.internal("button_unpress.png")); //k_
        BUTTON_K_PRESS = new Texture(Gdx.files.internal("button_press.png"));

        LANE_DEFAULT = new Texture(Gdx.files.internal("lane.png"));
        TITLE_DEFAULT = new Texture(Gdx.files.internal("title.png"));
        TITLE_LEVEL_DEFAULT = new Texture(Gdx.files.internal("level.png"));
        BUTTON_PLAY_DEFAULT = new Texture(Gdx.files.internal("button_play.png"));
        BUTTON_OPTION_DEFAULT = new Texture(Gdx.files.internal("button_option.png"));
        BACKGROUND = new Texture(Gdx.files.internal("bg.png"));
        OCARINA = new Texture(Gdx.files.internal("ocarina.png"));

        ENSIM_LOGO = new Texture(Gdx.files.internal("ensim.png"));
    }

    // --- MÉTHODE DE DESTRUCTION (LE BULLDOZER) ---
    // Cette méthode va vider la RAM proprement.
    public static void dispose() {
        if (COMBO_ALL_NUMBERS != null) COMBO_ALL_NUMBERS.dispose();
        if (ERROR_INDIQUATOR != null) ERROR_INDIQUATOR.dispose();
        if (PASS_INDIQUATOR != null) PASS_INDIQUATOR.dispose();
        if (BLOCK_PRESS != null) BLOCK_PRESS.dispose();
        if (BLOCK_DEFAULT != null) BLOCK_DEFAULT.dispose();
        if (BLOCK_TURBO != null) BLOCK_TURBO.dispose();

        if (BUTTON_F_UNPRESS != null) BUTTON_F_UNPRESS.dispose();
        if (BUTTON_F_PRESS != null) BUTTON_F_PRESS.dispose();
        if (BUTTON_D_UNPRESS != null) BUTTON_D_UNPRESS.dispose();
        if (BUTTON_D_PRESS != null) BUTTON_D_PRESS.dispose();
        if (BUTTON_G_UNPRESS != null) BUTTON_G_UNPRESS.dispose();
        if (BUTTON_G_PRESS != null) BUTTON_G_PRESS.dispose();
        if (BUTTON_H_UNPRESS != null) BUTTON_H_UNPRESS.dispose();
        if (BUTTON_H_PRESS != null) BUTTON_H_PRESS.dispose();
        if (BUTTON_J_UNPRESS != null) BUTTON_J_UNPRESS.dispose();
        if (BUTTON_J_PRESS != null) BUTTON_J_PRESS.dispose();
        if (BUTTON_K_UNPRESS != null) BUTTON_K_UNPRESS.dispose();
        if (BUTTON_K_PRESS != null) BUTTON_K_PRESS.dispose();

        if (LANE_DEFAULT != null) LANE_DEFAULT.dispose();
        if (TITLE_DEFAULT != null) TITLE_DEFAULT.dispose();
        if (TITLE_LEVEL_DEFAULT != null) TITLE_LEVEL_DEFAULT.dispose();
        if (BUTTON_PLAY_DEFAULT != null) BUTTON_PLAY_DEFAULT.dispose();
        if (BUTTON_OPTION_DEFAULT != null) BUTTON_OPTION_DEFAULT.dispose();
        if (BACKGROUND != null) BACKGROUND.dispose();
        if (OCARINA != null) BACKGROUND.dispose();
        if (ENSIM_LOGO != null) ENSIM_LOGO.dispose();
    }
}
