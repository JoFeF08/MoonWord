package com.example.moonword;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class DictReader {

    private static BufferedReader reader;

    public static void configure(Context context){
        reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.paraules)));
    }




}
