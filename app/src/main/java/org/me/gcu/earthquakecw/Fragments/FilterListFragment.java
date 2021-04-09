package org.me.gcu.earthquakecw.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.me.gcu.earthquakecw.Data.EarthQuake;
import org.me.gcu.earthquakecw.UI.EarthQuakeActivity;
import org.me.gcu.earthquakecw.R;
import org.me.gcu.earthquakecw.UI.MainActivity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * This fragment is used to process the data, and input from the filter fragment
 * As explained in the MainActivity, arrayList is used for better performance
 * I believe this can be improved with some automation later instead calling everything separately
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FilterListFragment extends Fragment{


    // TODO: Rename and change types of parameters
    private TextView dates, northerlyLocation, northerlyStrength, southerlyLocation, southerlyStrength,
            westerlyLocation, westerlyStrength, easterlyLocation, easterlyStrength,
            lMagLocation, lMagStrength, deepestLocation, deepestStrength,
            shallowestLocation, shallowestStrength;
    private LinearLayout northerly, southerly, westerly, easterly, lMag, deepest, shallowest;
    private Button buttonSetDates, buttonFilterList;
    private MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder;
    private ArrayList<EarthQuake> quakeList, filteredQuakeList;
    private LocalDateTime date1, date2;
    private EarthQuake largestMQ, northerlyQ, southerlyQ, easterlyQ, westerlyQ, deepestQ, shallowestQ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_filter_list, container, false);

        if(savedInstanceState!=null) {
            quakeList = savedInstanceState.getParcelableArrayList("earthQuake");
        }
        else{
            quakeList = ((MainActivity) getActivity()).getData();
        }
        Log.e("MyTag", "Passed Filter");
        dates= (TextView) v.findViewById(R.id.dates);
        buttonSetDates = (Button) v.findViewById(R.id.buttonSetDates);
        buttonFilterList = (Button) v.findViewById(R.id.buttonFilterList);
        filteredQuakeList = new ArrayList<EarthQuake>();

        northerly = (LinearLayout) v.findViewById(R.id.Northerly) ;
        southerly = (LinearLayout) v.findViewById(R.id.Southerly) ;
        westerly = (LinearLayout) v.findViewById(R.id.Westerly) ;
        easterly = (LinearLayout) v.findViewById(R.id.Easterly) ;
        lMag = (LinearLayout) v.findViewById(R.id.Lmagnitude) ;
        deepest = (LinearLayout) v.findViewById(R.id.Deepest) ;
        shallowest = (LinearLayout) v.findViewById(R.id.Shallowest) ;
        northerlyLocation = (TextView) v.findViewById(R.id.NortherlyLocation);
        northerlyStrength = (TextView) v.findViewById(R.id.NortherlyStrength);
        southerlyLocation = (TextView) v.findViewById(R.id.SoutherlyLocation);
        southerlyStrength = (TextView) v.findViewById(R.id.SoutherlyStrength);
        westerlyLocation = (TextView) v.findViewById(R.id.WesterlyLocation);
        westerlyStrength = (TextView) v.findViewById(R.id.WesterlyStrength);
        easterlyLocation = (TextView) v.findViewById(R.id.EasterlyLocation);
        easterlyStrength = (TextView) v.findViewById(R.id.EasterlyStrength);
        lMagLocation = (TextView) v.findViewById(R.id.LmagnitudeLocation);
        lMagStrength = (TextView) v.findViewById(R.id.LmagnitudeStrength);
        deepestLocation = (TextView) v.findViewById(R.id.DeepestLocation);
        deepestStrength = (TextView) v.findViewById(R.id.DeepestStrength);
        shallowestLocation = (TextView) v.findViewById(R.id.ShallowestLocation);
        shallowestStrength = (TextView) v.findViewById(R.id.ShallowestStrength);

        buttonSetDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = MaterialDatePicker.Builder.dateRangePicker();
                MaterialDatePicker<androidx.core.util.Pair<Long, Long>> picker = builder.build();
                picker.show(getActivity().getSupportFragmentManager(), picker.toString());
                picker.addOnPositiveButtonClickListener((new MaterialPickerOnPositiveButtonClickListener<androidx.core.util.Pair<Long,Long>>() {
                    @Override
                    public void onPositiveButtonClick(androidx.core.util.Pair<Long, Long> selection) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");
                        date1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.first), TimeZone.getDefault().toZoneId());
                        date2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.second), TimeZone.getDefault().toZoneId());
                        String post = "From: " + date1.format(formatter) + " To: " + date2.format(formatter);
                        dates.setText(post);
                        buttonFilterList.setVisibility(View.VISIBLE);
                        //Log.d("DatePicker Activity", "Date String = " + dateText + ":: Date epoch value = ${it}");
                    }
                }));
            }
        });


        buttonFilterList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredQuakeList.removeAll(filteredQuakeList);
                for(int i = 0; i < quakeList.size(); i++)
                {
                    if (quakeList.get(i).getDate().isAfter(date1) && quakeList.get(i).getDate().isBefore(date2)) {
                        filteredQuakeList.add(quakeList.get(i));
                    }
                }
                if(filteredQuakeList.size() > 0) {
                    largestMQ = largestMagnitude(filteredQuakeList);
                    northerlyQ = northerly(filteredQuakeList);
                    southerlyQ = southerly(filteredQuakeList);
                    easterlyQ = easterly(filteredQuakeList);
                    westerlyQ = westerly(filteredQuakeList);
                    deepestQ = deepest(filteredQuakeList);
                    shallowestQ = shallowest(filteredQuakeList);
                }
                else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Alert")
                            .setMessage("There are no earthquakes in the range \n please select a new range.")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                Log.d("DatePicker Activity", String.valueOf(filteredQuakeList.size()));
            }
        });

        lMag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EarthQuakeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("earthQuake", largestMQ);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        northerly.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EarthQuakeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("earthQuake", northerlyQ);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        southerly.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EarthQuakeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("earthQuake", southerlyQ);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        easterly.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EarthQuakeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("earthQuake", easterlyQ);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        westerly.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EarthQuakeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("earthQuake", westerlyQ);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        deepest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EarthQuakeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("earthQuake", deepestQ);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        shallowest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EarthQuakeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("earthQuake", shallowestQ);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        //
        return v;
    }

    public EarthQuake largestMagnitude(ArrayList<EarthQuake> eQuakeList)
    {
        float maxMag = 0;
        EarthQuake eQuake = null;

        for(int i = 0; i < eQuakeList.size(); i++)
        {
            if(eQuakeList.get(i).getMagnitude() > maxMag)
            {
                maxMag = eQuakeList.get(i).getMagnitude();
                eQuake = eQuakeList.get(i);
            }
        }
        Log.d("DatePicker Activity", eQuake.toString());
        lMag.setVisibility(View.VISIBLE);
        lMagLocation.setText(eQuake.getLocation());
        lMagStrength.setText(String.format("Magnitude of: %s", eQuake.getMagnitude()));
        return eQuake;
    }

    public EarthQuake northerly(ArrayList<EarthQuake> eQuakeList)
    {
        float maxLat = -Float.MAX_VALUE;
        EarthQuake eQuake = null;

        for(int i = 0; i < eQuakeList.size(); i++)
        {
            if(eQuakeList.get(i).getGlat() > maxLat)
            {
                maxLat = eQuakeList.get(i).getGlat();
                eQuake = eQuakeList.get(i);
            }
        }
        Log.d("DatePicker Activity", eQuake.toString());
        northerly.setVisibility(View.VISIBLE);
        northerlyLocation.setText(eQuake.getLocation());
        northerlyStrength.setText(String.format("Magnitude of: %s", eQuake.getMagnitude()));
        return eQuake;
    }

    public EarthQuake southerly(ArrayList<EarthQuake> eQuakeList)
    {
        float minLat = Float.MAX_VALUE;
        EarthQuake eQuake = null;

        for(int i = 0; i < eQuakeList.size(); i++)
        {
            if(eQuakeList.get(i).getGlat() < minLat)
            {
                minLat = eQuakeList.get(i).getGlat();
                eQuake = eQuakeList.get(i);
            }
        }
        Log.d("DatePicker Activity", eQuake.toString());
        southerly.setVisibility(View.VISIBLE);
        southerlyLocation.setText(eQuake.getLocation());
        southerlyStrength.setText(String.format("Magnitude of: %s", eQuake.getMagnitude()));
        return eQuake;
    }

    public EarthQuake easterly(ArrayList<EarthQuake> eQuakeList)
    {
        float maxLong = -Float.MAX_VALUE;
        EarthQuake eQuake = null;

        for(int i = 0; i < eQuakeList.size(); i++)
        {
            if(eQuakeList.get(i).getGlong() > maxLong)
            {
                maxLong = eQuakeList.get(i).getGlong();
                eQuake = eQuakeList.get(i);
            }
        }
        Log.d("DatePicker Activity", eQuake.toString());
        easterly.setVisibility(View.VISIBLE);
        easterlyLocation.setText(eQuake.getLocation());
        easterlyStrength.setText(String.format("Magnitude of: %s", eQuake.getMagnitude()));
        return eQuake;
    }

    public EarthQuake westerly(ArrayList<EarthQuake> eQuakeList)
    {
        float maxLong = Float.MAX_VALUE;
        EarthQuake eQuake = null;

        for(int i = 0; i < eQuakeList.size(); i++)
        {
            if(eQuakeList.get(i).getGlong() < maxLong)
            {
                maxLong = eQuakeList.get(i).getGlong();
                eQuake = eQuakeList.get(i);
            }
        }
        Log.d("DatePicker Activity", eQuake.toString());
        westerly.setVisibility(View.VISIBLE);
        westerlyLocation.setText(eQuake.getLocation());
        westerlyStrength.setText(String.format("Magnitude of: %s", eQuake.getMagnitude()));
        return eQuake;
    }

    public EarthQuake deepest(ArrayList<EarthQuake> eQuakeList)
    {
        int depth = -Integer.MAX_VALUE;
        EarthQuake eQuake = null;

        for(int i = 0; i < eQuakeList.size(); i++)
        {
            if(eQuakeList.get(i).getDepth() > depth)
            {
                depth = eQuakeList.get(i).getDepth();
                eQuake = eQuakeList.get(i);
            }
        }
        Log.d("DatePicker Activity", "Deepest " + eQuake.toString());
        deepest.setVisibility(View.VISIBLE);
        deepestLocation.setText(eQuake.getLocation());
        deepestStrength.setText(String.format("Magnitude of: %s", eQuake.getMagnitude()));
        return eQuake;
    }

    public EarthQuake shallowest(ArrayList<EarthQuake> eQuakeList)
    {
        int depth = Integer.MAX_VALUE;
        EarthQuake eQuake = null;

        for(int i = 0; i < eQuakeList.size(); i++)
        {
            if(eQuakeList.get(i).getDepth() < depth)
            {
                depth = eQuakeList.get(i).getDepth();
                eQuake = eQuakeList.get(i);
            }
        }
        Log.d("DatePicker Activity", "Shallowest " + eQuake.toString());
        shallowest.setVisibility(View.VISIBLE);
        shallowestLocation.setText(eQuake.getLocation());
        shallowestStrength.setText(String.format("Magnitude of: %s", eQuake.getMagnitude()));
        return eQuake;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putParcelableArrayList("earthQuake", quakeList);
        // etc.
    }
}