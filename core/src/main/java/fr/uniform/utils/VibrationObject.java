package fr.uniform.utils;

import fr.uniform.object.Block;

import java.util.Random;

public class VibrationObject {

    public static void VibrateBlock(Block block) {

        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        float value_vibrate_x = r.nextFloat()*10;

        block.sprite_press.translateX(value_vibrate_x);
        block.sprite_turbo.translateX(value_vibrate_x);
        block.sprite_default.translateX(value_vibrate_x);
        block.sprite_press.translateX(-value_vibrate_x);
        block.sprite_turbo.translateX(-value_vibrate_x);
        block.sprite_default.translateX(-value_vibrate_x);

    }
}
