package com.projects050414.myapplication3.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class TripCreatedActivity extends FragmentActivity implements View.OnClickListener {

    Double dest_lat;
    Double dest_lon;
    Double start_lat;
    Double start_lon;
    String dest_address;
    String orig_address;
    int triphours;
    int tripminute;
    int tripday;
    int tripmonth;
    int tripyear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        dest_address = intent.getStringExtra("com.projects050414.myapplication3.app.dest_address");
        orig_address = intent.getStringExtra("com.projects050414.myapplication3.app.orig_address");
        triphours = intent.getIntExtra("com.projects050414.myapplication3.app.triphours", 0);
        tripminute = intent.getIntExtra("com.projects050414.myapplication3.app.tripminute", 0);
        tripday = intent.getIntExtra("com.projects050414.myapplication3.app.tripday", 0);
        tripmonth = intent.getIntExtra("com.projects050414.myapplication3.app.tripmonth", 1);

        tripyear = intent.getIntExtra("com.projects050414.myapplication3.app.tripyear", 2014);
        //setTitle("Trip To " + dest_address);
        dest_lat = intent.getDoubleExtra("com.projects050414.myapplication3.app.dest_lat", 0.0);
        dest_lon = intent.getDoubleExtra("com.projects050414.myapplication3.app.dest_lon", 0.0);
        start_lat = intent.getDoubleExtra("com.projects050414.myapplication3.app.start_lat", 0.0);
        start_lon = intent.getDoubleExtra("com.projects050414.myapplication3.app.start_lon", 0.0);



        TextView from_trip = (TextView)findViewById(R.id.from_trip);
        TextView to_trip = (TextView)findViewById(R.id.to_trip);
        TextView tripdate = (TextView)findViewById(R.id.date_trip);

        TextView triptime = (TextView)findViewById(R.id.time_trip);

        from_trip.setText(orig_address);
        to_trip.setText(dest_address);

        tripdate.setText(tripday+"/"+tripmonth+"/"+tripyear);
        triptime.setText(triphours+":"+tripminute);

        ImageButton edittrip = (ImageButton)findViewById(R.id.edittrip);
        edittrip.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trip_created, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
/*
        FragmentManager fragmentManager = getFragmentManager();
        KutsuplusFragment kutsuplusFragment = new  KutsuplusFragment();

        //kutsuplusFragment.onAttach(this.getParent());
        Bundle locationBundle = new Bundle();
        //Location local = getLocation();

            //locationBundle.putDouble("latitude", local.getLatitude());
            //locationBundle.putDouble("longitude", local.getLongitude());
            locationBundle.putDouble("latitude",start_lat);
            locationBundle.putDouble("longitude", start_lon);


        locationBundle.putString("orig",orig_address);
        locationBundle.putString("dest",dest_address);
        locationBundle.putString("date",tripyear+"/"+tripmonth+"/"+tripday);
        locationBundle.putString("time",triphours+":"+tripminute);
        kutsuplusFragment.setArguments(locationBundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, kutsuplusFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        //NavUtils.navigateUpFromSameTask(this);*/

    }


    /*@Override
    public void onCreatingTrip(Stop s, Double dest_lat, Double dest_lon, String dest_address) {

    }*/
}
