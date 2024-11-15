package com.example.moonword;

import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import SetMap.Map.MapInterficie;
import SetMap.Map.UnsortedArrayMap;
import SetMap.Set.Pair;

public class Game {

    /**
     * static de moment, idea de instanciar Game, per joc
     */
    public static final char[] abecedari = "abcdefghijklmnopqrstuvwxyzç".toCharArray();
    public static final Random random = new Random();

    //map tamany paraula -> set de paraula
    private HashMap<Integer, HashSet<String>> mapNumSol;
    //map paraula -> posició mostrar
    private TreeMap<String, Integer> mapWordsSol;
    //set paraules trobades
    private TreeSet<String> setFoundWords;

    //map Char -> Integer | Lletra -> nº aparicions
    private HashMap<Character, Integer> mapChars = new HashMap<>();

    private HashSet<Integer> setParaulesBonus;

//numTotalW cantidad de paraules que es poden escriure amb les lletres seleccionades
//contadorBonus catidad de punts bonus acumulats
//numTotalW cantidad de paraules encertades
    private int tamLLetraMax, currentParaulesN, numTotalW, contadorBonus, paraulesSenseBonusN, contadorCorrecte;


    //*******************************GETTERS SETTERS************************************************
    public int getContadorCorrecte() {
        return contadorCorrecte;
    }

    public void setContadorBonus(int contadorBonus) {
        this.contadorBonus = contadorBonus;
    }

    public void setContadorCorrecte(int contadorCorrecte) {
        this.contadorCorrecte = contadorCorrecte;
    }

    //GETTERS SETTERS
    public HashMap<Character, Integer> getMapChars() {
        return mapChars;
    }

    public int getTamLLetraMax() {
        return tamLLetraMax;
    }

    public int getContadorBonus() {
        return contadorBonus;
    }

    public int getNumTotalW() {
        return numTotalW;
    }

    public TreeMap<String, Integer> getMapWordsSol(){
        return this.mapWordsSol;
    }

    public int getCurrentParaulesN() {
        return currentParaulesN;
    }
    public void setCurrentParaulesN(int n){
        this.currentParaulesN = n;
    }


    public HashMap<Integer, HashSet<String>> getMapNumSol() {
        return mapNumSol;
    }

    public TreeSet<String> getSetFoundWords() {
        return setFoundWords;
    }
//-----------------------------------------CONSTRUCTOR------------------------------------------------
    public Game(int tamLletraMax){
        this.tamLLetraMax = tamLletraMax;

        //obtenir paraula aleatoria tamany
        Set<String> paraulesMax = DictReader.getMapNumWords().get(tamLletraMax);
        int tamSet = DictReader.getSizeNumWords(tamLletraMax);
        int sel = random.nextInt(tamSet);
        Iterator<String> iter = paraulesMax.iterator();
        Log.d("GAME_INIT", "selRandom: "+sel+", size:" + paraulesMax.size()+",  tamSet:"+tamSet+", l:"+tamLletraMax);
        for(int i=0;i<sel-1;i++){
            iter.next();
        }
        String pSel = iter.next();
        System.out.println("GAME: paraula molde "+pSel);

        //initcializar sets i maps
        mapChars = new HashMap<>();
        addStringToMap(pSel, mapChars);
        setFoundWords = new TreeSet<>();
        mapNumSol = new HashMap<>();
        mapWordsSol = new TreeMap<>(new StringLengthComparator());
        TreeMap<Integer, Integer> contadorsMapNumSol = new TreeMap<>();

        System.out.println("setChars="+ mapChars);

        //emplenar totes les paraules posibles
        numTotalW = 0;

        for (int i = 3; i <= pSel.length(); i++) {
            HashSet<String> aux = new HashSet<>();
            HashSet<String> allW = DictReader.getMapNumWords().get(i);

            int numNumSol = 0;

            //usa el iterator
            Iterator<String> iterAllW = allW.iterator();
            while (iterAllW.hasNext()) {
                String s = iterAllW.next();
                if (esParaulaSolucio(pSel, s)) {
                    if(aux.add(s)) {
                        //Calcular nombre de paraules possibles amb les lletres
                        numTotalW++;
                        numNumSol++;
                    }
                }
            }
            System.out.println(aux);
            mapNumSol.put(i, aux);
            contadorsMapNumSol.put(i, numNumSol);
        }

        //Triar les 5 paraules solució
        int paraluesRestants = 5;
        int checkSize = 7;
        int acumulat = 1;
        TreeSet<String> auxSet = new TreeSet<>(new StringLengthComparator());

        while(paraluesRestants>0 && checkSize>2){
            //Log.d("SEL_LOOP", "paraulesRestants"+paraluesRestants+", checkSize:"+checkSize);
            HashSet<String> aux = mapNumSol.get((checkSize));
            if(aux == null || aux.isEmpty()){
                checkSize--;
                acumulat++;
                continue;
            }

            Set<Integer> valores = setNAleatorios(acumulat, contadorsMapNumSol.get(checkSize));
            //Log.d("SEL_LOOP", "valores: "+valores);
            Iterator<String> iterFind = aux.iterator();
            int i = 0;
            while(iter.hasNext() && !valores.isEmpty()) {
                String found = iterFind.next(); //comprobar no coger la misma
                if(valores.contains(i)){
                    //Log.d("GAME_INIT", "Paraula solució " + found);
                    auxSet.add(found);
                    paraluesRestants--;
                    acumulat--;
                    valores.remove(i);
                }
                i++;
            }
            checkSize--;
            acumulat++;
        }
        this.setParaulesBonus = new HashSet<>();

        this.currentParaulesN = 5-paraluesRestants;
        this.paraulesSenseBonusN = this.currentParaulesN;

        Iterator<String> auxSetIter = auxSet.iterator();
        for(int j=0;j<currentParaulesN;j++){
            this.mapWordsSol.put(auxSetIter.next(),j);
            this.setParaulesBonus.add(j);
        }



        //Log.d("GAME_INIT", "mapSolucions" + this.mapWordsSol+ " "+this.currentParaulesN);

    }
    private Set<Integer> setNAleatorios(int n, int max){
        //Log.d("NRAND", "n"+n+", max"+max);
        int c = 0;
        TreeSet<Integer> aux = new TreeSet<>();
        if(max<n){
            n=max;
        }
        while(c!=n){
            if(aux.add(random.nextInt(max)))
                c++;
        }

        return aux;
    }

