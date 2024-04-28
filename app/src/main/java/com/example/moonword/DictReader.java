package com.example.moonword;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DictReader {

    private static BufferedReader reader;

    public static void configure(Context context){
        reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.paraules)));
    }


}
