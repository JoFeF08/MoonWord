package com.example.moonword;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import SetMap.Map.MapInterficie;


public class DictReader {

    private static BufferedReader reader;

    private static MapInterficie<Integer, ArrayList<String>> mapNumWords;
    private static MapInterficie<String, String> mapAllWords;

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
            if(mapNumWords.get(len)==null){
                ArrayList<String> aux = new ArrayList<>();
                aux.add(words[0]);
                mapNumWords.put(len, aux);
            }else{
                mapNumWords.get(len).add(words[0]);
            }
        }
    }




}
