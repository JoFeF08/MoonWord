package com.example.moonword;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Tema {

    private static Tema tema1 = new Tema(R.drawable.terra, R.drawable.jupiter, R.drawable.lluna, R.drawable.fons1, Color.parseColor("#53acac"));

    private static Tema[] temas = {tema1};
    private @DrawableRes int bgSend, bgCrear, bgLletres, bgFons;
    private @ColorInt int color;

    public static Tema getRandomTema(){
        Random r = new Random();
        return temas[r.nextInt(temas.length)];
    }

    public Tema(@DrawableRes int bgSend, @DrawableRes int bgCrear, @DrawableRes int bgLletres, @DrawableRes int bgFons, @ColorInt int color) {
        this.bgSend = bgSend;
        this.bgCrear = bgCrear;
        this.bgLletres = bgLletres;
        this.bgFons = bgFons;
        this.color = color;
    }

    /**
     * Configura els views amb la configuració del tema
     * @param app
     */
    public void applyTema(AppCompatActivity app){
        app.findViewById(R.id.buttonClear).setBackground(app.getDrawable(this.bgCrear));
        app.findViewById(R.id.buttonSend).setBackground(app.getDrawable(this.bgSend));
        app.findViewById(R.id.lluna).setBackground(app.getDrawable(this.bgLletres));
        app.findViewById(R.id.parentConstraint).setBackground(app.getDrawable(this.bgFons));
    }

    public @ColorInt int getColor(){
        return color;
    }
}