    /**
     *
     * @param p1 molde
     * @param p2 comprovar
     * @return
     */
    public static boolean esParaulaSolucio(String p1, String p2){
        MapInterficie<Character, Integer> p1char = new UnsortedArrayMap<>(p1.length()),
                p2char = new UnsortedArrayMap<>(p2.length());
        addStringToMap(p1, p1char);
        addStringToMap(p2, p2char);
        Iterator<Pair<Character, Integer>> p2Iter = p2char.iterator();
        while(p2Iter.hasNext()){
            char c = p2Iter.next().getKey();

            if(p1char.get(c)==null || p2char.get(c).compareTo(p1char.get(c))>0){
                return false;
            }
        }
        return true;
    }

    //***************************MIRAR SI SON SOLUCIO***********************************************
    /**
     * Mira si p és una paraula de les amagades
     * */
    public boolean conteParaulesAmagades(String p){
        return this.mapWordsSol.containsKey(p);
    }
    /**
     * Mira si maWordsSol mapping amb totes les paraules possibles amb les lletres d'aquesta partida
     * */
    public boolean conteParaulesPossibles( String p) {
        HashSet<String> set = mapNumSol.get(p.length());
        if(set==null){
            return false;
        }
        return set.contains(p);
    }


    /**
     * Mira si maWordsSol mapping amb totes les paraules possibles amb les lletres d'aquesta partida
     * */
    public boolean conteParaulesEncertades( String p) {
        return setFoundWords.contains(p);
    }
//**************************************************************************************************

    public boolean hasWon(){
        return this.currentParaulesN==0;
    }

    public int getParaulaPosAjuda(){
        if(this.setParaulesBonus.isEmpty()){
            return -1;
        }
        Iterator<Integer> posSenseBonus = this.setParaulesBonus.iterator();
        Log.i("PARAULA_BONUS", "paraules sense bonus: "+this.setParaulesBonus);
        int r = random.nextInt(this.paraulesSenseBonusN);
        for (int i = 1; i < r; i++) {
            posSenseBonus.next();
        }

        int pos = posSenseBonus.next();
        this.setParaulesBonus.remove(pos);
        this.paraulesSenseBonusN--;
        Log.i("PARAULA_BONUS", "paraula pos a bonusear "+pos);

        return pos;
    }

    public boolean bonusDisponible(){
        return !this.setParaulesBonus.isEmpty();
    }

    /**
     * Retorna la posició de una paraula oculta, després o elimina i decrementa les paraules restants
     * @param w
     * @return
     */
    public int foundHidden(String w){
        this.currentParaulesN--;
        //una paraula encertada no ha de ser usada com a bonus
        //decrementar paraules sense bonus si no tenia bonus
        if(this.setParaulesBonus.remove(this.mapWordsSol.get(w))){
            this.paraulesSenseBonusN--;
        }
        return this.mapWordsSol.remove(w);
    }

    private static void addStringToMap(String p, MapInterficie<Character, Integer> map){
        for(char c:p.toCharArray()){
            if(map.get(c)==null){
                map.put(c,1);
            }else{
                map.put(c,map.get(c)+1);
            }
        }
    }
    private static void addStringToMap(String p, Map<Character, Integer> map){
        for(char c:p.toCharArray()){
            if(map.get(c)==null){
                map.put(c,1);
            }else{
                map.put(c,map.get(c)+1);
            }
        }
    }



    @Override
    public String toString(){
        String aux = "GAME: nºParaules sol "+ this.currentParaulesN+", tamanyMax " + this.tamLLetraMax+"\n"+
                "\tmapWordSol"+this.mapWordsSol;

        return aux;
    }
}
