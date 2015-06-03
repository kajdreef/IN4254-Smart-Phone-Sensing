package io.github.kajdreef.smartphonesensing.Activities;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ObserverSensor;
import io.github.kajdreef.smartphonesensing.Localization.FloorPlan;
import io.github.kajdreef.smartphonesensing.Localization.LocalizationView;
import io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering.ParticleFilter;
import io.github.kajdreef.smartphonesensing.R;

/**
 * Created by kajdreef on 27/05/15.
 */
public class TestActivity extends ActionBarActivity implements ObserverSensor {

    private FloorPlan floorPlan;
    private LocalizationView localizationView;
    private ParticleFilter pf;
    public int WINDOW_SIZE;

    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executor  = Executors.newSingleThreadExecutor();

        floorPlan = new FloorPlan();

        // Get resources from the resource folder
        Resources res = this.getResources();
        WINDOW_SIZE = res.getInteger(R.integer.WINDOW_SIZE_ACC);

        // Generate x amount of particles
        pf = new ParticleFilter(1000, floorPlan);


        // Get window size;
        Point windowSize = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(windowSize);

        // Create the localization view with screen orientation in landscape and set it
        localizationView = new LocalizationView(this, floorPlan.getPath(), pf.getParticles(), windowSize.x, windowSize.y);

        setContentView(localizationView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.updateMovement();
    }

@Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Update the location of the particles.
     */
    public void updateMovement() {
        // Movement in +x direction
        for (int i = 0; i < 70; i++) {
            // Make Runnable and add it to the executor
            Runnable runMovement = new Runnable() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                    pf.movement(0f, 1.0f, 160);
                    localizationView.setParticles(pf.getParticles());
                    localizationView.post(new Runnable() {
                        public void run() {
                            localizationView.invalidate();
                        }
                    });
                }
            };
            executor.execute(runMovement);
        }

        // Movement in -y direction
        for (int i = 0; i < 5; i++) {
            // Make Runnable and add it to the executor
            Runnable runMovement = new Runnable() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                    pf.movement(90f, 1.0f, 160);
                    localizationView.setParticles(pf.getParticles());
                    localizationView.post(new Runnable() {
                        public void run() {
                            localizationView.invalidate();
                        }
                    });
                }
            };
            executor.execute(runMovement);
        }

        // Movement in +y direction
        for (int i = 0; i < 5; i++) {
            // Make Runnable and add it to the executor
            Runnable runMovement = new Runnable() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                    pf.movement(-90f, 1.0f, 160);
                    localizationView.setParticles(pf.getParticles());
                    localizationView.post(new Runnable() {
                        public void run() {
                            localizationView.invalidate();
                        }
                    });
                }
            };
            executor.execute(runMovement);
        }

        executor.shutdown();
        Log.d("Executor","All Threads Finished!");
    }

    public void update(int SensorType){

    }
}
