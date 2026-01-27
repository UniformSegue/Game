package fr.uniform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public interface Texture_File {
    //multiplicateur taille texture
    int multiplicateur_texture = 5; // 5

    //Combo Indiquator
    int COMBO_WIDTH_BASE = 16;
    int COMBO_HEIGHT_BASE = 16;
    int COMBO_WIDTH_TEXTURE = COMBO_WIDTH_BASE*multiplicateur_texture;
    int COMBO_HEIGHT_TEXTURE = COMBO_HEIGHT_BASE*multiplicateur_texture;

    Texture COMBO_ALL_NUMBERS = new Texture("all_number.png");

    //Error Indiquator
    Texture ERROR_INDIQUATOR = new Texture(Gdx.files.internal("failed.png"));
    Texture PASS_INDIQUATOR = new Texture(Gdx.files.internal("pass.png"));
    int ERROR_INDIQUATOR_WIDTH = 32*multiplicateur_texture;
    int ERROR_INDIQUATOR_HEIGHT = 32*multiplicateur_texture;
    int PASS_INDIQUATOR_WIDTH = 32*multiplicateur_texture;
    int PASS_INDIQUATOR_HEIGHT = 32*multiplicateur_texture;

    //Texture Block
    int TEXTURE_BLOCK_DEFAULT_WIDTH = 32*multiplicateur_texture;
    int TEXTURE_BLOCK_DEFAULT_HEIGHT = 32*multiplicateur_texture;
    int TEXTURE_BLOCK_TURBO_WIDTH = 32*multiplicateur_texture;
    int TEXTURE_BLOCK_TURBO_HEIGHT = 64*multiplicateur_texture;
    float TEXTURE_OPACITY = 0.6f;
    float TEXTURE_OPACITY_TURBO = 0.3f;
    int TEXTURE_BLOCK_PRESS_WIDTH = 2*multiplicateur_texture;
    Texture BLOCK_PRESS = new Texture(Gdx.files.internal("lane.png"));


    Texture BLOCK_DEFAULT = new Texture(Gdx.files.internal("block_default.png"));
    Texture BLOCK_TURBO = new Texture(Gdx.files.internal("block_turbo.png"));


    //button
    int TEXTURE_BUTTON_WIDTH = 32*multiplicateur_texture;
    int TEXTURE_BUTTON_HEIGHT = 32*multiplicateur_texture;

    Texture BUTTON_F_UNPRESS = new Texture(Gdx.files.internal("f_button_unpress.png"));
    Texture BUTTON_F_PRESS = new Texture(Gdx.files.internal("f_button_press.png"));
    Texture BUTTON_D_UNPRESS = new Texture(Gdx.files.internal("d_button_unpress.png"));
    Texture BUTTON_D_PRESS = new Texture(Gdx.files.internal("d_button_press.png"));
    Texture BUTTON_G_UNPRESS = new Texture(Gdx.files.internal("g_button_unpress.png"));
    Texture BUTTON_G_PRESS = new Texture(Gdx.files.internal("g_button_press.png"));
    Texture BUTTON_H_UNPRESS = new Texture(Gdx.files.internal("h_button_unpress.png"));
    Texture BUTTON_H_PRESS = new Texture(Gdx.files.internal("h_button_press.png"));
    Texture BUTTON_J_UNPRESS = new Texture(Gdx.files.internal("j_button_unpress.png"));
    Texture BUTTON_J_PRESS = new Texture(Gdx.files.internal("j_button_press.png"));
    Texture BUTTON_K_UNPRESS = new Texture(Gdx.files.internal("k_button_unpress.png"));
    Texture BUTTON_K_PRESS = new Texture(Gdx.files.internal("k_button_press.png"));


    //lanes
    int multiplicateur_texture_lane = 15; // 15

    int TEXTURE_LANE_WIDTH = 2*multiplicateur_texture_lane;
    int TEXTURE_LANE_HEIGHT = 256*multiplicateur_texture_lane;

    Texture LANE_DEFAULT = new Texture(Gdx.files.internal("lane.png"));



}
