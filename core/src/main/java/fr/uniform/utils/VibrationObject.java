package fr.uniform.utils;

import fr.uniform.object.Block;

import java.util.Random;

public class VibrationObject {

    public static void VibrateBlock(Block block) {

        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        float value_vibrate_x = r.nextFloat()*10;


    }
}
