package org.mybop.weatherapplication;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tmtron.greenannotations.EventBusGreenRobot;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.format.DateTimeFormat;
import org.mybop.weatherapplication.openweathermap.WeatherResponse;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

@EFragment(R.layout.fragment_weather)
public class WeatherFragment extends Fragment {

    private static final String LOG_TAG = WeatherFragment.class.getSimpleName();

    @EventBusGreenRobot
    protected EventBus eventBus;

    @ViewById(R.id.icon)
    protected ImageView icon;

    @ViewById(R.id.main)
    protected TextView main;

    @ViewById(R.id.description)
    protected TextView description;

    @ViewById(R.id.temperature)
    protected TextView temperature;

    @ViewById(R.id.pressure)
    protected TextView pressure;

    @ViewById(R.id.humidity)
    protected TextView humidity;

    @ViewById(R.id.speed)
    protected TextView speed;

    @ViewById(R.id.lastUpdate)
    protected TextView lastUpdate;

    @Subscribe(sticky = true)
    @UiThread(propagation = UiThread.Propagation.REUSE)
    public void onWeatherResponse(WeatherResponse response) {
        Log.d(LOG_TAG, response.toString());

        if (isAdded()) {
            Observable.fromIterable(response.getWeather())
                    .firstElement()
                    .subscribe(new Consumer<WeatherResponse.Weather>() {
                        @Override
                        public void accept(@NonNull WeatherResponse.Weather weather) throws Exception {
                            main.setText(weather.getMain());

                            description.setText(weather.getDescription());

                            Picasso.with(getContext())
                                    .load(weather.getIconUri())
                                    .into(icon);
                        }
                    });

            temperature.setText(String.format("%.2f °C", response.getMain().getTemp()));
            pressure.setText(String.format("%.2f hPa", response.getMain().getPressure()));
            humidity.setText(String.format("%d %%", response.getMain().getHumidity()));

            speed.setText(String.format("%.2f m/s", response.getWind().getSpeed()));

            lastUpdate.setText(DateTimeFormat.shortDateTime().print(response.getDatetime()));
        }
    }
}
