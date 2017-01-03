package com.projects050414.myapplication3.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import static com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;


public class CreateTripActivity extends FragmentActivity implements View.OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener, TimePickerFragment.TimeSelectedListener, DatePickerFragment.DateSelectedListener
         {

    Button time_button;
    Button date_button;
    AutoCompleteTextView start_trip;
    AutoCompleteTextView dest_trip;
    Button create_trip;
    TextView journey_trip_time;
    TextView journey_trip_date;
    int set_trip_year;
    int set_trip_day;
    int set_trip_month;
    int set_trip_hour;
    int set_trip_minute;

    ArrayAdapter<String> adapter;
    String trip_date;
    String trip_time;

    ArrayList<Stop> stops = null;
    Stop selected_stop = null;

    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    boolean mUpdatesRequested = false;
    Activity parentActivity;
    TripSQLiteHelper trip_db;
    private Handler mHandler;
    private LocationManager mLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        mLocationManager = (LocationManager)
                         this.getSystemService(Context.LOCATION_SERVICE);


        setContentView(R.layout.activity_kutsuplus_fragment);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        start_trip = (AutoCompleteTextView) findViewById(R.id.start_trip);
        start_trip.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.item_auto));

        start_trip.setThreshold(1);

        start_trip.setOnItemClickListener(starttriphandler);
        start_trip.setText(GlobalValues.address);
        //adapter = new ArrayAdapter<String>(this, R.layout.item_auto);

        //start_trip.getText();
        dest_trip = (AutoCompleteTextView)findViewById(R.id.destination_trip);
        dest_trip.setThreshold(1);
        dest_trip.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.item_auto));
        dest_trip.setOnItemClickListener(desttriphandler);
        //dest_trip.getText();

        time_button = (Button) findViewById(R.id.timeButton);
        //time_button.setOnClickListener(this);
        date_button = (Button) findViewById(R.id.dateButton);

        create_trip = (Button) findViewById(R.id.create_kutsuplus_trip);

        Calendar cal = Calendar.getInstance();
        trip_date = new SimpleDateFormat("yyyy:MM:dd").format(cal.getTime());
        trip_time = new SimpleDateFormat("HH:mm").format(cal.getTime());
        java.text.DateFormat time = java.text.DateFormat.getTimeInstance();
        java.text.DateFormat dates = java.text.DateFormat.getDateInstance();

        time_button.setText(trip_date);
        date_button.setText(trip_time);

        create_trip.setOnClickListener(this);


        // Create a new global location parameters object
        mLocationRequest = LocationRequest.create();

        /*
         * Set the update interval
         */
        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

       /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);


       if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
       {
           CheckEnableGPS();
       }
       trip_db = new TripSQLiteHelper(this);

    }

    AdapterView.OnItemClickListener starttriphandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String selected = (String) (parent.getItemAtPosition(position));

            String reference = ((PlacesAutoCompleteAdapter) parent.getAdapter()).getRefKey((int) id);
            //selected_stop = stops.get((int) id);


            String url="https://maps.googleapis.com/maps/api/place/details/json?sensor=true&key=AIzaSyCy2IUq1x6GJ7FosCEHV8j-1p9yezsb3Qs&"+
                    "reference=" + reference;


            new PlacesDetailsRequestTask().execute(url, "origin");

            //Toast.makeText(getActivity(), selected, Toast.LENGTH_SHORT).show();
        }

        /*@Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String selectedFromList = (String) (parent.getItemAtPosition(position));
            selected_stop = stops.get((int) id);
            Toast.makeText(getApplicationContext(), new StringBuilder().append(selectedFromList).append(":").append(selected_stop.address).toString(), Toast.LENGTH_SHORT).show();

        }*/
    };
    AdapterView.OnItemClickListener desttriphandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String selected = (String) (parent.getItemAtPosition(position));
            Toast.makeText(getApplicationContext(),"Country:" +GlobalValues.countryCode,Toast.LENGTH_LONG).show();


            String reference = ((PlacesAutoCompleteAdapter) parent.getAdapter()).getRefKey((int) id);
            //selected_stop = stops.get((int) id);


            String url="https://maps.googleapis.com/maps/api/place/details/json?sensor=true&key=AIzaSyCy2IUq1x6GJ7FosCEHV8j-1p9yezsb3Qs&"+
                    "reference=" + reference;


            new PlacesDetailsRequestTask().execute(url,"destination");

            //Toast.makeText(getActivity(), selected, Toast.LENGTH_SHORT).show();
        }
    };

    /*
    * Called when the Activity is restarted, even before it becomes visible.
    */

    @Override
    public void onPause() {

        super.onPause();
        mLocationManager.removeUpdates(mAndroidLocationListener);
    }
    /*
     * Called when the Activity is no longer visible at all.
     * Stop updates and disconnect.
     */
    @Override
    public void onStop() {

        super.onStop();
        mLocationManager.removeUpdates(mAndroidLocationListener);

        /*if(mLocationClient!=null){
            if (mLocationClient.isConnected()) {
                stopPeriodicUpdates();
            }

            // After disconnect() is called, the client is considered "dead".
            mLocationClient.disconnect();
        }*/
        // If the client is connected
    }

             LocationListener mAndroidLocationListener = new LocationListener() {
                 public void onLocationChanged(final Location location) {
                     mHandler.post(new Runnable() {
                         public void run() {

                             GlobalValues.start_lat = location.getLatitude();
                             GlobalValues.start_lon = location.getLongitude();

                         }
                     });
                 }

                 @Override
                 public void onStatusChanged(String provider, int status, Bundle extras) {

                 }

                 @Override
                 public void onProviderEnabled(String provider) {

                 }

                 @Override
                 public void onProviderDisabled(String provider) {

                 }
             };

    public void enableLocation()
    {
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            CheckEnableGPS();
        }
          try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0,
                        mAndroidLocationListener);
            } catch(IllegalArgumentException e) {
                Log.w("Create Trip", "on Resume, Location Updates, Location provider is unavailable");
            }

    }
    @Override
    protected void onDestroy() {
        Log.w(LocationUtils.APPTAG, "App destoryed");

        super.onDestroy();
    }
    @Override
    public void onResume()
    {
        super.onResume();

        enableLocation();

        /*if(servicesConnected())
        {
            Location local = getLocation();
*/
            //Bundle args = getArguments();
            //if (args != null) {

            /*Double lat = args.getDouble("latitude");
            Double lon = args.getDouble("longitude");
            String orig = args.getString("orig");
            String dest = args.getString("dest");
            String date = args.getString("date");
            String time = args.getString("time");*/

            //Double lat = local.getLatitude();
            //Double lon = local.getLongitude();
            /*start_lat = Double.parseDouble("60.1708");
            start_lon = Double.parseDouble("24.9375");
            Calendar rightNow = Calendar.getInstance();
            rightNow.set(Calendar.HOUR_OF_DAY, 0);
            Date d = rightNow.getTime();

            d.getYear();


            String url = "http://api.reittiopas.fi/hsl/prod/?request=stops_area" +
                    "&user=theholeinmysole&pass=report6683&format=json" +
                    "&epsg_in=wgs84&epsg_out=wgs84&limit=20&diameter=1500&" +
                    "center_coordinate=" + start_lon + "," +
                    +start_lat;
            System.out.println(" URL : " + url);
            new RequestTask().execute(url);*/
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_trip, menu);
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
        if(id == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {


            Intent intent = new Intent(getApplicationContext(), TripFragment.class);

            startActivity(intent);
            Trips trip = new Trips();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            System.out.println(dateFormat.format(cal.getTime()));
            trip_db.addTrip(trip);

    }

    @Override
    public void onConnected(Bundle bundle) {

        if (mUpdatesRequested) {
            startPeriodicUpdates();
        }
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {

                // Log the error
                e.printStackTrace();
            }
        } else {

            // If no resolution is available, display a dialog to the user with the error.
            //showErrorDialog(connectionResult.getErrorCode());
        }

    }


    public void startUpdates() {
        mUpdatesRequested = true;

        if (servicesConnected()) {
            startPeriodicUpdates();
        }
    }

    /**
     * Invoked by the "Stop Updates" button
     * Sends a request to remove location updates
     * request them.
     *
     *
     */
    public void stopUpdates() {
        mUpdatesRequested = false;

        if (servicesConnected()) {
            stopPeriodicUpdates();
        }
    }

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {

        mLocationClient.requestLocationUpdates(mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        //mConnectionState.setText(R.string.location_requested);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates((com.google.android.gms.location.LocationListener) this);
        //mConnectionState.setText(R.string.location_updates_stopped);
    }

     class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //Do anything with response..
            System.out.println(" Result : " + result);
            InputStream stream = new ByteArrayInputStream(result.getBytes());
            try {
                stops = readJsonStream(stream);
                stream.close();


                ArrayList<String> vals = new ArrayList<String>();

                if (stops != null)
                    for (int i = 0; i < stops.size(); i++) {
                        Stop a_stop = stops.get(i);
                        vals.add(i,
                                ((a_stop)).address +
                                        ", " + (a_stop).city + ", Finland, " +
                                        (a_stop).dist + " meters"
                        );


                    }

                System.out.println(" Vals: " + vals.size());
                adapter.addAll(vals);
                start_trip.setAdapter(adapter);
                Global g = Global.getInstance();
                g.setStops_in_area(stops);
                //start_trip.setText("Helsinki");

            } catch (IOException ie) {
                ie.printStackTrace();

            }
        }

    }


    public ArrayList<Stop> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {

            System.out.println(" Sending stop request readJsonStream");
            return readMessagesArray(reader);

        } finally {
            reader.close();
        }
    }

    public ArrayList<Stop> readMessagesArray(JsonReader reader) throws IOException {
        ArrayList<Stop> stops_in_area
                = new ArrayList();
        System.out.println(" Sending stop request readMessagesArray" );
        reader.beginArray();
        while (reader.hasNext()) {
            System.out.println(" Sending stop request readMessagesArray" );
            stops_in_area.add(readMessage(reader));
            System.out.println(" Sending stop request readMessagesArray" );
        }
        reader.endArray();
        return stops_in_area;
    }

    public Stop readMessage(JsonReader reader) throws IOException {
        long id = -1;
        String text = null;
        List geo = null;
        Stop stop = new Stop();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("code")) {
                stop.code = reader.nextLong();
                System.out.println(" stop code = " + stop.code);
            } else if (name.equals("name")) {
                stop.stopname = reader.nextString();
            } else if (name.equals("city")) {
                stop.city = reader.nextString();
            } else if (name.equals("coords") ) {
                String val = reader.nextString();
                String coordinate[] = val.split(",");
                stop.coords = new Coords(Double.parseDouble(coordinate[0]),Double.parseDouble(coordinate[1]) );
            } else if (name.equals("dist")) {
                stop.dist = reader.nextInt();
            } else if (name.equals("codeShort")) {
                stop.codeShort = reader.nextString();
            }else if (name.equals("address")) {
                stop.address = reader.nextString();
            }
        }
        reader.endObject();
        return stop;
    }

