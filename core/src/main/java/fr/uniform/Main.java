package fr.uniform;

import com.badlogic.gdx.Game;
import fr.uniform.screen.MenuScreen;

/**
 * Point d'entrée principal et contrôleur global du jeu LibGDX.
 * Gère le cycle de vie de l'application, l'initialisation des ressources globales
 * et le routage initial vers l'écran du menu principal.
 */
public class Main extends Game {

    @Override
    public void create() {
        // Initialisation de toutes les ressources graphiques globales (textures) en mémoire
        Texture_File.init();

        // Lancement et affichage du premier écran du jeu
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        // Appelle le dispose() de l'écran actuellement actif
        super.dispose();

        // Libération de la mémoire vidéo pour toutes les textures globales
        // afin d'éviter les fuites de mémoire (memory leaks) à la fermeture
        Texture_File.dispose();
    }
}
