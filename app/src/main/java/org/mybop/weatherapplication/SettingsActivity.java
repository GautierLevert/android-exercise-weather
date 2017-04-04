package org.mybop.weatherapplication;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.tmtron.greenannotations.EventBusGreenRobot;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.greenrobot.eventbus.EventBus;
import org.mybop.weatherapplication.openweathermap.WeatherResponse;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends AppCompatActivity {

    @ViewById(R.id.metricSpinner)
    protected Spinner metricSpinner;

    @Pref
    protected Preferences_ preferences;

    @EventBusGreenRobot
    protected EventBus eventBus;

    @AfterViews
    protected void initSpinner() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.measurement, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metricSpinner.setAdapter(adapter);

        metricSpinner.setSelection(preferences.unitSystem().get());
    }

    @ItemSelect(R.id.metricSpinner)
    protected void onItemSelected(boolean selected, int position) {
        Log.d("Settings", "item selected " + position);
        preferences.edit()
                .unitSystem().put(position)
                .apply();

        WeatherResponse response = eventBus.getStickyEvent(WeatherResponse.class);
        if (response != null) {
            eventBus.removeStickyEvent(response);
        }

        eventBus.postSticky(new MetricChangeEvent());
    }
}
