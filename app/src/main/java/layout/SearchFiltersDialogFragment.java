package layout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;

import com.hphays.nytimessearch.DatePickerFragment;
import com.hphays.nytimessearch.R;
import com.hphays.nytimessearch.SearchFilters;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import org.parceler.Parcels;

import static android.R.attr.fragment;
// ...

public class SearchFiltersDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    CheckBox artCheckbox;
    CheckBox fashionCheckbox;
    CheckBox sportsCheckbox;

    RadioButton newest;
    RadioButton oldest;

    EditText launchDatePicker;

    SearchFilters filters;

    @Override
    public void onClick(View view) {
        if (artCheckbox.isChecked()) {
            filters.setArts(true);
        } else {
            filters.setArts(false);
        }

        if (fashionCheckbox.isChecked()) {
            filters.setFashionAndStyle(true);
        } else {
            filters.setFashionAndStyle(false);
        }

        if (sportsCheckbox.isChecked()) {
            filters.setSports(true);
        } else {
            filters.setSports(false);
        }

        if (newest.isChecked()) {
            filters.setNewest(true);
        } else {
            filters.setNewest(false);
        }

        if (oldest.isChecked()) {
            filters.setOldest(true);
        } else {
            filters.setOldest(false);
        }



        //pass filters object back to the activity
        // Return input text back to activity through the implemented listener
        EditNameDialogListener listener = (EditNameDialogListener) getActivity();
        listener.onFinishEditDialog(Parcels.wrap(filters));
        // Close the dialog and return back to the parent activity
        dismiss();


        //close the fragment
    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(Parcelable filters);
    }



    public SearchFiltersDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SearchFiltersDialogFragment newInstance(String title) {
        SearchFiltersDialogFragment frag = new SearchFiltersDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_filters, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filters = new SearchFilters();

        artCheckbox = (CheckBox) view.findViewById(R.id.arts);
        fashionCheckbox = (CheckBox) view.findViewById(R.id.fashion);
        sportsCheckbox = (CheckBox) view.findViewById(R.id.sports);

        newest = (RadioButton) view.findViewById(R.id.newest);
        oldest = (RadioButton) view.findViewById(R.id.oldest);

        Button btnSave = (Button) view.findViewById(R.id.saveBtn);

        launchDatePicker = (EditText) view.findViewById(R.id.launchDatePicker);

        launchDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        btnSave.setOnClickListener(this);
    }
    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(this, 300);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String time = format.format(c.getTime());
        filters.setBeginDate(time);
        launchDatePicker.setText(time);
    }
}
