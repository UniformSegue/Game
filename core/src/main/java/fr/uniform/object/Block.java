package fr.uniform.object;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import fr.uniform.GameEnvironnement;
import fr.uniform.Texture_File;

public class Block {

    public int x;
    public int y;
    public int width;
    public int height;
    public boolean visible;
    public boolean fallen;
    public int vitesse;
    public Texture texture;
    public Sprite sprite;
    public Rectangle collision;
    private final Lane lane;
    private static float taille_collision_reduce;
    //private boolean touched = false; Debug jusqu'ou le block peut toucher le button


    public Block(int x, int y, int width, int height, boolean fallen, int vitesse, Texture texture, Lane lane) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
        this.fallen = fallen;
        this.vitesse = vitesse;
        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.lane = lane;
        sprite.setSize(width, height);
        sprite.setPosition(x, y);
        sprite.setColor(1.0f, 1.0f, 1.0f, Texture_File.TEXTURE_OPACITY);

        taille_collision_reduce = height * 0.50f;
        collision = new Rectangle(x, y, (float) width, (float) height - taille_collision_reduce);

    }

    public boolean checkBlockClicked(){

        updatePosition();
        this.collision.setPosition(this.x, this.y+taille_collision_reduce);

        if(this.lane.assignedButton.clicked){
            if(this.collision.overlaps(this.lane.assignedButton.collision)){
                fallen = false;
                return true;
            }
        }

        /* Debug jusqu'ou le block peut toucher le button
        if(touched && !this.collision.overlaps(this.lane.assignedButton.collision)){
            fallen = false;
        }*/
        return false;
    }

    public void updatePosition(){
        this.x = (int)this.sprite.getX();
        this.y = (int)this.sprite.getY();


    }

    public static Block spawnBlock(Lane lane, GameEnvironnement gameEnvironnement,float screenHeight){

        if(lane.playabled){
            return new Block(lane.x-((Texture_File.TEXTURE_BLOCK_WIDTH - Texture_File.TEXTURE_LANE_WIDTH)/2)
                ,(int)screenHeight-Texture_File.TEXTURE_BLOCK_HEIGHT,Texture_File.TEXTURE_BLOCK_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,true, (int)gameEnvironnement.block_vitesse,(gameEnvironnement.turbo) ? Texture_File.BLOCK_DEFAULT : Texture_File.BLOCK_TURBO,lane);
        }
        return null;
    }

}
