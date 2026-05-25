package fr.uniform.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.*;
import fr.uniform.GameEnvironnement;
import fr.uniform.Texture_File;
import fr.uniform.object.game.Block;
import fr.uniform.object.game.Lane;
import fr.uniform.object.game.Note;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur chargé de la gestion des données musicales (beatmaps).
 * Lit les fichiers de niveau JSON, instancie les notes en fonction de leur horodatage (timestamp),
 * et associe les blocs physiques aux pistes correspondantes.
 */
public class MusicController {

    private List<Lane> availableLanes;
    private String currentAudioFileName;
    private List<Note> currentNotes;
    private GameEnvironnement gameEnvironnement;

    /**
     * Initialise le contrôleur musical.
     *
     * @param lanes             La liste des pistes disponibles dans l'environnement de jeu.
     * @param gameEnvironnement L'environnement global (pour récupérer les vitesses et dimensions).
     */
    public MusicController(List<Lane> lanes, GameEnvironnement gameEnvironnement) {
        this.availableLanes = lanes;
        this.currentNotes = new ArrayList<>();
        this.gameEnvironnement = gameEnvironnement;
    }

    /**
     * Charge le fichier JSON, parse les données du niveau et associe les index aux objets Lane.
     * Gère à la fois les chemins absolus (niveaux Custom) et internes (niveaux de base).
     *
     * @param jsonFilePath Le chemin (interne ou absolu) vers le fichier .json de la beatmap.
     */
    public void loadMusicData(String jsonFilePath) {

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Note.class, new NoteDeserializer())
            .create();

        try {
            // 1. Recherche du fichier via le système de fichiers multi-plateforme de LibGDX
            FileHandle file = Gdx.files.absolute(jsonFilePath);

            // Si le fichier n'existe pas en chemin absolu, on cherche dans les assets internes
            if (!file.exists()) {
                file = Gdx.files.internal(jsonFilePath);

                // Sécurité supplémentaire : vérification dans le dossier par défaut "music/"
                if (!file.exists()) {
                    file = Gdx.files.internal("music/" + jsonFilePath);
                }
            }

            // 2. Lecture et désérialisation sécurisée
            if (file.exists()) {
                // Utilisation de file.reader() fourni par LibGDX pour éviter les crashs à l'export (.jar / Android)
                try (Reader reader = file.reader()) {
                    LevelData data = gson.fromJson(reader, LevelData.class);

                    this.currentAudioFileName = data.audioFile;
                    this.currentNotes = data.notes;

                    System.out.println("Succes : " + currentNotes.size() + " notes chargees pour " + currentAudioFileName);
                }
            } else {
                System.err.println("ERREUR : Fichier JSON introuvable -> " + jsonFilePath);
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fichier JSON : " + jsonFilePath);
            e.printStackTrace();
        }
    }

    public String getAudioFileName() {
        return currentAudioFileName;
    }

    public List<Note> getNotes() {
        return currentNotes;
    }

    /**
     * Structure de données interne utilisée par Gson pour mapper la racine du fichier JSON.
     */
    private static class LevelData {
        String audioFile;
        List<Note> notes;
    }

    /**
     * Désérialiseur personnalisé pour traduire un bloc JSON "Note" en objet Java `Note`.
     * Vérifie et associe l'index numérique de la piste à l'objet `Lane` correspondant.
     */
    private class NoteDeserializer implements JsonDeserializer<Note> {
        @Override
        public Note deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            int timestamp = jsonObject.get("timestamp").getAsInt();
            int laneIndex = jsonObject.get("lane").getAsInt();
            int time_press = jsonObject.get("time_press").getAsInt();

            Lane selectedLane = null;
            if (laneIndex >= 0 && laneIndex < availableLanes.size()) {
                selectedLane = availableLanes.get(laneIndex);
            } else {
                System.err.println("ERREUR CRITIQUE : Index de piste (lane) " + laneIndex + " invalide dans le JSON.");
            }

            return new Note(selectedLane, timestamp, time_press);
        }
    }

    /**
     * Instancie les blocs visuels et physiques pour chaque note chargée, en calculant
     * leur position Y initiale en fonction de leur timestamp d'apparition et de la vitesse de défilement.
     *
     * @param delaTime L'écart de temps initial (en ms) par rapport à la lecture audio.
     */
    public void generateNotes(long delaTime) {
        if (!currentNotes.isEmpty()) {
            for (Note note : currentNotes) {
                // Instanciation de référence pour calculer la hauteur
                note.block = new Block(
                    note.lane.x,
                    calculerY(delaTime, note.timestamp, gameEnvironnement.vitesse_actuelle_pixel_per_ms),
                    Texture_File.TEXTURE_BLOCK_DEFAULT_WIDTH,
                    Texture_File.TEXTURE_BLOCK_DEFAULT_HEIGHT,
                    note.time_press,
                    true,
                    gameEnvironnement.block_vitesse_pixel_per_frame,
                    Texture_File.BLOCK_DEFAULT,
                    Texture_File.BLOCK_TURBO,
                    Texture_File.BLOCK_PRESS,
                    note.lane
                );

                // Création du bloc définitif via la Factory et ajout à la piste
                Block block = Block.spawnBlock(note.lane, gameEnvironnement, (int) note.block.y, note);
                note.lane.blocks.add(block);
            }
        }
    }

    /**
     * Calcule la position verticale de départ (Y) d'une note.
     * Assure que la note atteindra la ligne de validation exactement au moment prévu (timestamp).
     *
     * @param delta     Le temps écoulé de la musique.
     * @param timestamp Le moment où la note doit être jouée.
     * @param vitesse   La vitesse de défilement en pixels par milliseconde.
     * @return La position Y initiale du bloc sur l'écran.
     */
    private int calculerY(float delta, int timestamp, float vitesse) {
        int y_cible = gameEnvironnement.hauter_y_line - Texture_File.TEXTURE_BUTTON_HEIGHT / 2;
        return (int) (y_cible + (timestamp - delta) * vitesse);
    }
}
