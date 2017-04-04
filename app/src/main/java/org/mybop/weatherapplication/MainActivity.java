package org.mybop.weatherapplication;

import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentByTag;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu)
public class MainActivity extends AppCompatActivity {

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

    @OptionsItem(R.id.action_settings)
    protected void actionSettings() {
        SettingsActivity_.intent(this)
                .start();
    }
}
