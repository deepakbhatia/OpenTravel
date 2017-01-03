package com.projects050414.myapplication3.app;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;

/**
 * Created by obelix on 16/05/2014.
 */
public class GooglePlusLoginFragment extends Activity implements View.OnClickListener, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    // The core Google+ client.
    private PlusClient mPlusClient;
    /*
     * Note if updates have been turned on. Starts out as "false"; is set to "true" in the
     * method handleRequestSuccess of LocationUpdateReceiver.
     *
     */
    boolean mUpdatesRequested = false;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
   * us from starting further intents.
   */
    private boolean mIntentInProgress;
    /* Track whether the sign-in button has been clicked so that we know to resolve
     * all issues preventing sign-in without waiting.
     */
    private boolean mSignInClicked;

    /* Store the connection result from onConnectionFailed callbacks so that we can
     * resolve them when the user clicks sign-in.
     */
    private ConnectionResult mConnectionResult;

    //private Button gpluslogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_login);


        findViewById(R.id.plus_sign_in_button).setOnClickListener(this);
        // Note that location updates are off until the user turns them on
        mUpdatesRequested = false;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        mPlusClient = new PlusClient.Builder(this, this, this).build();

    }

    public void onClick(View view) {
        if (view.getId() == R.id.plus_sign_in_button
                && !mGoogleApiClient.isConnected()) {

            mSignInClicked = true;
            resolveSignInError();
        }
    }

    public void onStart() {

        super.onStart();

        /*
         * Connect the client. Don't re-start any requests here;
         * instead, wait for onResume()
         */
        mPlusClient.connect();

        mGoogleApiClient.connect();


    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

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
        if (!mIntentInProgress) {
            // Store the ConnectionResult so that we can use it later when the user clicks
            // 'sign-in'.
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError(connectionResult);
            }
        }


    }
    public void resolveSignInError(ConnectionResult connectionResult)
    {
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                mIntentInProgress = true;
                startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
                // Log the error
                e.printStackTrace();
            }
        } else {

            // If no resolution is available, display a dialog to the user with the error.
            //showErrorDialog(connectionResult.getErrorCode());
        }
    }

    /*
     * Called when the Activity is no longer visible at all.
     * Stop updates and disconnect.
     */
    @Override
    public void onStop() {

        super.onStop();


        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }


    @Override
    protected void onDestroy() {
        Log.w(LocationUtils.APPTAG, "Google Plus Login destoryed");

        super.onDestroy();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        //CheckEnableGPS();
    }
    @Override
    public void onConnected(Bundle bundle) {


        mSignInClicked = false;
        Toast.makeText(this, "User is connected! "+mPlusClient.getAccountName(), Toast.LENGTH_LONG).show();
        /*Toast.makeText(getApplicationContext(), "Signed in as: "+ mPlusClient.getAccountName()
                , Toast.LENGTH_LONG).show();


        Intent intent = new Intent(this,getCallingActivity().getClass());
        startActivity(intent);*/


    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }
    /* A helper method to resolve the current ConnectionResult error. */
    public void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }


}