/*




    class PlacesDetailsRequestTask extends AsyncTask<String, String, String> {

        String type="";
        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                type=uri[1];
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //Do anything with response..
            System.out.println(" Result : " + result);
            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);

                    JSONObject tmp = jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
                    JSONObject addressObj = jsonObj.getJSONObject("result");
                    if(type.equals("destination"))
                    {
                        dest_address= addressObj.getString("formatted_address");
                        dest_lat = tmp.getDouble("lat");
                        dest_lon = tmp.getDouble("lng");
                    }

                    else
                    {

                        orig_address= addressObj.getString("formatted_address");
                        start_lat = tmp.getDouble("lat");
                        start_lon = tmp.getDouble("lng");
                    }


                } catch (Exception ie) {
                    ie.printStackTrace();

                }
            }
        }
    }

*/

    private boolean locationServicesEnabled()
    {
        LocationManager locationManager=null;
        Boolean gps_enabled = false, network_enabled = false, location_services_enabled=false;


        // Getting LocationManager object from System Service LOCATION_SERVICE
        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if(!gps_enabled && !network_enabled) {
            return false;
        }

        return true;
    }

    private void CheckEnableGPS(){


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Location Settings are off, Activate Location Settings");
        dialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 100);
            }
        });

        dialog.show();

    }

    /**
     * Invoked by the "Get Location" button.
     *
     * Calls getLastLocation() to get the current location
     *
     *
     */
    public Location getLocation() {

        // If Google Play Services is available
        if (servicesConnected()) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();
            /*Toast.makeText(this, currentLocation.getLatitude() + ","+currentLocation.getLongitude(),
                    Toast.LENGTH_LONG).show();
*/

            return currentLocation;

        }

        return null;

    }



    /**
     * Invoked by the "Get Address" button.
     * Get the address of the current location, using reverse geocoding. This only works if
     * a geocoding service is available.
     *
     *
     */
    // For Eclipse with ADT, suppress warnings about Geocoder.isPresent()
    @SuppressLint("NewApi")
    public void getAddress() {

        // In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
            // No geocoder is present. Issue an error message
            Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
            return;
        }

        if (servicesConnected()) {


            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();

            // Turn the indefinite activity indicator on
            // mActivityIndicator.setVisibility(View.VISIBLE);

            // Start the background task
            (new GetAddressTask(this)).execute(currentLocation);
        }
    }

    /**
     * An AsyncTask that calls getFromLocation() in the background.
     * The class uses the following generic types:
     * Location - A {@link android.location.Location} object containing the current location,
     *            passed as the input parameter to doInBackground()
     * Void     - indicates that progress units are not used by this subclass
     * String   - An address passed to onPostExecute()
     */
    protected class GetAddressTask extends AsyncTask<Location, Void, String> {

        // Store the context passed to the AsyncTask when the system instantiates it.
        Context localContext;

        // Constructor called by the system to instantiate the task
        public GetAddressTask(Context context) {

            // Required by the semantics of AsyncTask
            super();

            // Set a Context for the background task
            localContext = context;
        }

        /**
         * Get a geocoding service instance, pass latitude and longitude to it, format the returned
         * address, and return the address to the UI thread.
         */
        @Override
        protected String doInBackground(Location... params) {
            /*
             * Get a new geocoding service instance, set for localized addresses. This example uses
             * android.location.Geocoder, but other geocoders that conform to address standards
             * can also be used.
             */
            Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

            // Get the current location from the input parameter list
            Location location = params[0];

            // Create a list to contain the result address
            List <Address> addresses = null;

            // Try to get an address for the current location. Catch IO or network problems.
            try {

                /*
                 * Call the synchronous getFromLocation() method with the latitude and
                 * longitude of the current location. Return at most 1 address.
                 */
                addresses = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1
                );

                // Catch network or other I/O problems.
            } catch (IOException exception1) {

                // Log an error and return an error message
                //Log.e(LocationUtils.APPTAG, getString(R.string.IO_Exception_getFromLocation));

                // print the stack trace
                exception1.printStackTrace();

                // Return an error message
                //return (getString(R.string.IO_Exception_getFromLocation));

                // Catch incorrect latitude or longitude values
            } catch (IllegalArgumentException exception2) {

                // Construct a message containing the invalid arguments
                String errorString = getString(
                        R.string.illegal_argument_exception,
                        location.getLatitude(),
                        location.getLongitude()
                );
                // Log the error and print the stack trace
                Log.e(LocationUtils.APPTAG, errorString);
                exception2.printStackTrace();

                //
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {

                // Get the first address
                Address address = addresses.get(0);

                // Format the first line of address
                String addressText = getString(R.string.address_output_string,

                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",

                        // Locality is usually a city
                        address.getLocality(),

                        // The country of the address
                        address.getCountryName()
                );

                // Return the text
                return addressText;

                // If there aren't any addresses, post a message
            } else {
                return getString(R.string.no_address_found);
            }
        }

        /**
         * A method that's called once doInBackground() completes. Set the text of the
         * UI element that displays the address. This method runs on the UI thread.
         */
        @Override
        protected void onPostExecute(String address) {

            // Set the address in the UI
            //Toast.makeText(this, address ,Toast.LENGTH_LONG).show();

        }
    }
    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {

            // In debug mode, log the status
            Log.d(ActivityUtils.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;

            // Google Play services was not available for some reason
        } else {

            // Display an error dialog
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();
            return false;
        }
    }
    /*

    Date and Time Methods


     */
    public void showTimePickerDialog(View v) {
        android.support.v4.app.DialogFragment picker = new TimePickerFragment();
        picker.show(getSupportFragmentManager(), "timePicker");


    }


    public void showDatePickerDialog(View v) {
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void timeselected(int hours, int minute) {
        GlobalValues.car_share_trip_time = hours+":"+minute;
    }

    @Override
    public void dateselected(int year, int month, int day) {
        GlobalValues.car_share_trip_date = year+":"+month+":"+day;
    }


    /*

                Date And Time Fragments

     */







}
