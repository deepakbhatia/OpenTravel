package com.projects050414.myapplication3.app;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by obelix on 23/10/2014.
 */
public class PlacesDetailsRequestTask extends AsyncTask<String, String, String> {

    private String type="";

    public PlacesDetailsRequestTask()
    {
    }
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
                    GlobalValues.dest_address= addressObj.getString("formatted_address");
                    GlobalValues.dest_lat = tmp.getDouble("lat");
                    GlobalValues.dest_lon = tmp.getDouble("lng");
                }

                else
                {

                    GlobalValues.orig_address= addressObj.getString("formatted_address");
                    GlobalValues.start_lat = tmp.getDouble("lat");
                    GlobalValues.start_lon = tmp.getDouble("lng");
                }


            } catch (Exception ie) {
                ie.printStackTrace();

            }
        }
    }
}
