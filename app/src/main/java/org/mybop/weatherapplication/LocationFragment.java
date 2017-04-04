package org.mybop.weatherapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.tmtron.greenannotations.EventBusGreenRobot;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SystemService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@EFragment
public class LocationFragment extends Fragment {

    private static final String LOG_TAG = LocationFragment.class.getSimpleName();

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @SystemService
    protected LocationManager locationManager;

    @EventBusGreenRobot
    protected EventBus eventBus;

    protected Disposable subscription = null;

    @AfterInject
    protected void retainInstance() {
        setRetainInstance(true);
    }

    protected void subscribeLocation() {
        subscription = lastKnownLocation(LocationManager.NETWORK_PROVIDER)
                .switchIfEmpty(lastKnownLocation(LocationManager.GPS_PROVIDER))
                .switchIfEmpty(Single.<Location>ambArray(requestLocation(LocationManager.NETWORK_PROVIDER), requestLocation(LocationManager.GPS_PROVIDER)).toMaybe())
                .toSingle()
                .subscribe(new Consumer<Location>() {
                    @Override
                    public void accept(@NonNull Location location) throws Exception {

                        Log.d(LOG_TAG, "received location " + location);

                        UpdateService_.intent(getContext())
                                .refresh(location)
                                .start();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(LOG_TAG, "", throwable);
                    }
                });
    }

    @AfterInject
    protected void initLocation() {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            subscribeLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(LOG_TAG, "permission result");

        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    subscribeLocation();
                }
                break;
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.dispose();
        }
    }

    private Maybe<Location> lastKnownLocation(final String locationProvider) {
        return Maybe.fromCallable(new Callable<Location>() {
            @Override
            public Location call() throws SecurityException {
                return locationManager.getLastKnownLocation(locationProvider);
            }
        });
    }

    private Single<Location> requestLocation(final String locationProvider) {
        return Single.create(new SingleOnSubscribe<Location>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull final SingleEmitter<Location> emitter) throws SecurityException {
                final LocationListener listener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        emitter.onSuccess(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };

                locationManager.requestSingleUpdate(locationProvider, listener, null);

                emitter.setDisposable(Disposables.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        locationManager.removeUpdates(listener);
                    }
                }));
            }
        });
    }

    @Subscribe(sticky = true)
    public void onMetricChange(MetricChangeEvent metricChangeEvent) {
        eventBus.removeStickyEvent(metricChangeEvent);
        initLocation();
    }
}
