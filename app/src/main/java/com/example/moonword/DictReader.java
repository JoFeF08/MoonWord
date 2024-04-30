package com.example.moonword;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import SetMap.Map.MapInterficie;


public class DictReader {

    private static BufferedReader reader;

    private static MapInterficie<Integer, ArrayList<String>> mapaNum;
    private static MapInterficie<String, String> mapaWord;

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
            mapaWord.put(words[0], words[1]);
            int len = words[0].length();
            if(mapaNum.get(len)==null){
                ArrayList<String> aux = new ArrayList<>();
                aux.add(words[0]);
                mapaNum.put(len, aux);
            }else{
                mapaNum.get(len).add(words[0]);
            }
        }
    }




}
