package com.example.moonword;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class DictReader {

    private static BufferedReader reader;

    /**
     *
     * @param context
     */
    public static void configure(@NonNull Context context){
        reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.paraules)));
    }




}
