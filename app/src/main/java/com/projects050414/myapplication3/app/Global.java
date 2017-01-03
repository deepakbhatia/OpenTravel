package com.projects050414.myapplication3.app;

import java.util.ArrayList;

/**
 * Created by obelix on 08/05/2014.
 */
public class Global{
    private static Global instance;

    // Global variable
    private int data;
    private ArrayList<Stop> stops_in_area;
    private String tripid;

    // Restrict the constructor from being instantiated
    private Global(){}

    public void setData(int d){
        this.data=d;
    }
    public int getData(){
        return this.data;
    }
    public void setTripId(String tripId)
    {
        this.tripid = tripId;
    }

    public void setStops_in_area(ArrayList<Stop> s)
    {
        stops_in_area = s;
    }

    public ArrayList<Stop> getStops_in_area()
    {
        return  stops_in_area;
    }

    public static synchronized Global getInstance(){
        if(instance==null){
            instance=new Global();
        }
        return instance;
    }
}