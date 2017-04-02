package org.mybop.weatherapplication;

import android.location.Location;
import android.util.Log;

import com.tmtron.greenannotations.EventBusGreenRobot;

import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;
import org.androidannotations.api.support.app.AbstractIntentService;
import org.androidannotations.rest.spring.annotations.RestService;
import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

@EIntentService
public class UpdateService extends AbstractIntentService {

    private static final String LOG_TAG = UpdateService.class.getSimpleName();

    @RestService
    protected WeatherClient weatherClient;

    @EventBusGreenRobot
    protected EventBus eventBus;

    public UpdateService() {
        super("UpdateService");
    }

    @ServiceAction
    protected void refresh(final Location location) {
        Log.d(LOG_TAG, "refresh");
        Single.fromCallable(new Callable<WeatherResponse>() {
            @Override
            public WeatherResponse call() throws Exception {
                return weatherClient.weather(location.getLatitude(), location.getLongitude());
            }
        })
                .doOnSuccess(new Consumer<WeatherResponse>() {
                    @Override
                    public void accept(@NonNull WeatherResponse response) throws Exception {
                        Log.d(LOG_TAG, "weather obtained " + response);
                    }
                })
                .subscribe(new Consumer<WeatherResponse>() {
                    @Override
                    public void accept(@NonNull WeatherResponse response) throws Exception {
                        eventBus.postSticky(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(LOG_TAG, "", throwable);
                    }
                });

    }
}
