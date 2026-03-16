package com.routeoptima.logistics_engine.service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class PricingService {
    private final WebClient.Builder webClientBuilder;

    @Value("${WEATHER_API_KEY}") private String weatherKey;
    @Value("${LOCATION_IQ_KEY}") private String locationKey;

    public Double calculatePrice(String origin, String destination, Double baseRate) {
        // 1. Get Distance (Mocked logic for LocationIQ Matrix)
        double distanceInKm = fetchDistance(origin, destination);
        
        // 2. Get Weather Multiplier
        double multiplier = fetchWeatherMultiplier(destination);
        
        // 3. Algorithm: (Base + (Dist * Rate)) * WeatherSurge
        return (baseRate + (distanceInKm * 10.0)) * multiplier;
    }

    private double fetchWeatherMultiplier(String city) {
        // Call OpenWeatherMap
        // If "Rain" -> return 1.2; If "Clear" -> return 1.0;
        return 1.2; // Example fallback
    }

    private double fetchDistance(String start, String end) {
        // Call LocationIQ Matrix API
        return 15.5; // Example fallback in km
    }
}
