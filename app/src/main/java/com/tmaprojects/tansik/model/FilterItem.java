package com.tmaprojects.tansik.model;

import java.util.List;

/**
 * Created by tarekkma on 8/21/17.
 */

public class FilterItem {
    private String name;
    private List<String> in;
    private List<String> out;

    public FilterItem(String name, List<String> in, List<String> out) {
        this.name = name;
        this.in = in;
        this.out = out;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIn() {
        return in;
    }

    public void setIn(List<String> in) {
        this.in = in;
    }

    public List<String> getOut() {
        return out;
    }

    public void setOut(List<String> out) {
        this.out = out;
    }
}
