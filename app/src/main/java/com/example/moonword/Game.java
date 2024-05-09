package com.example.moonword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import SetMap.Map.MapInterficie;
import SetMap.Map.UnsortedArrayMap;
import SetMap.Set.Pair;
import SetMap.Set.SetInterficie;

public class Game {

    /**
     * static de moment, idea de instanciar Game, per joc
     */
    public static final char[] abecedari = "abcdefghijklmnopqrstuvwxyzç".toCharArray();
    public static final Random random = new Random();

    private HashMap<Integer, HashSet<String>> mapNumSol;
    private HashMap<String, Integer> mapWordsSol;
    private TreeSet<String> setFoundWords;

    private HashMap<Character, Integer> setChars = new HashMap<>();
    private int tamLLetraMax;


    public Game(int tamLletraMax){
        this.tamLLetraMax = tamLletraMax;

        //obtenir paraula aleatoria tamany
        Set<String> paraulesMax = DictReader.getMapNumWords().get(tamLletraMax);
        int tamSet = paraulesMax.size();
        int sel = random.nextInt(tamSet);
        Iterator<String> iter = paraulesMax.iterator();
        for(int i=0;i<sel-1;i++){
            iter.next();
        }
        String pSel = iter.next();
        System.out.println("GAME: paraula molde "+pSel);

        //initcializar sets i maps
        setChars = new HashMap<>();
        addStringToMap(pSel, setChars);
        setFoundWords = new TreeSet<>();
        mapNumSol = new HashMap<>();
        mapWordsSol = new HashMap<>();

        System.out.println("setChars="+setChars);

        //emplenar totes les paraules posibles
        for (int i = 3; i <= pSel.length(); i++) {
            HashSet<String> aux = new HashSet<>();
            HashSet<String> allW = DictReader.getMapNumWords().get(i);

            //usa el iterator
            for (String s : allW) {
                if (esParaulaSolucio(pSel, s)) {
                    aux.add(s);
                }
            }
            System.out.println(aux);
            mapNumSol.put(i, aux);
        }



    }
    public static boolean esParaulaSolucio(String p1, String p2){
        MapInterficie<Character, Integer> p1char = new UnsortedArrayMap<>(p1.length()),
                p2char = new UnsortedArrayMap<>(p2.length());
        addStringToMap(p1, p1char);
        addStringToMap(p2, p2char);
        Iterator<Pair<Character, Integer>> p2Iter = p2char.iterator();
        while(p2Iter.hasNext()){
            char c = p2Iter.next().getKey();

            if(p1char.get(c)==null || !p2char.get(c).equals(p1char.get(c))){
                return false;
            }
        }
        return true;
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

    public int getTamLLetraMax(){
        return tamLLetraMax;
    }

}
