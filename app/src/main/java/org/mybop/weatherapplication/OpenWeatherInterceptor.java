package org.mybop.weatherapplication;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;
import java.net.URI;

public class OpenWeatherInterceptor implements ClientHttpRequestInterceptor {

    private static final String API_KEY = "b9f451a73c130f5c9c91ec7a9e6240ca";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request = new HttpRequestWrapper(request) {
            @Override
            public URI getURI() {
                return URI.create(super.getURI() + "&APPID=" + API_KEY);
            }
        };
        return execution.execute(request, body);
    }
}
