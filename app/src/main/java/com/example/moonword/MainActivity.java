package com.example.moonword;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.AlertDialog;
import android.graphics.fonts.FontStyle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        int interficie = View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(interficie);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        crearFilaTextViews(R.id.ref15H, 7);
    }

    public void setLletra(View v){
        Button btn =  (Button) v;
        String lletra = btn.getText().toString();
        System.out.println("D: lletra:"+lletra);
        //btn.setEnabled(!btn.isEnabled());
    }

    public TextView[] crearFilaTextViews(@IdRes int guia, int lletres){
        ConstraintLayout layout = findViewById(R.id.hiddenWordsConstraint);

        TextView textViews[] = new TextView[lletres];


        int lastTextViewId = -1;

        // Iterar para crear y posicionar cada TextView
        for (int i = 0; i < lletres; i++) {
            TextView textView = new TextView(this);
            textView.setId(View.generateViewId()); // Generar ID único para el TextView
            int id = textView.getId();
            textView.setText(""+i);
            textView.setTextSize(32);

            layout.addView(textView);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(layout);
            constraintSet.constrainWidth(id, 60);
            constraintSet.constrainHeight(id, 120);
            constraintSet.connect(id, ConstraintSet.TOP, guia, ConstraintSet.BOTTOM, 10);
            if(i>=1){
                constraintSet.connect(id, ConstraintSet.LEFT, lastTextViewId, ConstraintSet.RIGHT, 10);
            }else{

            }
            //constraintSet.centerHorizontally(id, ConstraintSet.PARENT_ID);
            constraintSet.applyTo(layout);

            textViews[i] = textView;
            lastTextViewId = id;
        }

        return textViews;
    }


    public void btnClear(View v){
        Button btn = (Button)v;
        System.out.println("D: pulsado clear");
    }

    public void btnSend(View v){
        Button btn = (Button)v;
        System.out.println("D: pulsado send");
    }

    public void btnRandom(View v){
        Button btn = (Button)v;
        System.out.println("D: pulsado Random");
    }

    public void btnBonus(View v){
        Button btn = (Button)v;
        System.out.println("D: pulsado bonus");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" < encertades i possibles >");
        builder.setMessage(" < la llista de trobades >");
        // Un botó OK per tancar la finestra
        builder.setPositiveButton(" OK ",null);
        // Mostrar l ’ AlertDialog a la pantalla
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void btnHint(View v){
        Button btn = (Button)v;
        System.out.println("D: pulsado ayuda");
    }

    public void btnRestart(View v){
        Button btn = (Button)v;
        System.out.println("D: pulsado mezcla");
    }
}