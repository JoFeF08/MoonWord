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
import android.util.DisplayMetrics;
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

        crearFilaTextViews(R.id.ref15H, 15);
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int width = 60;
        final int offsetMargin = displayMetrics.widthPixels/2 - (width*lletres/2) - (int)layout.getX();
        System.out.println("MARGIN :"+offsetMargin);
        // Iterar para crear y posicionar cada TextView
        for (int i = 0; i < lletres; i++) {
            TextView textView = new TextView(this);
            textView.setId(View.generateViewId()); // Generar ID único para el TextView
            int id = textView.getId();
            textView.setText(""+i);
            textView.setTextSize(32);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            layout.addView(textView);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(layout);
            constraintSet.constrainWidth(id, width);
            constraintSet.constrainHeight(id, 120);

            if(i==0){
                constraintSet.connect(id, ConstraintSet.START, layout.getId(), ConstraintSet.START, offsetMargin);
            }else{
                constraintSet.connect(id, ConstraintSet.START, lastTextViewId, ConstraintSet.END, 0);
            }

            //constraintSet.centerVertically(id, layout.getId());
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