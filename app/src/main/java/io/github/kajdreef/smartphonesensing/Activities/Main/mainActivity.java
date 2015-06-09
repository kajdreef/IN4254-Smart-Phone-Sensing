package io.github.kajdreef.smartphonesensing.Activities.Main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.github.kajdreef.smartphonesensing.Activities.Test.TestActivity;
import io.github.kajdreef.smartphonesensing.R;

/**
 * Created by kajdreef on 09/06/15.
 */
public class mainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu);
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

    public void startLocalization(View view){
        Intent startLoc = new Intent(this, LocalizationActivity.class);
        startActivity(startLoc);
    }

    public void startClassification(View view){
        Intent startClass = new Intent(this, ServiceAndQueueActivity.class);
        startActivity(startClass);
    }

    public void startTest(View view){
        Intent startClass = new Intent(this, TestActivity.class);
        startActivity(startClass);
    }
}
