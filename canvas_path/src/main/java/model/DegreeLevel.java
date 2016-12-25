package model;

/**
 * Created by jierui on 2016/12/23.
 */

public class DegreeLevel {
    private int year;
    private int month;
    private int[] degree = new int[42];

    public int[] getDegree() {
        return degree;
    }

    public void setDegree(int[] degree) {
        this.degree = degree;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
