package com.projects050414.myapplication3.app;

import android.support.v4.app.FragmentActivity;


public class KutsuplusFragment extends FragmentActivity  {/*

    Button time_button;
    Button date_button;
    AutoCompleteTextView start_trip;
    AutoCompleteTextView dest_trip;
    Button create_trip;
    TextView journey_trip_time;
    TextView journey_trip_date;
    KutsuplusSelectedListener mCallback;
    int set_trip_year;
    int set_trip_day;
    int set_trip_month;
    int set_trip_hour;
    int set_trip_minute;
    Double start_lon;
    Double start_lat;
    Double dest_lat;
    Double dest_lon;
    String dest_address;


    ArrayList<Stop> stops = null;
    Stop selected_stop = null;

    @Override
    public void onClick(View v) {
        *//*System.out.println(" vals: ");
        FragmentManager fragmentManager = getFragmentManager();

        TripFragment tripFragment = new  TripFragment();
        Bundle locationBundle = new Bundle();
        locationBundle.putDouble("latitude",s.coords.lat );
        locationBundle.putDouble("longitude", s.coords.lon);


        locationBundle.putString("orig",s.address);
        locationBundle.putString("dest",dest_address);
        locationBundle.putString("date","Today");
        locationBundle.putString("time","Now");
        tripFragment.setArguments(locationBundle);
        fragmentManager.beginTransaction()
                .replace(R.id.trip_info, tripFragment)
                .commit();
        v = inflater.inflate(
                R.layout.trip_layout, v, false);*//*

        mCallback.onCreatingTrip(selected_stop, dest_lat, dest_lon, dest_address);


        return;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    // Container Activity must implement this interface
    public interface KutsuplusSelectedListener {
        public void onCreatingTrip(Stop s, Double dest_lat, Double dest_lon, String dest_address);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (KutsuplusSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement KutsuplusSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(
                R.layout.activity_kutsuplus_fragment, container, false);

        start_trip = (AutoCompleteTextView) view.findViewById(R.id.start_trip);
        start_trip.setThreshold(1);

        start_trip.setOnItemClickListener(starttriphandler);

        //start_trip.getText();
        dest_trip = (AutoCompleteTextView) view.findViewById(R.id.destination_trip);
        dest_trip.setThreshold(1);
        dest_trip.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.item_auto));
        dest_trip.setOnItemClickListener(desttriphandler);
        //dest_trip.getText();

        time_button = (Button) view.findViewById(R.id.timeButton);
        //time_button.setOnClickListener(this);
        date_button = (Button) view.findViewById(R.id.dateButton);

        create_trip = (Button) view.findViewById(R.id.create_kutsuplus_trip);

        time_button.setText("Now");
        date_button.setText("Today");

        create_trip.setOnClickListener(this);
        return view;
        *//*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*//*
    }

    AdapterView.OnItemClickListener starttriphandler = new AdapterView.OnItemClickListener() {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String selectedFromList = (String) (parent.getItemAtPosition(position));
            selected_stop = stops.get((int) id);
            Toast.makeText(getActivity(), selectedFromList + ":" + selected_stop.address, Toast.LENGTH_SHORT).show();

        }
    };
    AdapterView.OnItemClickListener desttriphandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String selected = (String) (parent.getItemAtPosition(position));

            String reference = ((PlacesAutoCompleteAdapter) parent.getAdapter()).getRefKey((int) id);
            //selected_stop = stops.get((int) id);


            String url="https://maps.googleapis.com/maps/api/place/details/json?sensor=true&key=AIzaSyCy2IUq1x6GJ7FosCEHV8j-1p9yezsb3Qs&"+
                    "reference=" + reference;


            new PlacesDetailsRequestTask().execute(url);

            //Toast.makeText(getActivity(), selected, Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        Bundle args = getArguments();
        if (args != null) {
            Double lat = args.getDouble("latitude");
            Double lon = args.getDouble("longitude");
            String orig = args.getString("orig");
            String dest = args.getString("dest");
            String date = args.getString("date");
            String time = args.getString("time");

            start_trip.setText(orig);
            dest_trip.setText(dest);
            time_button.setText(time);
            date_button.setText(date);

            String url = "http://api.reittiopas.fi/hsl/prod/?request=stops_area" +
                    "&user=theholeinmysole&pass=report6683&format=json" +
                    "&epsg_in=wgs84&epsg_out=wgs84&limit=20&diameter=1500&" +
                    "center_coordinate=" + lon + "," +
                    +lat;
            System.out.println(" URL : " + url);
            new RequestTask().execute(url);

        }


    }


    class PlacesDetailsRequestTask extends AsyncTask<String, String, String> {

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
            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);

                    JSONObject tmp = jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
                    JSONObject addressObj = jsonObj.getJSONObject("result");
                    dest_address = addressObj.getString("formatted_address");
                    dest_lat = tmp.getDouble("lat");
                    dest_lon = tmp.getDouble("lng");
                    //Toast.makeText(getActivity(), dest_address + "----"+dest_lat.toString() + ":" + dest_lon.toString(), Toast.LENGTH_SHORT).show();


                    //start_trip.setText("Helsinki");

                } catch (Exception ie) {
                    ie.printStackTrace();

                }
            }
        }


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
                ArrayAdapter<String> adapter;
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_auto, vals);
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


    @Override
    public void onStart() {
        super.onStart();

        //If user is signed in



        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        *//*Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateArticleView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateArticleView(mCurrentPosition);
        }*//*
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        //outState.putInt(ARG_POSITION, mCurrentPosition);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }




*//*

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case ActivityUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem

                    case Activity.RESULT_OK:

                        // Log the result
                        Log.d(ActivityUtils.APPTAG, getString(R.string.google_play_services_location_error_resolved));

                        // Display the result
                        *//*
*//*mConnectionState.setText(R.string.connected);
                        mConnectionStatus.setText(R.string.google_play_services_location_error_resolved);*//**//*

                        break;

                    // If any other result was returned by Google Play services

                    // If any other result was returned by Google Play services
                    default:

                        // Report that Google Play services was unable to resolve the problem.
                        Log.d(ActivityUtils.APPTAG, getString(R.string.no_resolution));
                }

                // If any other request code was received
            default:
                // Report that this Activity received an unknown requestCode
                Log.d(ActivityUtils.APPTAG,
                        getString(R.string.unknown_activity_request_code, requestCode));

                break;
        }
    }
*//*

*//*public static class TripFra extends DialogFragment implements View.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onClick(View v) {

    }
}*//*
public static class TimePickerFragment extends android.support.v4.app.DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    TimeSelectedListener mCallback;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        try {

            ((Button)getActivity().findViewById(R.id.timeButton)).setText(hourOfDay + ":" + minute);
            mCallback.timeselected(hourOfDay, minute);
        }
        catch (Exception e)
        {

        }

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (TimeSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TimeSelectedListener");
        }
    }

    // Container Activity must implement this interface
    public interface TimeSelectedListener {
        public void timeselected(int hours, int minute);
    }
}

    public static class DatePickerFragment extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        DateSelectedListener mCallback;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        // Container Activity must implement this interface
        public interface DateSelectedListener {
            public void dateselected(int year, int month, int day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            //month = month+1;
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.MONTH, month);
            String format = new SimpleDateFormat("yyyy:MM:dd").format(cal.getTime());

            Toast.makeText(
                    getActivity(),
                    ""+month,
                    Toast.LENGTH_LONG).show();
            try {
                ((Button)getActivity().findViewById(R.id.dateButton)).setText(format);
                mCallback.dateselected(year, month, day);

            }
            catch (Exception e)
            {

            }

        }


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            // This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception
            try {
                mCallback = (DateSelectedListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement DateSelectedListener");
            }
        }


    }
*/

}



