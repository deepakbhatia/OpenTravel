package com.projects050414.myapplication3.app;

/**
 * Created by obelix on 03/05/2014.
 */
/*
public class DestinationSearchActivity extends FragmentActivity
        {

    private final String TAG="DESTINATIONSEARCHACTIVITY";


    public static final String tampere_api_public_transport = "http://api.publictransport.tampere.fi/1_0_3/?user=theholeinmysole&pass=report6683&epsg_in=wgs84&epsg_out=wgs84";
    public static final String hsl_api_public_transport="http://api.reittiopas.fi/hsl/prod/?user=obelix&pass=report6683&epsg_in=wgs84&epsg_out=wgs84";
    public static final String matka_api = "http://api.matka.fi/public-lvm/fi/api/?user=obelix&pass=report6683";

    // Constants that define the activity detection interval
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int DETECTION_INTERVAL_SECONDS = 20;
    public static final int DETECTION_INTERVAL_MILLISECONDS =
            MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    */
/*
     * Store the PendingIntent used to send activity recognition events
     * back to the app
     *//*

    private PendingIntent mActivityRecognitionPendingIntent;
    // Store the current activity recognition client
    private ActivityRecognitionClient mActivityRecognitionClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        mLocationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        // Get the intent, verify the action and get the query

    }






    */
