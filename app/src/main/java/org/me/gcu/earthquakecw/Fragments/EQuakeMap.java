package org.me.gcu.earthquakecw.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.me.gcu.earthquakecw.Data.EarthQuake;
import org.me.gcu.earthquakecw.UI.EarthQuakeActivity;
import org.me.gcu.earthquakecw.R;
import org.me.gcu.earthquakecw.Data.RetrieveData;
import org.me.gcu.earthquakecw.UI.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * fragment logic for the map fragment
 * As explained in the MainActivity, arrayList is used for better performance
 * Using HashMap to keep all the circles matched with specific earthquake, so they can be clicked
 * and redirected to specific data about the earthquake
 */

public class EQuakeMap extends Fragment {

    private ArrayList<EarthQuake> quakeList;
    private HashMap<Circle, EarthQuake> hashMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng UK = new LatLng(55.3781, -3.4360);
            hashMap = new HashMap<>();

            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(UK));
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(UK, 5);
            googleMap.animateCamera(yourLocation);
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            for (int i = 0; i < quakeList.size(); i++)
            {
                int color = colorCalc(quakeList.get(i).getMagnitude());
                Circle tempCyrcle = googleMap.addCircle( new CircleOptions()
                        .center(new LatLng(quakeList.get(i).getGlat(),quakeList.get(i).getGlong()))
                        .fillColor(color)
                        .strokeColor(color)
                        .clickable(true)
                        .radius(10000)); // In meters
                hashMap.put(tempCyrcle, quakeList.get(i));
            }

            googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                @Override
                public void onCircleClick(Circle circle) {
                    Intent i = new Intent(getActivity(), EarthQuakeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("earthQuake", hashMap.get(circle));
                    i.putExtras(bundle);
                    startActivity(i);

                }
            });

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if(savedInstanceState!=null) {
            quakeList = savedInstanceState.getParcelableArrayList("earthQuake");
        }
        else{
            quakeList = ((MainActivity) getActivity()).getData();
        }
        Log.e("MyTag", "Passed Map");

        return inflater.inflate(R.layout.fragment_e_quake_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    /**
     * Method to get a color between green and red based on the strength of the earthquake
     * for testing, max magnitude is set to 3 as all the earthquakes are max 2.3magnitude, so color
     * coding would be bearly seen
     * @param strength - of the earthquake
     * @return - color code to color the earthquake identifier
     */
    public int colorCalc(float strength)
    {
        int color = (Integer) new ArgbEvaluator().evaluate(strength/3f, 0x00ff00, 0xff0000);
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        return Color.parseColor(hexColor);
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