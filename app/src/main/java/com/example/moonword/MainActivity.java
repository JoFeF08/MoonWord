package com.example.moonword;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.AlertDialog;
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

        crearFilaTextViews(R.id.hiddenWordsConstraint, 5);
    }

    public void setLletra(View v){
        Button btn =  (Button) v;
        String lletra = btn.getText().toString();
        System.out.println("D: lletra:"+lletra);
        //btn.setEnabled(!btn.isEnabled());
    }

    public TextView[] crearFilaTextViews(int guia, int lletres){
        ConstraintLayout layout = findViewById(R.id.hiddenWordsConstraint);

        TextView textViews[] = new TextView[lletres];
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        int lastTextViewId = guia; // La primera vista debe estar debajo de la Guideline

        // Iterar para crear y posicionar cada TextView
        for (int i = 0; i < lletres; i++) {
            TextView textView = new TextView(this);
            textView.setId(View.generateViewId()); // Generar ID único para el TextView

            // Configurar propiedades del TextView (puedes personalizar según necesites)
            textView.setText("TextView " + (i + 1));
            textView.setTextSize(16);

            // Agregar TextView al ConstraintLayout
            layout.addView(textView);

            // Conectar el TextView a la Guideline y configurar restricciones
            constraintSet.connect(textView.getId(), ConstraintSet.TOP, lastTextViewId, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(textView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
            constraintSet.connect(textView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

            // Aplicar restricciones al TextView
            lastTextViewId = textView.getId();

            // Agregar TextView al array de TextViews
            textViews[i] = textView;
            System.out.println(textViews[i].getId() + " "+textViews[i].getX() + ", "+textViews[i].getY());
        }
        constraintSet.applyTo(layout);
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