package org.mybop.weatherapplication.openweathermap;

import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.mybop.weatherapplication.Preferences_;
import org.mybop.weatherapplication.UnitSystem;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;
import java.net.URI;

@EBean
public class MetricInterceptor implements ClientHttpRequestInterceptor {

    @Pref
    protected Preferences_ preferences;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final UnitSystem unitSystem = UnitSystem.valueOf(preferences.unitSystem().get());

        Log.d(MetricInterceptor.class.getSimpleName(), "unit system = " + unitSystem);

        request = new HttpRequestWrapper(request) {
            @Override
            public URI getURI() {
                return URI.create(super.getURI() + "&units=" + unitSystem.getParam());
            }
        };

        return execution.execute(request, body);
    }
}
