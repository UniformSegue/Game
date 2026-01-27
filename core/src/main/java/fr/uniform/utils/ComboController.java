package fr.uniform.utils;

import fr.uniform.GameEnvironnement;
import fr.uniform.Texture_File;
import fr.uniform.object.ComboIndiquator;

public class ComboController {

    public ComboIndiquator comboIndiquatorCroix;
    public ComboIndiquator comboIndiquatorPremier;
    public ComboIndiquator comboIndiquatorDeuxieme;
    private int x;
    private int y;

    public ComboController(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setIndiquator() {

        //set x combo
        comboIndiquatorCroix = new ComboIndiquator(x,y, Texture_File.COMBO_ALL_NUMBERS);
        comboIndiquatorCroix.changeTexture(10);

        //Set Premier Nombre
        comboIndiquatorPremier = new ComboIndiquator(x+Texture_File.COMBO_WIDTH_TEXTURE,y,Texture_File.COMBO_ALL_NUMBERS);
        comboIndiquatorPremier.changeTexture(0);

        //Set Deuxieme Nombre
        comboIndiquatorDeuxieme = new ComboIndiquator(x+2*(Texture_File.COMBO_WIDTH_TEXTURE),y, Texture_File.COMBO_ALL_NUMBERS);
        comboIndiquatorDeuxieme.changeTexture(0);

    }

    public void updateNumber(int number) {

        if(number < 10){
            comboIndiquatorPremier.changeTexture(number);
            comboIndiquatorDeuxieme.visible = false;

        }else {
            int dizaine = number / 10;
            comboIndiquatorPremier.changeTexture(dizaine);
            comboIndiquatorDeuxieme.visible = true;
            int unite = number - dizaine*10;
            comboIndiquatorDeuxieme.changeTexture(unite);

        }

    }
    public void enabledComboIndiquator() {
        comboIndiquatorCroix.visible = true;
        comboIndiquatorPremier.visible = true;
        comboIndiquatorDeuxieme.visible = true;
    }

    public void disableComboIndiquator(){
        comboIndiquatorCroix.visible = false;
        comboIndiquatorPremier.visible = false;
        comboIndiquatorDeuxieme.visible = false;
    }

}
