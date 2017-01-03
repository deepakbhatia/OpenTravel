package com.projects050414.myapplication3.app;

import android.location.Location;
import android.widget.AutoCompleteTextView;

/**
 * Created by obelix on 22/10/2014.
 */
public  class GlobalValues {

    public static String country = "";
    public static String address ="";
    public static String city="";
    public static Location location=null;
    public static String countryCode="";
    public static Double start_lon;
    public static Double start_lat;
    public static Double dest_lat;
    public static Double dest_lon;
    public static String dest_address;
    public static String orig_address;
    public static AutoCompleteTextView start_trip;
    public static AutoCompleteTextView dest_trip;
    public static String car_share_trip_date="";
    public static String car_share_trip_time="";
}
