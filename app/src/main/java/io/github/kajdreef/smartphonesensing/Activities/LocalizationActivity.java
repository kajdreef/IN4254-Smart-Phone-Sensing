package io.github.kajdreef.smartphonesensing.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Observer;
import io.github.kajdreef.smartphonesensing.Localization.FloorPlan;
import io.github.kajdreef.smartphonesensing.Localization.LocalizationView;
import io.github.kajdreef.smartphonesensing.Localization.Particle;
import io.github.kajdreef.smartphonesensing.R;

/**
 * Created by kajdreef on 15/05/15.
 */
public class LocalizationActivity extends ActionBarActivity implements Observer{

    FloorPlan floorPlan;
    ArrayList<Particle> particleList;
    LocalizationView localizationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        floorPlan = new FloorPlan();

        particleList = new ArrayList<>();

        generateParticles(10000);
//        Particle p = new Particle(50f, 50f);
//        if(floorPlan.particleInside(p)){
//            particleList.add(p);
//        }
//        else{
//            Log.d("Particle Placement", "Out Of Bound!!");
//        }
//
//        p = new Particle(100f, 50f);
//        if(floorPlan.particleInside(p)){
//            particleList.add(p);
//        }
//        else{
//            Log.d("Particle Placement", "Out Of Bound!!");
//        }
//
//        p = new Particle(500f, 130f);
//        if(floorPlan.particleInside(p)){
//            particleList.add(p);
//        }
//        else{
//            Log.d("Particle Placement", "Out Of Bound!!");
//        }
//
//
        localizationView = new LocalizationView(this, floorPlan.getPath(), particleList);
//        for(Particle pEntry : particleList){
//            pEntry.updateLocation(70f,0f);
//            if(floorPlan.particleCollision(pEntry)){
//                pEntry.setCurrentLocation(pEntry.getPreviousLocation());
//                Log.d("Particle Collision", "Collision detected!!");
//            }
//        }
        setContentView(localizationView);
    }

    public void generateParticles(int numOfParticles){
        int i = 0;
        int height = floorPlan.getHeight();
        int width = floorPlan.getWidth();

        while(i < numOfParticles){
            Particle p = new Particle((float)(Math.random() * width), (float)(Math.random()* height));
            if(floorPlan.particleInside(p)){
                Log.d("Particle Location: " ,p.getCurrentLocation().getX() + ", " + p.getCurrentLocation().getY());
                particleList.add(p);
                i++;
            }
        }
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

    public void update() {
        particleList.get(0).updateLocation(particleList.get(0).getCurrentLocation().getX()+5, particleList.get(0).getCurrentLocation().getY()+5);
        localizationView.setParticles(particleList);
    }
}
