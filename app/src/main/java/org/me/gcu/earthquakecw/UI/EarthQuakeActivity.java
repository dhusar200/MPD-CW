package org.me.gcu.earthquakecw.UI;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.me.gcu.earthquakecw.Data.EarthQuake;
import org.me.gcu.earthquakecw.R;

import java.time.format.DateTimeFormatter;

/**
 * Activity which displays specific data about selected earthquake
 * As explained in the MainActivity, arrayList is used for better performance
 */

public class EarthQuakeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView location, category, date, depth, magnitude, geo;
    private FloatingActionButton buttonBack;
    private EarthQuake earthQuakes;
    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        location = (TextView) findViewById(R.id.location);
        category = (TextView) findViewById(R.id.category);
        date = (TextView) findViewById(R.id.date);
        depth = (TextView) findViewById(R.id.depth);
        magnitude = (TextView) findViewById(R.id.magnitude);
        geo = (TextView) findViewById(R.id.geo);
        map = (MapView) findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);

        buttonBack = (FloatingActionButton) findViewById(R.id.backButton);
        buttonBack.setOnClickListener(this);

        getData();
        setData();

        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsInitializer.initialize(getApplicationContext());
                LatLng UK = new LatLng(earthQuakes.getGlat(), earthQuakes.getGlong());

                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(UK, 5);
                googleMap.animateCamera(yourLocation);
                googleMap.getUiSettings().setAllGesturesEnabled(false);
                googleMap.addMarker(new MarkerOptions().position(UK).title(earthQuakes.getLocation()));
            }
        });

    }

    /**
     * Getting the data for the specific earthquake which was passed in the intent as extra
     */
    private void getData()
    {
        if(getIntent().hasExtra("earthQuake"))
        {
            earthQuakes = getIntent().getParcelableExtra("earthQuake");
        }
        else
        {
            Toast.makeText(this, "No data have been passed.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Setting the data for the earthquake
     */
    private void setData()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss");
        location.setText(earthQuakes.getLocation());
        category.setText(earthQuakes.getCategory());
        date.setText(earthQuakes.getDate().format(formatter));
        depth.setText(String.format("%skm", String.valueOf(earthQuakes.getDepth())));
        magnitude.setText(String.valueOf(earthQuakes.getMagnitude()));
        geo.setText(convert(earthQuakes.getGlat(), earthQuakes.getGlong()));
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            finish();
        }
    }

    /**
     * onResume which is needed to correctly load the map
     */
    @Override
    public void onResume() {
        map.onResume();
        super.onResume();
    }

    /**
     *
     * @param latitude - to transfer
     * @param longitude - to transfer
     * @return returns coordinate of earthquake in S/N & E/W
     */
    private String convert(double latitude, double longitude) {
        StringBuilder builder = new StringBuilder();

        if (latitude < 0) {
            builder.append("S ");
        } else {
            builder.append("N ");
        }

        String latitudeDegrees = Location.convert(Math.abs(latitude), Location.FORMAT_SECONDS);
        String[] latitudeSplit = latitudeDegrees.split(":");
        builder.append(latitudeSplit[0]);
        builder.append("°");
        builder.append(latitudeSplit[1]);
        builder.append("'");
        builder.append(latitudeSplit[2]);
        builder.append("\"");

        if (longitude < 0) {
            builder.append("\n");
            builder.append("W ");
        } else {
            builder.append("\n");
            builder.append("E ");
        }

        String longitudeDegrees = Location.convert(Math.abs(longitude), Location.FORMAT_SECONDS);
        String[] longitudeSplit = longitudeDegrees.split(":");
        builder.append(longitudeSplit[0]);
        builder.append("°");
        builder.append(longitudeSplit[1]);
        builder.append("'");
        builder.append(longitudeSplit[2]);
        builder.append("\"");

        return builder.toString();
    }
}
