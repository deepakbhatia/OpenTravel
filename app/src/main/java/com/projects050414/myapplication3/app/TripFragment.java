package com.projects050414.myapplication3.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by obelix on 09/05/2014.
 */
public class TripFragment extends Activity implements View.OnClickListener {

    TextView trip_created_greeting;
    TextView from_trip;
    TextView to_trip;
    TextView date_trip;
    TextView time_trip;
    TextView trip_info;

    Double dest_lat;
    Double dest_lon;
    Double start_lat;
    Double start_lon;
    String dest_address;
    String orig_address;
    String datetrip;
    String timetrip;
    int hours;
    int minute;
    int day;
    int month;
    int year;
    ImageButton edittrip;
    ImageButton deletetrip;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //trip_created_greeting = (TextView) findViewById(R.id.trip_created_greeting);
        from_trip = (TextView) findViewById(R.id.from_trip);
        to_trip = (TextView)findViewById(R.id.to_trip);
        date_trip = (TextView)findViewById(R.id.date_trip);
        time_trip = (TextView)findViewById(R.id.time_trip);

        trip_info = (TextView)findViewById(R.id.trip_info_card);

        from_trip.setText("from: " + GlobalValues.orig_address);
        to_trip.setText("to: " + GlobalValues.dest_address);

        date_trip.setText("on "+ GlobalValues.car_share_trip_date);
        time_trip.setText("at " +GlobalValues.car_share_trip_time);

        edittrip  = (ImageButton)findViewById(R.id.edittrip);
        edittrip.setOnClickListener(this);

        deletetrip  = (ImageButton)findViewById(R.id.delete_trip);
        deletetrip.setOnClickListener(this);


    }


  /*  // Container Activity must implement this interface
    public interface TripFragmentSelectedListener {
        public void onCreatingTrip(Stop s, Double dest_lat, Double dest_lon, String dest_address);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (TripFragmentSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TripFragmentSelectedListener");
        }
    }*/

    @Override
    public void onClick(View v) {
        Intent intent;
        if(v.getId() == R.id.edittrip){
            intent = new Intent(this, CreateTripActivity.class);
            intent.putExtra("orig_address", orig_address);
            intent.putExtra("dest_address",dest_address );
            intent.putExtra("hours", hours);
            intent.putExtra("minute", minute);
            intent.putExtra("year", year);
            intent.putExtra("month", month);
            intent.putExtra("day", day);
            intent.putExtra("start_lat",start_lat );
            intent.putExtra("start_lon",start_lon);

            startActivity(intent);
        }
        else if(v.getId() == R.id.delete_trip)
        {
            Toast.makeText(getApplicationContext(), "Trip Deleted", Toast.LENGTH_SHORT);
        }
        else if(v.getId() == R.id.ok_trip)
        {
            Toast.makeText(getApplicationContext(), "Trip Created", Toast.LENGTH_SHORT);
        }



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
        if(id == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
