package com.routeoptima.logistics_engine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PricingServiceTest {

    @InjectMocks
    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFormatDuration_CalculatesHoursAndMinutesCleanly() {
        // Arrange: 7200 seconds = exactly 2 Hours
        long totalSeconds = 7500; // 2 Hours, 5 Minutes

        // Act
        String result = pricingService.formatDuration(totalSeconds);

        // Assert
        assertEquals("2H 5M", result);
    }

    
}