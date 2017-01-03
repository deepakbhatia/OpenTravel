package com.projects050414.myapplication3.app;

/**
 * Created by obelix on 08/05/2014.
 */
public class Coords {
    public double lon;
    public double lat;

    public Coords(String coord) {
        int pos = coord.indexOf(",");
        this.lon = Double.parseDouble(coord.substring(pos+1, coord.length()));
        this.lat = Double.parseDouble(coord.substring(0, pos));
    }

    public Coords(Double x, Double y) {
        this.lat = y;
        this.lon = x;
    }

    public String toString() {
        return ""+lat+","+lon;
    }

    public int xToInt() {
        return (int) (lon*1E6);
    }

    public int yToInt() {
        return (int) (lat*1E6);
    }
}