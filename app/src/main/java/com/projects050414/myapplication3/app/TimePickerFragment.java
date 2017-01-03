package com.projects050414.myapplication3.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by obelix on 11/05/2014.
 */
public class TimePickerFragment extends android.support.v4.app.DialogFragment
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
        String hours = "";
        if(hourOfDay<10)
        {
            hours = "0"+hourOfDay;
        }
        else
            hours = ""+hourOfDay;
        try {

            ((Button)getActivity().findViewById(R.id.timeButton)).setText(hours + ":" + minute);
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

