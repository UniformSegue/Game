package fr.uniform;

public class GameEnvironnement {

    public boolean gameOver;
    public boolean turbo;
    public float block_vitesse;

    public GameEnvironnement(float block_vitesse) {
        this.turbo = false;
        this.block_vitesse = block_vitesse;
    }
}
