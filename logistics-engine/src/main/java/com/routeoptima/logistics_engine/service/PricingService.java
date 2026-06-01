package com.routeoptima.logistics_engine.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.routeoptima.logistics_engine.dto.DistanceResponse;
import com.routeoptima.logistics_engine.dto.ShipmentResponse;
import com.routeoptima.logistics_engine.dto.GeocodeResponse;
import org.springframework.cache.annotation.Cacheable;

@Service
@RequiredArgsConstructor
public class PricingService {
    private final WebClient.Builder webClientBuilder;

    @Value("${app.api.weather-api-key}") private String weatherKey;
    @Value("${app.api.location-iq-key}") private String locationKey;

    @Cacheable(
    value = "routePricing", 
    key = "#origin.toLowerCase() + '-' + #destination.toLowerCase() + '-' + #strategy.name()"
    )
    public ShipmentResponse calculatePrice(String origin, String destination, Double baseRate, RoutingStrategy strategy) {
        // Fetch Distance and Duration
        DistanceResponse distanceResponse = fetchDistanceAndDuration(origin, destination, strategy);
        DistanceResponse.Route selectedRoute = selectRoute(distanceResponse, strategy);
        double distanceInKm = selectedRoute.getDistance() / 1000.0; // Convert meters to kilometers
        long durationInSeconds = (long) selectedRoute.getDuration();

        // Calculate Price
        double multiplier = fetchWeatherMultiplier(destination);
        double finalPrice = (baseRate + (distanceInKm * 10.0)) * multiplier;

        // Format ETA
        String formattedETA = formatDuration(durationInSeconds);

        System.out.println("Cache MISS! Executing full calculation for paths...");

        return new ShipmentResponse(finalPrice, distanceInKm, formattedETA, strategy);
    }

    private DistanceResponse fetchDistanceAndDuration(String origin, String destination, RoutingStrategy strategy) {
        double[] originCoords = getCoordinates(origin);
        double[] destCoords = getCoordinates(destination);

        // LocationIQ expects: lon,lat;lon,lat
        String url = String.format(
            "https://us1.locationiq.com/v1/directions/driving/%f,%f;%f,%f?key=%s&overview=full",
            originCoords[0], originCoords[1], destCoords[0], destCoords[1], locationKey
        );

        if (strategy == RoutingStrategy.ECONOMIC) {
            url += "&alternatives=true";
        } else if (strategy == RoutingStrategy.SECURE) {
            url += "&exclude=tolls,ferries";
        }

        WebClient webClient = webClientBuilder.build();
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(DistanceResponse.class)
                .block();
    }

    private double[] getCoordinates(String city) {
        String url = String.format("https://us1.locationiq.com/v1/search?key=%s&q=%s&format=json&limit=1", locationKey, city);

        GeocodeResponse[] response = webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(GeocodeResponse[].class)
                .block();

        if (response != null && response.length > 0) {
            return new double[]{
                Double.parseDouble(response[0].getLon()),
                Double.parseDouble(response[0].getLat())
            };
        }
        throw new RuntimeException("Could not find coordinates for: " + city);
    }

    private double fetchWeatherMultiplier(String city) {
        // Call OpenWeatherMap
        return 1.2; // Example fallback
    }

    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        return hours + "H " + minutes + "M";
    }

    private DistanceResponse.Route selectRoute(DistanceResponse response, RoutingStrategy strategy) {
        if (response.getRoutes() == null || response.getRoutes().isEmpty()) {
            throw new RuntimeException("No routes found for this path.");
        }

        if (strategy == RoutingStrategy.ECONOMIC) {
            // Find the route with the absolute shortest distance (minimizes fuel/distance)
            return response.getRoutes().stream()
                    .min(java.util.Comparator.comparingDouble(DistanceResponse.Route::getDistance))
                    .orElse(response.getRoutes().get(0));
        }

        // Default (FASTEST and SECURE)
        // LocationIQ's first route is the most optimal by time/duration
        return response.getRoutes().get(0);
    }

    public enum RoutingStrategy {
        FASTEST, ECONOMIC, SECURE
    }
    
}
