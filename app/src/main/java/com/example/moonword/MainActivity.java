package com.example.moonword;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int PERMISSIONS_REQUEST_CODE = 200;
    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };

    private Button[] charButtons = new Button[7];
    private TextView textViewIntent;
    private Button bonusButton ;
    private TextView textCont;
    private  int num_boto;

    private String imprimir;

    private TextView[][] hiddenWords = new TextView[5][];
    /**
     * controla cuantas filas de palabras hay en un momento dado
     */

    private Game currentGame;
    private Tema currentTema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        int interficie = View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(interficie);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Verificar si todos los permisos están concedidos
        if (!PermissionUtils.arePermissionsGranted(this, permissions)) {
            // Si no están concedidos, solicitar los permisos al usuario
            PermissionUtils.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
        } else {
            // Todos los permisos están concedidos, continuar con la lógica de la aplicación
            // Por ejemplo, iniciar una función que requiere permisos
            // doSomethingWithPermissions();
        }
        checkCameraPermission();


        DictReader.configure(this);
        try {
            DictReader.loadAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getCharButtons();
        textViewIntent = findViewById(R.id.textViewIntent);
        textCont = findViewById(R.id.TextViewContador);
        bonusButton = findViewById(R.id.bonusButton);
        bonusButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                afegirBonus(777);
                return true;
            }
        });


        nextTheme();
        startGame();
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
    public TextView[] crearFilaTextViews(@IdRes int guia, int lletres, int currentDepthHiddenWords){
        ConstraintLayout layout = findViewById(R.id.parentConstraint);

        TextView textViews[] = new TextView[lletres];

        int lastTextViewId = -1;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int width = 120;
        final int height = 180;
        final int padding = 10;
        final int offsetMargin = displayMetrics.widthPixels/2 - (((width*lletres)+padding*(lletres-1))/2) - (int)layout.getX();

        // Iterar para crear y posicionar cada TextView
        for (int i = 0; i < lletres; i++) {
            TextView textView = new TextView(this); //1
            textView.setId(View.generateViewId()); // Generar ID único para el TextView
            int id = textView.getId();
            textView.setText("");
            textView.setTextSize(52);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setBackgroundColor(currentTema.getColor());
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



    private void mostraPrimeraLletra( String s , int posicio, boolean majuscula){
        mostraLletraPosicio(s,posicio,0, majuscula);
    }

    private void mostraLletraPosicio ( String s , int posicio, int n_lletra, boolean majuscula){
        TextView[] panells = hiddenWords[posicio];

        char lletra;
        if (majuscula){
            lletra= s.toUpperCase().charAt(n_lletra);
        } else{
            lletra= s.toLowerCase().charAt(n_lletra);
        }

        panells[n_lletra].setText(""+lletra);
    }


    private void enableViews (int parent){
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
    private void disableViews (@IdRes int parent ){
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


    private void nextTheme(){
        currentTema = Tema.getRandomTema();
        currentTema.applyTema(this);
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

//----------------------------------------------------AUXILIARS-----------------------------------------------------------
/**
 * Mostra el missatge pasat per parametre String
 * */
    private void mostrarMissatge(String s, boolean llarg){
    Context context = getApplicationContext ();
    int duration = llarg ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;

    Toast toast = Toast.makeText(context, s, duration) ;
    toast.show();
}


//----------------------------------------------------STAR GAME-----------------------------------------------------------
    /**
     * Inicialitza el joc
     */
    private void startGame(){
        int carryBonus;
        if (this.currentGame == null){
            carryBonus = 0;
        }else{
            carryBonus = this.currentGame.getContadorBonus();
        }
        this.currentGame = new Game(Game.random.nextInt(4)+4); //entre 7 y 3

        this.currentGame.setContadorBonus(carryBonus);
        bonusButton.setText(carryBonus + "");

        Log.i("START_GAME()", currentGame.toString());

        num_boto = 0;
        Iterator<Map.Entry<Character, Integer>> iterator = currentGame.getMapChars().entrySet().iterator();

        //Inicialitazació botons cercle
        while (iterator.hasNext()) {
            Map.Entry<Character, Integer> entry = iterator.next();
            Character character = entry.getKey();
            int count = entry.getValue();

            for (int i = 0; i < count; i++) {
                charButtons[num_boto].setEnabled(true);
                charButtons[num_boto].setText(character.toString());

                num_boto++;
            }
        }

        //deshabilitar botons no usats
        for (int i=num_boto; i<charButtons.length;i++){
            charButtons[i].setText("");
            charButtons[i].setEnabled(false);
        }

        inicialitzaBotonsiContadors();
        botons_aleatoris();

        //borrar anteriors paraules cuadro
        for(int i=0; i < hiddenWords.length; i++){
            if(hiddenWords[i]==null){
                break;
            }
            for(int j=0; j < hiddenWords[i].length; j++){
                if(hiddenWords[i][j]==null){
                    break;
                }
                ((ConstraintLayout)hiddenWords[i][j].getParent()).removeView(hiddenWords[i][j]);
            }
            hiddenWords[i]=null;
        }
        //crear nous
        Iterator<String> iterSols = currentGame.getMapWordsSol().keySet().iterator();
        for (int i = 0; i < hiddenWords.length && i<currentGame.getCurrentParaulesN(); i++) {
            hiddenWords[i]=crearFilaTextViews(R.id.ref15H, iterSols.next().length(), i);
        }

    }

    private void inicialitzaBotonsiContadors(){
        currentGame.setContadorCorrecte(0);
        actulaitzarTextContador(null);
        clearIntento();
        enableViews(R.id.parentConstraint);
    }


    //--------------------------------------BOTONS--------------------------------------------------------------------------
    //-----------------------------------------------RANDOM-----------------------------------------------------------------
    @SuppressLint("SetTextI18n")
    public void btnRandom(View v){
        System.out.println("D: pulsado Random");

        botons_aleatoris();
    }

    private void botons_aleatoris(){
        char[] letras = new char[num_boto];
        for (int i = 0; i < letras.length; i++) {
            letras[i] = charButtons[i].getText().charAt(0);
        }
        for (int i = letras.length-1; i>1 ; i--) {
            int j = Game.random.nextInt(i-1);
            char aux = letras[i];
            letras[i] = letras[j];
            letras[j] = aux;
        }
        for (int i = 0; i < letras.length; i++) {
            charButtons[i].setText(""+letras[i]);
        }

        clearIntento();
    }

//-----------------------------------------------RESTART-----------------------------------------------------------------
    public void btnRestart(View v){
        System.out.println("D: pulsado restart");
        mostrarMissatge("REINICIAR", true);

        nextTheme();
        startGame();

    }

    //-----------------------------------------------HELP-----------------------------------------------------------------

    public void btnHint(View v){
        System.out.println("D: pulsado ayuda");


        if(currentGame.getContadorBonus() >= 5){
            mostrarMissatge("AJUDA", false);
            currentGame.setContadorBonus(currentGame.getContadorBonus()-5);
            bonusButton.setText(String.valueOf(currentGame.getContadorBonus()));
            mostraPrimeraLletraAleatori();
            if(!currentGame.bonusDisponible()){
                findViewById(R.id.ajudaButton).setEnabled(false);
            }

        }else{
            mostrarMissatge("NECESSITES ALMENYS 5 BONUS", false);
        }

    }

    private void mostraPrimeraLletraAleatori(){
        int pos = currentGame.getParaulaPosAjuda();
        Iterator<String> paraules = currentGame.getMapWordsSol().keySet().iterator();
        for (int i = 1; i < pos; i++) {
            paraules.next();
        }
        String p = paraules.next();

        mostraPrimeraLletra(p,pos, false);
    }

    //-----------------------------------------------BONUS-----------------------------------------------------------------
    public void btnBonus(View v){
        System.out.println("D: pulsado bonus");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String message = "Punts de bonus: " + currentGame.getContadorBonus();
        builder.setTitle(message);

        builder.setMessage(Html.fromHtml(imprimir, Html.FROM_HTML_MODE_LEGACY));

        // Un botó OK per tancar la finestra
        builder.setPositiveButton(" OK ",null);

        // Mostrar l ’ AlertDialog a la pantalla
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //-----------------------------------------------SEND-------------------------------------------------------------------
    public void btnSend(View v){
        System.out.println("D: pulsado send");

        String intro = (String) textViewIntent.getText();
        actulaitzarTextContador(intro);

        clearIntento();

        if(currentGame.hasWon()){
            Log.i("GAMELOOP", "HAS GUANYAT");
            mostrarMissatge("Enhorabona! Has guanyat", true);

            disableViews(R.id.parentConstraint);

        }
    }

    private void  actulaitzarTextContador(String p){
        boolean conte = false;

        if(p != null && !p.equals("")) {

            if (currentGame.conteParaulesEncertades(p)) {
                conte = true;
                mostrarMissatge("Aquesta ja la tens", false);

            } else if (currentGame.conteParaulesPossibles(p)){
                String missatge = "Paraula vàlida!";

                currentGame.getSetFoundWords().add(p);
                currentGame.setContadorCorrecte(currentGame.getContadorCorrecte()+1);

                if(currentGame.conteParaulesAmagades(p)){
                    int pos = currentGame.foundHidden(p);
                    mostraParaula(p,pos);
                    Log.d("GAMELOOP", "p: " + p + "  pos: "+pos);
                }else{
                    afegirBonus(1);
                    missatge += " Tens un bonus";
                }
                mostrarMissatge(missatge, false);


            }else{
                mostrarMissatge("Paraula NO vàlida", false);
            }

            imprimir = "Has encertat (" + currentGame.getContadorCorrecte() + "/" + currentGame.getNumTotalW() + "): " +   generaText(conte, p).toString();

        }else if (p == null) {
            imprimir = "Has encertat (" + currentGame.getContadorCorrecte() + "/" + currentGame.getNumTotalW() + "): ";
        }

        textCont.setText(Html.fromHtml(imprimir, Html.FROM_HTML_MODE_LEGACY));
    }

    private void afegirBonus(int n) {
        currentGame.setContadorBonus(currentGame.getContadorBonus() + n);
        bonusButton.setText(String.valueOf(currentGame.getContadorBonus()));
    }


    private StringBuilder generaText(boolean conte, String p){
        StringBuilder text = new StringBuilder();

        Iterator<String> iterator = currentGame.getSetFoundWords().iterator();
        boolean no_primera = false;

        while (iterator.hasNext()) {
            String paraula = iterator.next();
            String sParaula= "";

            if (no_primera){
                sParaula=", ";
            }
            no_primera = true;

            if (paraula.equals(p) && conte) {
                sParaula += "<font color='red'>" + DictReader.getMapAllWords().get(paraula) + "</font>";

            } else {
                sParaula += DictReader.getMapAllWords().get(paraula);
            }
            text.append(sParaula);
        }
    return text;
    }

    private void mostraParaula ( String s , int posicio ){
        TextView[] panells = hiddenWords[posicio];
        char[] lletres = DictReader.getParaulaAccent(s).toUpperCase().toCharArray();
        for (int i = 0; i < s.length() && i<hiddenWords[posicio].length; i++) {
            if(!panells[i].getText().toString().isEmpty()){
                continue;
            }
            panells[i].setText(""+lletres[i]);
        }
    }

    //-----------------------------------------------CLEAR-------------------------------------------------------------------
    public void btnClear(View v){
        System.out.println("D: pulsado clear");

        clearIntento();
    }

    /**
     * Rehabilita els botons de lletra i neteja l'intent actual
     */
    private void clearIntento() {
        for(Button b:charButtons){
            b.setEnabled(!b.getText().equals(""));
        }
        textViewIntent.setText("");
    }

    //------------------------------------------------------------------PERMISOS---------------------------------------------
// Verificar y solicitar permisos de cámara
private void checkCameraPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA_PERMISSION);
    } else {
        // Permiso ya concedido, puedes abrir la cámara
    }
}
}

    class PermissionUtils {

        // Método para comprobar si todos los permisos especificados están concedidos
        public static boolean arePermissionsGranted(Context context, String[] permissions) {
            // Verificar para dispositivos con Android 6.0 (API 23) o superior, ya que los permisos se solicitan dinámicamente desde esta versión
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (String permission : permissions) {
                    // Verificar si el permiso actual no está concedido
                    if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        // Si alguno de los permisos no está concedido, retornar false
                        return false;
                    }
                }
            }
            // Si todos los permisos están concedidos, retornar true
            return true;
        }

        // Método para solicitar permisos al usuario si no están concedidos
        public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
            // Listar permisos que necesitan ser solicitados
            List<String> permissionsToRequest = new ArrayList<>();

            // Verificar para cada permiso si no está concedido
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    // Agregar permiso a la lista de permisos a solicitar
                    permissionsToRequest.add(permission);
                }
            }

            // Convertir lista de permisos a un array de Strings
            String[] permissionsArray = new String[permissionsToRequest.size()];
            permissionsArray = permissionsToRequest.toArray(permissionsArray);

            // Solicitar permisos al usuario (solo si hay permisos que solicitar)
            if (permissionsArray.length > 0) {
                ActivityCompat.requestPermissions(activity, permissionsArray, requestCode);
            }
        }
    }
