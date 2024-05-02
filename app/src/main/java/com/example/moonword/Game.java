package com.example.moonword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

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

    private MapInterficie<Integer, ArrayList<String>> mapNumSol;
    private MapInterficie<String, Integer> mapWordsSol;
    private SetInterficie<String> setFoundWords;

    private HashMap<Character, Integer> setChars = new HashMap<>();
    private int nLletres;

    public Game(int nLletres){
        this.nLletres = nLletres;

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

    public int getnLletres(){
        return nLletres;
    }

}
