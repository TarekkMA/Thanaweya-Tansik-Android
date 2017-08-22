package com.tmaprojects.tansik.icepick_bundlers;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import icepick.Bundler;

/**
 * Created by tarekkma on 8/22/17.
 */

public class ArrayListBundler implements Bundler<ArrayList<Integer>> {

    @Override
    public void put(String s, ArrayList<Integer> ts, Bundle bundle) {
        bundle.putIntegerArrayList(s,ts);
    }

    @Override
    public ArrayList<Integer> get(String s, Bundle bundle) {
        return bundle.getIntegerArrayList(s);
    }
}
