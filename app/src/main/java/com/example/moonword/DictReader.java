package com.example.moonword;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import SetMap.Map.MapInterficie;


public class DictReader {

    private static BufferedReader reader;

    private static TreeMap<Integer, HashSet<String>> mapNumWords= new TreeMap<>();
    private static HashMap<String, String>  mapAllWords = new HashMap<>();

    /**
     *
     * @param context
     */
    public static void configure(@NonNull Context context){
        reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.paraules)));
    }

    /**
     * no se si al final ha de ser aixi
     * @throws IOException
     */
    public static void loadAll() throws IOException {
        while (reader.ready()){
            String line = reader.readLine();
            String[] words = line.split(";");
            //per_cerca;correcte
            mapAllWords.put(words[0], words[1]);
            int len = words[0].length();
            if(len<3 || len>7){
                continue;
            }
            if(mapNumWords.get(len)==null){
                HashSet<String> aux = new HashSet<>();
                aux.add(words[0]);
                mapNumWords.put(len, aux);
            }else{
                mapNumWords.get(len).add(words[0]);
            }
        }
    }


    public static TreeMap<Integer, HashSet<String>> getMapNumWords() {
        return mapNumWords;
    }

    public static HashMap<String, String> getMapAllWords() {
        return mapAllWords;
    }



}
