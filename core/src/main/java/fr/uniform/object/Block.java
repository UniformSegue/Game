package fr.uniform.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import fr.uniform.GameEnvironnement;
import fr.uniform.Texture_File;

public class Block {

    public int x;
    public float y;
    public int width;
    public int height;
    public boolean visible;
    public boolean fallen;
    public float vitesse;
    public Texture texture;
    public Sprite sprite;
    public Rectangle collision;
    private final Lane lane;
    private static float taille_collision_reduce;
    public boolean pass;
    //private boolean touched = false; Debug jusqu'ou le block peut toucher le button


    public Block(int x, float y, int width, int height, boolean fallen, float vitesse, Texture texture, Lane lane) {
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
        sprite.setAlpha(Texture_File.TEXTURE_OPACITY);
        this.pass = false;

        taille_collision_reduce = 0;
        collision = new Rectangle(x, y, (float) width, (float) height - taille_collision_reduce);

    }

    public boolean checkBlockClicked(GameEnvironnement gameEnvironnement) {

        this.collision.setPosition(this.x, this.y+taille_collision_reduce);

        if(this.lane.assignedButton.clicked){
            if(this.collision.overlaps(this.lane.assignedButton.collision)){
                destroy();
                pass = true;
                gameEnvironnement.ComboManager(true);
                return true;
            }else{
                gameEnvironnement.ComboManager(false);
                return false;
            }
        }

        /* Debug jusqu'ou le block peut toucher le button
        if(touched && !this.collision.overlaps(this.lane.assignedButton.collision)){
            fallen = false;
        }*/
        return false;
    }


    public static Block spawnBlock(Lane lane, GameEnvironnement gameEnvironnement,int y){

        if(lane.playabled){
            return new Block(lane.x-((Texture_File.TEXTURE_BLOCK_WIDTH - Texture_File.TEXTURE_LANE_WIDTH)/2)
                ,y,Texture_File.TEXTURE_BLOCK_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,true, gameEnvironnement.block_vitesse_pixel_per_frame,(gameEnvironnement.turbo) ? Texture_File.BLOCK_DEFAULT : Texture_File.BLOCK_TURBO,lane);
        }
        return null;
    }

    public void destroy(){

        visible = false;
        fallen = false;
        vitesse = 0;

    }

    public void move(GameEnvironnement gameEnvironnement){

        if (this.visible && this.fallen) {

            this.y = this.y - this.vitesse;
            this.sprite.setY(this.y);

            if (this.sprite.getY() < -this.height) {
                this.visible = false;
                gameEnvironnement.ComboManager(false);
            }
        }
    }

}
