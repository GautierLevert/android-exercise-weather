package org.mybop.weatherapplication.openweathermap;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

public class LanguageInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        request = new HttpRequestWrapper(request) {
            @Override
            public URI getURI() {
                return URI.create(super.getURI() + "&lang=" + Locale.getDefault().getLanguage());
            }
        };

        return execution.execute(request, body);
    }
}
