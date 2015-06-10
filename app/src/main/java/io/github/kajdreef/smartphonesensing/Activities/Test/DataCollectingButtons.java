package io.github.kajdreef.smartphonesensing.Activities.Test;

import android.content.res.Resources;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.github.kajdreef.smartphonesensing.Sensor.Accelerometer;
import io.github.kajdreef.smartphonesensing.Sensor.AbstractSensor;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ObserverSensor;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Utils.Reader;


public class DataCollectingButtons extends ActionBarActivity implements ObserverSensor {

    SensorManager sm;
    AbstractSensor accelerometer;

    Button walking;
    Button queueing;
    TextView t;

    boolean initAccel = false;
    Reader read;

    private void initAccelerometerAndButtons(){
        initAccel = true;

        t = (TextView) this.findViewById(R.id.state);

        Resources res = this.getResources();
        String acceleroFileLocation = res.getString(R.string.accelerometer_data_file);

        accelerometer = new Accelerometer(sm, acceleroFileLocation);
        accelerometer.attach(this);
        // Create walk button, when clicked on the button state will change state to WALK.
        walking = (Button) findViewById(R.id.walking);
        walking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Accelerometer.setState(Type.WALK);
                if(initAccel) {
                    accelerometer.register();
                    initAccel = false;
                }
                else{
                    accelerometer.unregister();
                    initAccel = true;
                    Accelerometer.setState(Type.NONE);
                    update(0);
                }
            }
        });

        // Create queueing button, when clicked on the button state will change state to QUEUE.
        queueing = (Button) findViewById(R.id.queueing);
        queueing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Accelerometer.setState(Type.QUEUE);
                if(initAccel) {
                    accelerometer.register();
                    initAccel = false;
                }
                else{
                    accelerometer.unregister();
                    initAccel = true;
                    Accelerometer.setState(Type.NONE);
                    update(0);
                }
            }
        });
    }


    public void initReader(){
        Resources res = this.getResources();
        String fileLocation = res.getString(R.string.accelerometer_data_file);
        read = new Reader(this, fileLocation);
        read.readData();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_collection_menu);
        sm =(SensorManager)getSystemService(SENSOR_SERVICE);


        initAccelerometerAndButtons();

//        initReader();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
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
    public void onDestroy(){
        super.onDestroy();
        if(initAccel)
            accelerometer.unregister();
    }

    public void update(int SensorType){
        t.setText(Accelerometer.getState().toString());
    }
}
