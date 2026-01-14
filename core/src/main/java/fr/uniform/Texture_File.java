package fr.uniform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public interface Texture_File {
    //multiplicateur taille texture
    int multiplicateur_texture = 5; // 5

    //Texture Block
    int TEXTURE_BLOCK_WIDTH = 32*multiplicateur_texture;
    int TEXTURE_BLOCK_HEIGHT = 32*multiplicateur_texture;
    float TEXTURE_OPACITY = 0.3f;

    Texture BLOCK_DEFAULT = new Texture(Gdx.files.internal("block_default.png"));
    Texture BLOCK_TURBO = new Texture(Gdx.files.internal("block_default.png"));


    //button
    int TEXTURE_BUTTON_WIDTH = 32*multiplicateur_texture;
    int TEXTURE_BUTTON_HEIGHT = 32*multiplicateur_texture;

    Texture BUTTON_F_UNPRESS = new Texture(Gdx.files.internal("f_button_unpress.png"));
    Texture BUTTON_F_PRESS = new Texture(Gdx.files.internal("f_button_press.png"));

    int multiplicateur_texture_lane = 15; // 15
    //lane
    int TEXTURE_LANE_WIDTH = 2*multiplicateur_texture_lane;
    int TEXTURE_LANE_HEIGHT = 64*multiplicateur_texture_lane;

    Texture LANE_DEFAULT = new Texture(Gdx.files.internal("lane.png"));;



}
