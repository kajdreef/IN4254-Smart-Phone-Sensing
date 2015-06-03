package io.github.kajdreef.smartphonesensing.Localization;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityMonitoring;

/**
 * Created by kajdreef on 03/06/15.
 *
 * Runnable implementation so the every movement and activity update
 * can be computed in its own thread
 *
 */
public class RunMovement implements Runnable {
    private ArrayList<Float> accelXClone = new ArrayList<>();
    private ArrayList<Float> accelYClone = new ArrayList<>();
    private ArrayList<Float> accelZClone = new ArrayList<>();

    private ArrayList<Float> magnXClone = new ArrayList<>();
    private ArrayList<Float> magnYClone = new ArrayList<>();
    private ArrayList<Float> magnZClone = new ArrayList<>();

    //Monitors
    private ActivityMonitoring am;
    private LocalizationMonitoring lm;

    // View that needs to be updated
    private LocalizationView localizationView;

    public RunMovement(ArrayList<Float> xA, ArrayList<Float> yA, ArrayList<Float> zA,
                       ArrayList<Float> xM, ArrayList<Float> yM, ArrayList<Float> zM,
                       ActivityMonitoring _am, LocalizationMonitoring _lm, LocalizationView _localizationView)
    {
        this.accelXClone = (ArrayList<Float>) xA.clone();
        this.accelYClone = (ArrayList<Float>) yA.clone();
        this.accelZClone = (ArrayList<Float>) zA.clone();
        this.magnXClone = (ArrayList<Float>) xM.clone();
        this.magnYClone = (ArrayList<Float>) yM.clone();
        this.magnZClone = (ArrayList<Float>) zM.clone();
        this.am = _am;
        this.lm = _lm;
        this.localizationView = _localizationView;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        this.am.update(accelXClone, accelYClone, accelZClone);

        if (this.lm.update( accelXClone, accelYClone, accelZClone,
                magnXClone, magnYClone, magnZClone)) {

            this.localizationView.setParticles(this.lm.getParticles());

            this.localizationView.post(new Runnable() {
                public void run() {
                    localizationView.invalidate();
                }
            });
        }
    }
}