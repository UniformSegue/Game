package fr.uniform.object;

public class Note {

    public Lane lane;
    public int timestamp; // en ms
    public Block block;


    public Note(Lane lane, int timestamp) {
        this.lane = lane;
        this.timestamp = timestamp;

    }

}
