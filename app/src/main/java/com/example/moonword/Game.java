package com.example.moonword;

import java.util.Iterator;

import SetMap.Map.MapInterfice;
import SetMap.Map.UnsortedArrayMap;
import SetMap.Set.Pair;

public class Game {

    /**
     * static de moment, idea de instanciar Game, per joc
     */
    public static boolean esParaulaSolucio(String p1, String p2){
        MapInterfice<Character, Integer> p1char = new UnsortedArrayMap<>(p1.length()),
                p2char = new UnsortedArrayMap<>(p2.length());
        addStringToMap(p1, p1char);
        addStringToMap(p2, p2char);
        //System.out.println(p1char);
        //System.out.println(p2char);
        Iterator<Pair<Character, Integer>> p2Iter = p2char.iterator();
        while(p2Iter.hasNext()){
            char c = p2Iter.next().getKey();
            //System.out.println("c: "+c+" "+p1char.get(c)+", "+p2char.get(c));

            if(p1char.get(c)==null || !p2char.get(c).equals(p1char.get(c))){
                return false;
            }
        }
        return true;
    }


    private static void addStringToMap(String p, MapInterfice<Character, Integer> map){
        for(char c:p.toCharArray()){
            if(map.get(c)==null){
                map.put(c,1);
            }else{
                map.put(c,map.get(c)+1);
            }
        }
    }

}
