package com.example.moonword;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void setLletra(View v){
        Button btn =  (Button) v;
        String lletra = btn.getText().toString();
        System.out.println("D: lletra:"+lletra);
        btn.setEnabled(!btn.isEnabled());
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
}