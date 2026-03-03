package fr.uniform.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    public Texture texture_base;
    public Texture texture_turbo;
    public Texture texture_press;
    public Sprite sprite_default;
    public Sprite sprite_turbo;
    public Sprite sprite_press;
    public Rectangle collision;
    private final Lane lane;
    private static float taille_collision_reduce;
    public boolean pass_clicked;
    public float press_height;
    public int press_time;
    //private boolean touched = false; Debug jusqu'ou le block peut toucher le button


    public Block(int x, float y, int width, int height, int press_time,boolean fallen, float vitesse, Texture texture_base,Texture texture_turbo,Texture texture_press, Lane lane) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.press_time = press_time;
        this.visible = true;
        this.fallen = fallen;
        this.vitesse = vitesse;
        this.texture_base = texture_base;
        this.texture_turbo = texture_turbo;
        this.texture_press = texture_press;
        this.sprite_default = new Sprite(this.texture_base);
        this.sprite_turbo = new Sprite(this.texture_turbo);
        this.sprite_press = new Sprite(this.texture_press);
        this.lane = lane;
        sprite_default.setSize(width, height);
        sprite_default.setPosition(x, y);
        sprite_default.setAlpha(Texture_File.TEXTURE_OPACITY);
        sprite_turbo.setSize(Texture_File.TEXTURE_BLOCK_TURBO_WIDTH, Texture_File.TEXTURE_BLOCK_TURBO_HEIGHT);
        sprite_turbo.setPosition(x, y);
        sprite_turbo.setAlpha(0);
        sprite_press.setPosition(x, y);
        sprite_press.setSize(width, 1);
        sprite_press.setAlpha(Texture_File.TEXTURE_OPACITY);
        this.pass_clicked = false;


        taille_collision_reduce = 0;
        collision = new Rectangle(x, y, (float) width, (float) height - taille_collision_reduce);

    }

    public boolean checkBlockClicked(GameEnvironnement gameEnvironnement) {

        this.collision.setPosition(this.x, this.y+taille_collision_reduce);

        if(this.lane.assignedButton.clicked){

            if(this.collision.overlaps(this.lane.assignedButton.collision)){

                pass_clicked = true;

                destroy();

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


    public static Block spawnBlock(Lane lane, GameEnvironnement gameEnvironnement,int y,Note note){

        if(lane.playabled){
            Block block =new Block(lane.x-((Texture_File.TEXTURE_BLOCK_DEFAULT_HEIGHT - Texture_File.TEXTURE_LANE_WIDTH)/2)
                ,y,Texture_File.TEXTURE_BLOCK_DEFAULT_WIDTH,Texture_File.TEXTURE_BUTTON_HEIGHT,note.time_press,true, gameEnvironnement.vitesse_actuelle_pixel_per_frame,Texture_File.BLOCK_DEFAULT, Texture_File.BLOCK_TURBO,Texture_File.BLOCK_PRESS,lane);
            block.calculHeightPress(gameEnvironnement);
            return block;
        }
        return null;
    }

    public void destroy(){

        visible = false;
        fallen = false;
        vitesse = 0;

    }

    public void move(GameEnvironnement gameEnvironnement){
        turbo(gameEnvironnement);
        if (this.visible && this.fallen) {

            this.y = this.y - this.vitesse;
            this.sprite_default.setY(this.y);
            this.sprite_press.setY(this.y+height);

            if (this.sprite_default.getY() < -this.height) {
                this.visible = false;
                gameEnvironnement.ComboManager(false);
            }
        }
    }

    public void updateVitesse(float vitesse){
        this.vitesse = vitesse;
    }

    public void turbo(GameEnvironnement gameEnvironnement){
        if(gameEnvironnement.turbo){
            sprite_turbo.setPosition(this.x, this.y);
            sprite_turbo.setAlpha(Texture_File.TEXTURE_OPACITY_TURBO);

        }else {
            sprite_turbo.setPosition(this.x, this.y);
            sprite_turbo.setAlpha(0);
        }
    }

    public void calculHeightPress(GameEnvironnement gameEnvironnement){
        System.out.println(gameEnvironnement.vitesse_actuelle_pixel_per_ms*press_time);
        press_height = gameEnvironnement.vitesse_actuelle_pixel_per_ms*press_time;
        System.out.println(press_height);
        sprite_press.setSize(Texture_File.TEXTURE_BLOCK_DEFAULT_WIDTH, press_height);
    }
}
