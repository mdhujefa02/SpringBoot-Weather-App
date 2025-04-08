package com.example.weather_app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.weather_app.model.weatherResponse;

@Controller
public class WeatherController {

    @Value("${api.key}")
    private String apiKey;

    @RequestMapping("/")
    public String getIndex() {
        return "index";
    }

    @RequestMapping("/weather.html")
    public String getWeather(@RequestParam String city, Model model) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
        
        RestTemplate restTemplate = new RestTemplate();

        try {
            weatherResponse weather = restTemplate.getForObject(url, weatherResponse.class);

            model.addAttribute("city", city);
            model.addAttribute("dateTime", weather.getFormattedDateTime());
            model.addAttribute("temperature", weather.getMain().getTemp());
            model.addAttribute("humidity", weather.getMain().getHumidity());
            model.addAttribute("windSpeed", weather.getWind().getSpeed());
            model.addAttribute("weatherCondition", weather.getWeather().get(0).getMain());

        } catch (Exception e) {
            model.addAttribute("error", "City not found or API key is invalid");
        }

        return "weather";
    }
}
