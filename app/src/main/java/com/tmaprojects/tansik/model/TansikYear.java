package com.tmaprojects.tansik.model;

/**
 * Created by tarekkma on 8/19/17.
 */

public class TansikYear {
    private int year;
    private Table scienceTable;
    private Table literatureTable;


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Table getScienceTable() {
        return scienceTable;
    }

    public Table getTable(Track t){
        switch (t){
            case SCIENCE:return scienceTable;
            case LITERATURE:return literatureTable;
            default:return null;
        }
    }

    public void setScienceTable(Table scienceTable) {
        this.scienceTable = scienceTable;
    }

    public Table getLiteratureTable() {
        return literatureTable;
    }

    public void setLiteratureTable(Table literatureTable) {
        this.literatureTable = literatureTable;
    }
}
