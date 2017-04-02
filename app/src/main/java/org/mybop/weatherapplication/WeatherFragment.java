package org.mybop.weatherapplication;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import com.tmtron.greenannotations.EventBusGreenRobot;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@EFragment(R.layout.fragment_weather)
public class WeatherFragment extends Fragment {

    private static final String LOG_TAG = WeatherFragment.class.getSimpleName();

    @ViewById(R.id.textView)
    protected TextView textView;

    @EventBusGreenRobot
    protected EventBus eventBus;

    @Subscribe(sticky = true)
    @UiThread(propagation = UiThread.Propagation.REUSE)
    public void onLocationUpdate(Location location) {
        if (isAdded()) {
            Log.d(LOG_TAG, "send refresh");
            UpdateService_.intent(getContext())
                    .refresh(location)
                    .start();
        }
    }

    @Subscribe(sticky = true)
    @UiThread(propagation = UiThread.Propagation.REUSE)
    public void onWeatherResponse(WeatherResponse response) {
        if (isAdded()) {
            textView.setText(response.toString());
        }
    }
}
