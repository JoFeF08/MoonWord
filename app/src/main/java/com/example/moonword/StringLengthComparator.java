package com.example.moonword;

import java.util.Comparator;

/**
 * Compara dos String primer per tamany i després per ordre alfabetic
 */
public class StringLengthComparator implements Comparator<String> {
    @Override
    public int compare(String s, String t1) {
        if(s.length()!=t1.length()){
            return s.length()-t1.length();
        }
        return s.compareTo(t1);
    }
}
