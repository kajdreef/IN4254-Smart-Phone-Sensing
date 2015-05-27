package LocalizationTest;

import android.test.ActivityTestCase;

import org.junit.Before;
import org.junit.Test;

import io.github.kajdreef.smartphonesensing.Localization.FloorPlan;
import io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering.ParticleFilter;

public class ParticleFilterTest extends ActivityTestCase {
    ParticleFilter pf;
    @Before
    public void setUp(){
        pf = new ParticleFilter(1000,new FloorPlan());
    }

    @Test
    public void testMovement(){
        pf.movement(5f, 5f);
    }
}
