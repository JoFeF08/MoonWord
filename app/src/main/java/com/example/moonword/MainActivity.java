package com.example.moonword;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.FontStyle;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button[] charButtons = new Button[7];
    private TextView textViewIntent;

    private TextView[][] hiddenWords = new TextView[5][];
    /**
     * controla cuantas filas de palabras hay en un momento dado
     */
    private int currentDepthHiddenWords = 0;

    private Game currentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        int interficie = View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(interficie);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getCharButtons();
        textViewIntent = findViewById(R.id.textViewIntent);
        textViewIntent.setText("");

        for (int i = 0; i < 5; i++) {
            hiddenWords[currentDepthHiddenWords]=crearFilaTextViews(R.id.ref15H, i+3);
            currentDepthHiddenWords++;
        }
        mostraParaula ("pac ", 1) ;
        mostraParaula ("t nt ", 2) ;
        mostraPrimeraLletra ("de lu ", 3) ;
        mostraLletraPosicio("de-llu ", 3,4);
        mostraLletraPosicio("de-llu ", 3,3);
        mostraLletraPosicio("de-llu ", 3,2);
        mostraParaula ("cma  r-", 4) ;
        //test


    }

    /**
     * Inicialitza el joc
     */
    private void startGame(){
        this.currentGame = new Game(7);
        this.currentDepthHiddenWords=0;

    }

    /**
     * Onclick dels botons de lletres
     * @param v
     */
    public void setLletra(View v){
        Button btn =  (Button) v;
        String lletra = btn.getText().toString();
        System.out.println("D: lletra:"+lletra);
        btn.setEnabled(false);
        textViewIntent.setText(textViewIntent.getText().toString()+btn.getText());
    }

    /**
     * Genera una fila de una paraula oculta
     * @param guia
     * @param lletres
     * @return
     */
    public TextView[] crearFilaTextViews(@IdRes int guia, int lletres){
        ConstraintLayout layout = findViewById(R.id.parentConstraint);

        TextView textViews[] = new TextView[lletres];

        int lastTextViewId = -1;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int width = 120;
        final int height = 180;
        final int padding = 10;
        final int offsetMargin = displayMetrics.widthPixels/2 - (((width*lletres)+padding*(lletres-1))/2) - (int)layout.getX();
        System.out.println("MARGIN :"+offsetMargin);
        // Iterar para crear y posicionar cada TextView
        for (int i = 0; i < lletres; i++) {
            TextView textView = new TextView(this); //1
            textView.setId(View.generateViewId()); // Generar ID único para el TextView
            int id = textView.getId();
            textView.setText("");
            textView.setTextSize(52);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setBackgroundColor(Color.parseColor("#53acac"));
            layout.addView(textView); //2

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(layout); //
            constraintSet.constrainWidth(id, width);
            constraintSet.constrainHeight(id, height);

            constraintSet.connect(id, ConstraintSet.TOP, guia, ConstraintSet.BOTTOM, (height+padding)*currentDepthHiddenWords);
            if(i==0){
                constraintSet.connect(id, ConstraintSet.START, layout.getId(), ConstraintSet.START, offsetMargin);
            }else{
                constraintSet.connect(id, ConstraintSet.START, lastTextViewId, ConstraintSet.END, padding);
            }

            //constraintSet.centerVertically(id, layout.getId());
            constraintSet.applyTo(layout);

            textViews[i] = textView;
            lastTextViewId = id;
        }
        return textViews;
    }

    private void mostraParaula ( String s , int posicio ){
        TextView[] panells = hiddenWords[posicio];
        char[] lletres = s.toUpperCase().toCharArray();
        for (int i = 0; i < s.length(); i++) {
            panells[i].setText(""+lletres[i]);
        }

    }

    private void mostraPrimeraLletra ( String s , int posicio ){
        mostraLletraPosicio(s,posicio,0);
    }

    private void mostraLletraPosicio ( String s , int posicio, int n_lletra){
        TextView[] panells = hiddenWords[posicio];
        char[] lletres = s.toUpperCase().toCharArray();
        panells[n_lletra].setText(""+lletres[n_lletra]);
    }


    private void enableViews (int parent ){
        ConstraintLayout lay = findViewById(parent);
        int nunFills = lay.getChildCount();
        for (int i = 0; i < nunFills; i++) {
            View v =  lay.getChildAt(i);
            if(v.getId() == R.id.layButLletres || v.getId() == R.id.layEscritura){
                enableViews(v.getId());
            }
            v.setEnabled(true);
        }
        lay.setEnabled(true);
    }
    private void disableViews (int parent ){
        ConstraintLayout lay = findViewById(parent);
        int nunFills = lay.getChildCount();
        for (int i = 0; i < nunFills; i++) {
            View v =  lay.getChildAt(i);
            if(v.getId() == R.id.layButLletres || v.getId() == R.id.layEscritura) {
                disableViews(v.getId());
            }else if(v.getId() != R.id.bonusButton && v.getId() != R.id.restartButton) {
                v.setEnabled(false);
            }
        }
        lay.setEnabled(false);
    }


    public void btnClear(View v){
        Button btn = (Button) v;
        System.out.println("D: pulsado clear");

        clearIntento();
        disableViews (R.id.parentConstraint);
    }

    public void btnSend(View v){
        Button btn = (Button)v;
        System.out.println("D: pulsado send");

        clearIntento();
    }

    /**
     * Rehabilita els botons de lletra i neteja l'intent actual
     */
    private void clearIntento() {
        for(Button b:charButtons){
            b.setEnabled(true);
        }
        textViewIntent.setText("");
    }

    @SuppressLint("SetTextI18n")
    public void btnRandom(View v){
        Button btn = (Button)v;
        System.out.println("D: pulsado Random");

        //un solo for sobre los botones?
        char[] letras = new char[7]; //depende tamaño letra max!!
        for (int i = 0; i < letras.length; i++) {
            letras[i] = charButtons[i].getText().charAt(0);
        }
        for (int i = letras.length-1; i>1 ; i--) {
            int j = Game.random.nextInt(i-1);
            char aux = letras[i];
            letras[i]=letras[j];
            letras[j]=aux;
        }
        for (int i = 0; i < letras.length; i++) {
            charButtons[i].setText(""+letras[i]);
        }

        clearIntento();
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
        mostrarMissatge("AYUDA", false);
    }

    public void btnRestart(View v){
        Button btn = (Button)v;
        System.out.println("D: pulsado restart");
        mostrarMissatge("REINICIAR", true);
        clearIntento();

        enableViews(R.id.parentConstraint);
    }

    private void mostrarMissatge(String s, boolean llarg){
        Context context = getApplicationContext ();
        int duration = llarg ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, s, duration) ;
        toast.show ();
    }

    private void getCharButtons(){
        ConstraintLayout layout = findViewById(R.id.layButLletres);
        int j=0;
        for(int i=0;i<layout.getChildCount();i++){
            if(layout.getChildAt(i) instanceof Button){
                Button btn = (Button) layout.getChildAt(i);
                btn.setText(""+Game.abecedari[Game.random.nextInt(Game.abecedari.length)]); //PLACEHOLDER
                charButtons[j++] = btn;
            }
        }
    }
}