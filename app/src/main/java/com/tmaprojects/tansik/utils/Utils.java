package com.tmaprojects.tansik.utils;

/**
 * Created by tarekkma on 8/20/17.
 */

public class Utils {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}