/*
     * Create the menu
     *//*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.destinationsearch, menu);
        return true;

    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
*/

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DestinationSearchActivity extends FragmentActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DatePickerFragment.DateSelectedListener,
        TimePickerFragment.TimeSelectedListener, View.OnClickListener {
    public static final String tampere_api_public_transport = "http://api.publictransport.tampere.fi/1_0_3/?user=theholeinmysole&pass=report6683&epsg_in=wgs84&epsg_out=wgs84";
    public static final String hsl_api_public_transport="http://api.reittiopas.fi/hsl/prod/?user=obelix&pass=report6683&epsg_in=wgs84&epsg_out=wgs84";
    public static final String matka_api = "http://api.matka.fi/public-lvm/fi/api/?user=obelix&pass=report6683";

    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    // Handles to UI widgets
    private AutoCompleteTextView start_trip;
    private AutoCompleteTextView end_trip;
    private ProgressBar mActivityIndicator;
    private TextView mConnectionState;
    private TextView mConnectionStatus;

    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;

    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;

    /*
     * Note if updates have been turned on. Starts out as "false"; is set to "true" in the
     * method handleRequestSuccess of LocationUpdateReceiver.
     *
     */
    boolean mUpdatesRequested = false;

    Location currentLocation;
    int set_trip_year;
    int set_trip_day;
    int set_trip_month;
    int set_trip_hour;
    int set_trip_minute;
    Button time_button;
    Button date_button;
    Button submit_button;
    /*
     * Initialize the Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get handles to the UI view objects
        start_trip = (AutoCompleteTextView) findViewById(R.id.origin_trip);
        end_trip = (AutoCompleteTextView) findViewById(R.id.end_trip);
        String country = "";

        start_trip.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.item_auto));
        end_trip.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.item_auto));

        start_trip.setOnItemClickListener(starttriphandler);
        end_trip.setOnItemClickListener(endtriphandler);

        time_button = (Button) findViewById(R.id.trip_timeButton);
        //time_button.setOnClickListener(this);
        date_button = (Button) findViewById(R.id.trip_dateButton);


        submit_button = (Button)findViewById(R.id.submit);

        submit_button.setOnClickListener(this);
        Calendar cal = Calendar.getInstance();

        /*cal.set(Calendar.YEAR, set_trip_year);
        cal.set(Calendar.DAY_OF_MONTH, set_trip_day);
        cal.set(Calendar.MONTH, set_trip_month);
        cal.set(Calendar.HOUR, set_trip_hour);
        cal.set(Calendar.MINUTE, set_trip_minute);
*/
        String trip_date;
        String trip_time;

        //new SimpleDateFormat().format(new Date(TimeinMilliSeccond));

        trip_date = new SimpleDateFormat("MM/dd/yyyy").format(new Date(cal.getTimeInMillis()));
        trip_time = new SimpleDateFormat("HH:mm").format(new Date(cal.getTimeInMillis()));
        java.text.DateFormat time_val = java.text.DateFormat.getTimeInstance();
        java.text.DateFormat date_val = java.text.DateFormat.getDateInstance();

        time_button.setText(trip_time);
        date_button.setText(trip_date);


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

        // Note that location updates are off until the user turns them on
        mUpdatesRequested = false;

        // Open Shared Preferences
        mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        // Get an editor
        mEditor = mPrefs.edit();

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);


    }
    public void showDatePickerDialog(View v) {
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        android.support.v4.app.DialogFragment picker = new TimePickerFragment();
        picker.show(getSupportFragmentManager(), "timePicker");


    }
    private void doMySearch(String query) {

        Toast.makeText(this, query, Toast.LENGTH_SHORT);

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

    };
    AdapterView.OnItemClickListener endtriphandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String selected = (String) (parent.getItemAtPosition(position));

            String reference = ((PlacesAutoCompleteAdapter) parent.getAdapter()).getRefKey((int) id);
            //selected_stop = stops.get((int) id);


            String url="https://maps.googleapis.com/maps/api/place/details/json?sensor=true&key=AIzaSyCy2IUq1x6GJ7FosCEHV8j-1p9yezsb3Qs&"+
                    "reference=" + reference;


            new PlacesDetailsRequestTask().execute(url,"destination");

            //Toast.makeText(getActivity(), selected, Toast.LENGTH_SHORT).show();
        }
    };

    /*
     * Called when the Activity is no longer visible at all.
     * Stop updates and disconnect.
     */
    @Override
    public void onStop() {

        // If the client is connected
        if (mLocationClient.isConnected()) {
            stopPeriodicUpdates();
        }

        // After disconnect() is called, the client is considered "dead".
        mLocationClient.disconnect();

        super.onStop();
    }
    /*
     * Called when the Activity is going into the background.
     * Parts of the UI may be visible, but the Activity is inactive.
     */
    @Override
    public void onPause() {

        // Save the current setting for updates
        mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, mUpdatesRequested);
        mEditor.commit();

        super.onPause();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {

        super.onStart();

        /*
         * Connect the client. Don't re-start any requests here;
         * instead, wait for onResume()
         */
        mLocationClient.connect();
        start_trip.setText(GlobalValues.address);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            end_trip.setText(query);
            //doMySearch(query);
        }

        //getAddress();

    }
    /*
     * Called when the system detects that this Activity is now visible.
     */
    @Override
    public void onResume() {
        super.onResume();

        // If the app already has a setting for getting location updates, get it
        if (mPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED)) {
            mUpdatesRequested = mPrefs.getBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);

            // Otherwise, turn off location updates until requested
        } else {
            mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
            mEditor.commit();
        }

    }

    /*
     * Handle results returned to this Activity by other Activities started with
     * startActivityForResult(). In particular, the method onConnectionFailed() in
     * LocationUpdateRemover and LocationUpdateRequester may call startResolutionForResult() to
     * start an Activity that handles Google Play services problems. The result of this
     * call returns here, to onActivityResult.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        // Log the result
                        Log.d(LocationUtils.APPTAG, getString(R.string.resolved));

                        // Display the result
                        //mConnectionState.setText(R.string.connected);
                        //mConnectionStatus.setText(R.string.resolved);
                        break;

                    // If any other result was returned by Google Play services
                    default:
                        // Log the result
                        Log.d(LocationUtils.APPTAG, getString(R.string.no_resolution));

                        // Display the result
                        //mConnectionState.setText(R.string.disconnected);
                        //mConnectionStatus.setText(R.string.no_resolution);

                        break;
                }

                // If any other request code was received
            default:
                // Report that this Activity received an unknown requestCode
                Log.d(LocationUtils.APPTAG,
                        getString(R.string.unknown_activity_request_code, requestCode));

                break;
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
            Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
            }
            return false;
        }
    }

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
            currentLocation = mLocationClient.getLastLocation();

            // Turn the indefinite activity indicator on

            // Start the background task
            new GetAddressTask(this).execute(currentLocation);
        }
    }

    /**
     * Invoked by the "Start Updates" button
     * Sends a request to start location updates
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    public void startUpdates(View v) {
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
     * @param v The view object associated with this method, in this case a Button.
     */
    public void stopUpdates(View v) {
        mUpdatesRequested = false;

        if (servicesConnected()) {
            stopPeriodicUpdates();
        }
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
//        mConnectionStatus.setText(R.string.connected);

        if (mUpdatesRequested) {
            startPeriodicUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        mConnectionStatus.setText(R.string.disconnected);
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
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
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    /**
     * Report location updates to the UI.
     *
     * @param location The updated location.
     */
    @Override
    public void onLocationChanged(Location location) {

        // Report to the UI that the location was updated
        //mConnectionStatus.setText(R.string.location_updated);

        // In the UI, set the latitude and longitude to the value received
        currentLocation = location;
    }

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {

        mLocationClient.requestLocationUpdates(mLocationRequest, this);
        //mConnectionState.setText(R.string.location_requested);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
        //mConnectionState.setText(R.string.location_updates_stopped);
    }

    public void getTripOptions(View v)
    {
        mActivityIndicator.setVisibility(View.VISIBLE);

    }

    @Override
    public void timeselected(int hours, int minute) {
        set_trip_hour = hours;
        set_trip_minute = minute;
    }

    @Override
    public void dateselected(int year, int month, int day) {
        set_trip_year = year;
        set_trip_month = month;
        set_trip_day = day;
    }

    @Override
    public void onClick(View v) {


        int view_id = v.getId();

        if(view_id == R.id.submit)
        {


        }
    }


    protected class SearchTripTask extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String[] params) {
            return null;
        }
        @Override
        protected void onPostExecute(String result)
        {

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
    protected class GetAddressTask extends AsyncTask<Location, String, String> {

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

                    //geocoder.get

                    // Catch network or other I/O problems.
                } catch (IOException exception1) {

                    // Log an error and return an error message
                    Log.e(LocationUtils.APPTAG, getString(R.string.IO_Exception_getFromLocation));

                    // print the stack trace
                    exception1.printStackTrace();

                    // Return an error message
                    return (getString(R.string.IO_Exception_getFromLocation));

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

                    String addressLine="";
                    String locality="";
                    String countryName="";
                    if(address.getAddressLine(0)!=null)
                        addressLine = address.getAddressLine(0);
                    if(address.getLocality()!=null)
                    {
                        locality = address.getLocality();
                    }

                    if(address.getCountryName()!=null)
                    {
                        countryName = address.getCountryName();
                    }

                    // Format the first line of address
                    String addressText = getString(R.string.address_output_string,

                            // If there's a street address, add it
                            address.getMaxAddressLineIndex() > 0 ?
                                    addressLine: "",

                            // Locality is usually a city
                            locality,

                            // The country of the address
                            countryName

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

            // Turn off the progress bar
            // Set the address in the UI
            start_trip.setText(address);

        }
    }



    protected class GetLocationTask extends AsyncTask<String, String, String> {

        // Store the context passed to the AsyncTask when the system instantiates it.
        Context localContext;

        // Constructor called by the system to instantiate the task
        public GetLocationTask(Context context) {

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
        protected String doInBackground(String... params) {
            /*
             * Get a new geocoding service instance, set for localized addresses. This example uses
             * android.location.Geocoder, but other geocoders that conform to address standards
             * can also be used.
             */
            Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());


            String address_location = params[0];

            // Create a list to contain the result address
            List<Address> addresses = null;

            // Try to get an address for the current location. Catch IO or network problems.
            try {

                /*
                 * Call the synchronous getFromLocation() method with the latitude and
                 * longitude of the current location. Return at most 1 address.
                 */

                addresses = geocoder.getFromLocationName(address_location, 1
                );

                //geocoder.get

                // Catch network or other I/O problems.
            } catch (IOException exception1) {

                // Log an error and return an error message
                Log.e(LocationUtils.APPTAG, getString(R.string.IO_Exception_getFromLocation));

                // print the stack trace
                exception1.printStackTrace();

                // Return an error message
                return (getString(R.string.IO_Exception_getFromLocation));

                // Catch incorrect latitude or longitude values
            } catch (IllegalArgumentException exception2) {

                // Construct a message containing the invalid arguments
                String errorString = getString(
                        R.string.illegal_argument_exception,
                        address_location
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

                double latitude = 0.0;
                double longitude = 0.0;

                if (address.getLatitude() != 0) {
                    latitude = address.getLatitude();
                }

                if (address.getLongitude() != 0) {
                    longitude = address.getLongitude();
                }

                // Return the text
                return latitude+":"+longitude;

                // If there aren't any addresses, post a message
            } else {
                return getString(R.string.no_address_found);
            }


            // Get the current location from the input parameter list


        }
    }
    /**
     * Show a dialog returned by Google Play services for the
     * connection error code
     *
     * @param errorCode An error code returned from onConnectionFailed
     */
    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
        }
    }

    /**
     * Define a DialogFragment to display the error dialog generated in
     * showErrorDialog.
     */
    public  static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /**
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
}
