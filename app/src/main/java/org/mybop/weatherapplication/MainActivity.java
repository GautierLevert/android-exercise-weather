package org.mybop.weatherapplication;

import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentByTag;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String TAG_LOCATION = "location";

    @FragmentByTag(TAG_LOCATION)
    protected LocationFragment locationFragment;

    @AfterViews
    protected void locationFragment() {
        if (locationFragment == null) {
            locationFragment = LocationFragment_.builder()
                    .build();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(locationFragment, TAG_LOCATION)
                    .commit();
        }
    }
}
