package com.routeoptima.logistics_engine.dto;

import lombok.Data;
import java.util.List;

public class WeatherResponse {
    private List<Weather> weather;
    @Data public static class Weather { private String main; }
}
