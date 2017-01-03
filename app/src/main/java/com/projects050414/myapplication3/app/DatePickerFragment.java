package com.projects050414.myapplication3.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by obelix on 11/05/2014.
 */
public class DatePickerFragment extends android.support.v4.app.DialogFragment
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
                "" + month,
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
