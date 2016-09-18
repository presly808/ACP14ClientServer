package ua.artcode.server;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import ua.artcode.utils.OpenWeatherResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WeatherApiImpl implements WeatherApi {

    private static final String APPID = PropertiesHolder.get("openweather.appid");
    private static final String GET_CITY_INFO_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=%s";

    private Gson gson = new Gson();

    @Override
    public String get(String city) {
        String preparedUrl = String.format(GET_CITY_INFO_URL, city, APPID);

        try {
            InputStream is = new URL(preparedUrl).openStream();
            String weatherInfoResponse = IOUtils.toString(is);
            OpenWeatherResponse weatherInfoContainer = gson.fromJson(weatherInfoResponse,OpenWeatherResponse.class);
            return weatherInfoContainer.getMain().getTemp().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "some problem";
    }
}
