package com.hphays.nytimessearch;

/**
 * Created by hhays on 11/16/16.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.hphays.nytimessearch.R;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import static android.R.attr.format;

public class DatePickerFragment extends DialogFragment {

    // Defines the listener interface
    public interface OnDateSetListener {
        void onDateSetListener(String inputText);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);

        int month = c.get(Calendar.MONTH);

        int day = c.get(Calendar.DAY_OF_MONTH);

        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getTargetFragment();

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);

    }
}
