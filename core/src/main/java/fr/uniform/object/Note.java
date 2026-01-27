package fr.uniform.object;

public class Note {

    public Lane lane;
    public int timestamp; // en ms
    public int time_press;
    public Block block;


    public Note(Lane lane, int timestamp, int time_press) {
        this.lane = lane;
        this.timestamp = timestamp;
        this.time_press = time_press;

    }

    public int calculateHeight(float currentScrollSpeed) {
        if (time_press <= 0) {
            return 50; // Hauteur par défaut pour une note simple (tap)
        }
        return (int) (currentScrollSpeed * time_press);
    }
}
