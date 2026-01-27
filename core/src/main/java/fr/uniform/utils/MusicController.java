package fr.uniform.utils;

import com.google.gson.*;
import fr.uniform.GameEnvironnement;
import fr.uniform.Texture_File;
import fr.uniform.object.Block;
import fr.uniform.object.Lane;
import fr.uniform.object.Note;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MusicController {

    private List<Lane> availableLanes;

    private String currentAudioFileName;
    private List<Note> currentNotes;
    private GameEnvironnement gameEnvironnement;

    public MusicController(List<Lane> lanes, GameEnvironnement gameEnvironnement) {
        this.availableLanes = lanes;
        this.currentNotes = new ArrayList<>();
        this.gameEnvironnement = gameEnvironnement;
    }

    /**
     * Charge le fichier JSON, parse les notes et lie les index aux objets Lane.
     * @param jsonFilePath Le chemin vers le fichier .json
     */
    public void loadMusicData(String jsonFilePath) {

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Note.class, new NoteDeserializer())
            .create();

        try (Reader reader = new FileReader(jsonFilePath)) {


            LevelData data = gson.fromJson(reader, LevelData.class);


            this.currentAudioFileName = data.audioFile;
            this.currentNotes = data.notes;

            System.out.println("Succes : " + currentNotes.size() + " notes chargees pour " + currentAudioFileName);

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier JSON : " + jsonFilePath);
            e.printStackTrace();
        }
    }

    public String getAudioFileName() { return currentAudioFileName; }
    public List<Note> getNotes() { return currentNotes; }


    private static class LevelData {
        String audioFile;
        List<Note> notes;
    }

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
                System.err.println("ERREUR CRITIQUE : Lane index " + laneIndex + " invalide dans le JSON.");
            }

            return new Note(selectedLane, timestamp,time_press);
        }
    }

    public void generateNotes(long delaTime){
        if (!currentNotes.isEmpty()) {
            for (Note note : currentNotes) {

                note.block = new Block(note.lane.x,calculerY(delaTime,note.timestamp,gameEnvironnement.vitesse_actuelle_pixel_per_ms), Texture_File.TEXTURE_BLOCK_DEFAULT_WIDTH,Texture_File.TEXTURE_BLOCK_DEFAULT_HEIGHT,Texture_File.TEXTURE_BLOCK_PRESS_WIDTH,note.calculateHeight(gameEnvironnement.block_vitesse_pixel_per_ms),note.time_press, true,gameEnvironnement.block_vitesse_pixel_per_frame,Texture_File.BLOCK_DEFAULT,Texture_File.BLOCK_TURBO,Texture_File.BLOCK_PRESS, note.lane);
                Block block = Block.spawnBlock(note.lane,gameEnvironnement,(int)note.block.y,note);
                note.lane.blocks.add(block);

            }
        }
    }

    private int calculerY(float delta, int timestamp, float vitesse) {
        int y_cible = gameEnvironnement.hauter_y_line - Texture_File.TEXTURE_BUTTON_HEIGHT/2;

        return (int) ( y_cible + (timestamp- delta) * vitesse);
    }

}
