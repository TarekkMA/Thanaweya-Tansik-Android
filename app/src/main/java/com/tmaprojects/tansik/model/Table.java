package com.tmaprojects.tansik.model;

import java.util.List;

/**
 * Created by tarekkma on 8/19/17.
 */

public class Table {

    private Track track;
    private int year;
    private List<TableItem> table;


    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<TableItem> getTable() {
        return table;
    }

    public void setTable(List<TableItem> table) {
        this.table = table;
    }
}
