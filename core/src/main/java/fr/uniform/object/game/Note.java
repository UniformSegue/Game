package fr.uniform.object.game;

/**
 * Représente les données brutes d'une note telles que lues depuis le fichier de niveau (beatmap JSON).
 * Sert de modèle pour instancier l'objet visuel et physique (Block) correspondant au bon moment.
 */
public class Note {

    // --- Données de la beatmap ---

    /** La piste sur laquelle la note doit apparaître. */
    public Lane lane;

    /** Le moment exact (en millisecondes) où le joueur doit appuyer sur la note. */
    public int timestamp;

    /** * La durée (en millisecondes) pendant laquelle la note doit être maintenue.
     * Vaut 0 pour une note simple (Tap).
     */
    public int time_press;

    // --- Référence en jeu ---

    /** L'entité physique et visuelle générée à partir de ces données brutes. */
    public Block block;

    /**
     * Instancie une nouvelle donnée de note.
     *
     * @param lane       La piste assignée à cette note.
     * @param timestamp  Le temps de validation cible depuis le début de la musique (en ms).
     * @param time_press La durée de maintien (en ms, 0 si note simple).
     */
    public Note(Lane lane, int timestamp, int time_press) {
        this.lane = lane;
        this.timestamp = timestamp;
        this.time_press = time_press;
    }

    /**
     * Calcule la hauteur théorique que devrait avoir la "traîne" de la note visuelle
     * en fonction de la vitesse de défilement (scroll speed) actuelle du jeu.
     *
     * @param currentScrollSpeed La vitesse de défilement actuelle.
     * @return La hauteur de la note en pixels.
     */
    public int calculateHeight(float currentScrollSpeed) {
        if (time_press <= 0) {
            return 50; // Hauteur visuelle par défaut pour une note simple (Tap)
        }

        // Pour une note maintenue (Hold), la hauteur est proportionnelle à la durée et à la vitesse
        return (int) (currentScrollSpeed * time_press);
    }
}
