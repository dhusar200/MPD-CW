package org.me.gcu.earthquakecw.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.me.gcu.earthquakecw.Data.EarthQuake;
import org.me.gcu.earthquakecw.R;
import org.me.gcu.earthquakecw.Data.RetrieveData;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Main activity of the application, used to start the retrieve data and create ArrayList<EarthQuake>
 * I have used ArrayList instead of list/linked list as it has better performance if its not being edited
 * such as adding new earthquakes etc. As this is the case, it is better to use ArrayList - this will
 * be the case also for all the fragments.
 */

public class MainActivity extends AppCompatActivity {

    private RetrieveData getXML;
    private ArrayList<EarthQuake> quakeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null) {
            quakeList = savedInstanceState.getParcelableArrayList("earthQuake");
        }
        else{
            //For testing purposes to show the refresh data functionality
            //quakeList = downloadData();
            EarthQuake earthQuake = new EarthQuake();
            quakeList.add(earthQuake);
            refresh();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavController navController = Navigation.findNavController(this,  R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    /**
     * Method which fragments use to get the data from their parent activity (this-one)
     * @return return earthQuake list
     */
    public ArrayList<EarthQuake> getData()
    {
        Log.e("MyTag", "Called");
        return quakeList;
    }

    /**
     * Calls the class to get the data from the XML file
     * @return Arraylist of earthquakes
     */
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putParcelableArrayList("earthQuake", quakeList);
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        quakeList = savedInstanceState.getParcelableArrayList("earthQuake");
    }

    public void refresh()
    {
        // Refreshing EarthQuake list every 10 seconds (10s only for testing)
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                Log.e("MyTag","Refreshed Data");
                quakeList = downloadData();
                refresh();
            }
        }, 10 * 1000 );
    }
}