package com.apexvision.optimizer.service;

import com.apexvision.optimizer.dtos.LocationDto;
import com.apexvision.optimizer.dtos.RouteRequest;
import com.apexvision.optimizer.dtos.RouteResponse;
import com.apexvision.optimizer.strategy.OptimizationStrategy;
import com.apexvision.optimizer.utils.GeoUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private OptimizationStrategy strategy;

    @Mock
    private GeoUtils geoUtils;

    @InjectMocks
    private RouteService routeService;

    @Test
    void shouldReturnOriginalList_WhenAlgorithmFails() {
        // 1. GIVEN
        LocationDto p1 = new LocationDto(1L, 0.0, 0.0, null);
        LocationDto p2 = new LocationDto(2L, 1.0, 1.0, null);
        List<LocationDto> locations = Arrays.asList(p1, p2);

        // Assuming that the RouteRequest constructor was adjusted to receive (Long, List) or (String, List)
        RouteRequest request = new RouteRequest("flota-pruebas-stress-01", locations);

        // We simulate a critical failure in the mathematical strategy.
        when(strategy.calculateOptimalRoute(anyList()))
                .thenThrow(new RuntimeException("Simulated mathematical error"));

        // 2. WHEN
        RouteResponse response = routeService.optimizeRoute(request);

        // 3. THEN
        Assertions.assertNotNull(response);
        // You must return the original list unchanged (Fallback).
        Assertions.assertEquals(2, response.getOptimizedOrder().size());
        Assertions.assertEquals(1L, response.getOptimizedOrder().get(0).getId());

        // The distance should not be zero even if the optimization fails.
        Assertions.assertNotNull(response.getTotalDistanceKm());
    }
}