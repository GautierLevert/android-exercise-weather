package org.mybop.weatherapplication;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

@Rest(rootUrl = "http://api.openweathermap.org/data/2.5/", converters = GsonHttpMessageConverter.class, interceptors = OpenWeatherInterceptor.class)
public interface WeatherClient {

    @Get("/weather?lat={lat}&lon={lon}")
    WeatherResponse weather(@Path("lat") double lat, @Path("lon") double lon);
}
