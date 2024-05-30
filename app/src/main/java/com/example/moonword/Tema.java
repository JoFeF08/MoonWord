package com.example.moonword;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Tema {

    private static Tema tema1 = new Tema(R.drawable.terra, R.drawable.jupiter, R.drawable.lluna, R.drawable.fons1, Color.parseColor("#4DD0E1"));
    private static Tema tema2 = new Tema(R.drawable.jupiter, R.drawable.lluna, R.drawable.terra, R.drawable.fons1, Color.parseColor("#FFF176"));
    private static Tema tema3 = new Tema(R.drawable.lluna, R.drawable.terra, R.drawable.jupiter, R.drawable.fons1, Color.parseColor("#81C784"));

    private static Tema nadal1 = new Tema(R.drawable.terra, R.drawable.jupiter, R.drawable.lluna, R.drawable.nadal, Color.parseColor("#228B22"));
    private static Tema nadal2 = new Tema(R.drawable.terra, R.drawable.jupiter, R.drawable.lluna, R.drawable.nadal, Color.parseColor("#B0E0E6"));
    private static Tema nadal3 = new Tema(R.drawable.terra, R.drawable.jupiter, R.drawable.lluna, R.drawable.nadal, Color.parseColor("#B22222"));

    private static Tema destrc1 = new Tema(R.drawable.terra, R.drawable.jupiter, R.drawable.lluna, R.drawable.destrucio, Color.parseColor("#898937"));
    private static Tema destrc2 = new Tema(R.drawable.terra, R.drawable.jupiter, R.drawable.lluna, R.drawable.destrucio, Color.parseColor("#4c5146"));
    private static Tema destrc3 = new Tema(R.drawable.terra, R.drawable.jupiter, R.drawable.lluna, R.drawable.destrucio, Color.parseColor("#c0c052"));

    private static Tema hallo1 = new Tema(R.drawable.terra, R.drawable.jupiter, R.drawable.lluna, R.drawable.hallo, Color.parseColor("#FF8C00"));
    private static Tema hallo2 = new Tema(R.drawable.terra, R.drawable.jupiter, R.drawable.lluna, R.drawable.hallo, Color.parseColor("#8B0000"));
    private static Tema hallo3 = new Tema(R.drawable.terra, R.drawable.jupiter, R.drawable.lluna, R.drawable.hallo, Color.parseColor("#F8F8FF"));
    private static Tema[] temas = {tema1, tema2, tema3, nadal1, nadal2, nadal3, destrc1,destrc2,destrc3, hallo1, hallo2,hallo3};
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
        ((ImageView)app.findViewById(R.id.lluna)).setImageResource(bgLletres);
        app.findViewById(R.id.parentConstraint).setBackground(app.getDrawable(this.bgFons));
    }

    public @ColorInt int getColor(){
        return color;
    }
}
