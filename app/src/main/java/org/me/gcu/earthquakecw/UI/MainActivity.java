package org.me.gcu.earthquakecw.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.me.gcu.earthquakecw.EarthQuake;
import org.me.gcu.earthquakecw.Fragments.EQuakeListFragment;
import org.me.gcu.earthquakecw.Fragments.EQuakeMap;
import org.me.gcu.earthquakecw.R;
import org.me.gcu.earthquakecw.RetrieveData;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private RetrieveData getXML;
    private ArrayList<EarthQuake> quakeList = new ArrayList<>();
    private SwipeRefreshLayout swipe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FROM HERE

        EarthQuake earthQuake = new EarthQuake();

        //quakeList = downloadData();
        quakeList.add(earthQuake);

        final Handler handler = new Handler();
            handler.postDelayed( new Runnable() {

                @Override
                public void run() {
                    Log.e("MyTag","Refreshed Data");
                    quakeList = downloadData();
                }
            }, 10 * 1000 );


        //TOHERE

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavController navController = Navigation.findNavController(this,  R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public ArrayList<EarthQuake> getData()
    {
        Log.e("MyTag", "Called");
        return quakeList;
    }

    public ArrayList<EarthQuake> downloadData()
    {
        getXML = new RetrieveData();
        try {
            getXML.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<EarthQuake>(getXML.getAlist());
    }
}