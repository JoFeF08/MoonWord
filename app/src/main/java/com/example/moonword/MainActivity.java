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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
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
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int PERMISSIONS_REQUEST_CODE = 200;
    private String[] permissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.USE_FINGERPRINT
    };

    private Button[] charButtons = new Button[7];
    private TextView textViewIntent;
    private TextView textCont;
    private  int num_boto;

    private int contadorCorrecte;

    private TextView[][] hiddenWords = new TextView[5][];
    /**
     * controla cuantas filas de palabras hay en un momento dado
     */

    private Game currentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        int interficie = View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(interficie);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //
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

        startGame();
    }

    /**
     * Inicialitza el joc
     */
    private void startGame(){
        this.currentGame = new Game(Game.random.nextInt(4)+4);
        contadorCorrecte = 0 ;
        actulaitzarTextContador(null);
        System.out.println("GAME: "+ currentGame.getTamLLetraMax());
        clearSetButtons();
        num_boto = 0;
        for (Map.Entry<Character, Integer> entry : currentGame.getSetChars().entrySet()) {
            Character character = entry.getKey();
            int count = entry.getValue();

            for (int i = 0; i < count; i++) {
                charButtons[num_boto].setText(character.toString());
                num_boto++;
            }
        }
        clearIntento();
        botons_aleatoris();
        //borrar anteriors
        for(int i=0;i<hiddenWords.length;i++){
            if(hiddenWords[i]==null){
                break;
            }
            for(int j=0;j<hiddenWords[i].length;j++){
                if(hiddenWords[i][j]==null){
                    break;
                }
                ((ConstraintLayout)hiddenWords[i][j].getParent()).removeView(hiddenWords[i][j]);
            }
            hiddenWords[i]=null;
        }
        //crear nous
        for (int i = 0; i < hiddenWords.length; i++) {
            hiddenWords[i]=crearFilaTextViews(R.id.ref15H, i+Game.random.nextInt(3)+1, i);
        }

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
        for (int i = 0; i < s.length() && i<hiddenWords[posicio].length; i++) {
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

    }

    public void btnSend(View v){
        Button btn = (Button)v;
        System.out.println("D: pulsado send");
        String intro = (String) textViewIntent.getText();
        actulaitzarTextContador(intro);

        clearIntento();
    }

    private void  actulaitzarTextContador(String p){
        String imprimir = "";
        boolean conte = false;
        HashMap<Integer, HashSet<String>> paraules = currentGame.getMapNumSol();
        TreeSet<String> trobades = currentGame.getSetFoundWords();


        if(p != null) {
            if (trobades.contains(p)) {
                conte = true;
            } else if (conteHmapHsetS(paraules,p)){
                currentGame.getSetFoundWords().add(p);
                contadorCorrecte++;
            }

            StringBuilder text = new StringBuilder();

            for (String paraula : currentGame.getSetFoundWords()) {
                String sParaula;
                if (paraula.equals(p) && conte) {
                    sParaula = "<font color='red'>" + paraula + "</font>, ";
                } else {
                    sParaula = paraula + ", ";
                }
                text.append(sParaula);
            }

            // Construct the output message with the list of words
            imprimir = "Has encertat (" + contadorCorrecte + "/" + currentGame.getNumTotalW() + "): " + text.toString();

        }else if (p == null) {
            imprimir = "Has encertat (" + contadorCorrecte + "/" + currentGame.getNumTotalW() + "): ";
        } else {
            imprimir = (String) textCont.getText();
        }

            textCont.setText(Html.fromHtml(imprimir, Html.FROM_HTML_MODE_LEGACY));
    }

    private boolean conteHmapHsetS(HashMap<Integer, HashSet<String>> paraules, String p) {
        for (Map.Entry<Integer, HashSet<String>> entry : paraules.entrySet()) {
            HashSet<String> set = entry.getValue();
            if (set.contains(p)) {
                return true;
            }
        }
        return false;
    }

    private boolean conteTreeS(HashMap<Integer, HashSet<String>> paraules, String p) {
        for (Map.Entry<Integer, HashSet<String>> entry : paraules.entrySet()) {
            HashSet<String> set = entry.getValue();
            if (set.contains(p)) {
                return true;
            }
        }
        return false;
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

    private void clearSetButtons() {
        for(Button b:charButtons) {
            b.setEnabled(true);
            b.setText("");
        }
    }

    @SuppressLint("SetTextI18n")
    public void btnRandom(View v){
        Button btn = (Button)v;
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
        enableViews(R.id.parentConstraint);
        Button btn = (Button)v;
        System.out.println("D: pulsado restart");
        mostrarMissatge("REINICIAR", true);
        nextTheme();
        startGame();
        clearIntento();
    }

    private void mostrarMissatge(String s, boolean llarg){
        Context context = getApplicationContext ();
        int duration = llarg ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, s, duration) ;
        toast.show();
    }

    private void nextTheme(){
        System.out.println("siguiente tema");
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