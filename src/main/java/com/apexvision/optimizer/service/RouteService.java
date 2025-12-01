package com.apexvision.optimizer.service;

import com.apexvision.optimizer.dtos.LocationDto;
import com.apexvision.optimizer.dtos.RouteRequest;
import com.apexvision.optimizer.dtos.RouteResponse;
import com.apexvision.optimizer.strategy.OptimizationStrategy;
import com.apexvision.optimizer.utils.GeoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteService {

    private final OptimizationStrategy strategy;
    private final GeoUtils geoUtils;

    // Distance correction factor for urban traffic
    private static final double TORTUOSITY_FACTOR = 1.3;

    public RouteResponse optimizeRoute(RouteRequest request) {

        List<LocationDto> originalList = request.getLocations();
        List<LocationDto> finalRoute;

        try{
            finalRoute = strategy.calculateOptimalRoute(originalList);
        } catch (Exception e) {
            log.error("Error in optimization algorithm: {}. Using original list.", e.getMessage());
            finalRoute = originalList;
            for(int i=0; i<finalRoute.size(); i++) finalRoute.get(i).setSequenceNumber(i);
        }

        double totalDistance = calculateTotalDistance(finalRoute);

        return RouteResponse.builder()
                .totalDistanceKm(totalDistance)
                .optimizedOrder(finalRoute)
                .build();
    }

    // Private Assistant Method for adding sequential distances
    private double calculateTotalDistance(List<LocationDto> route) {
        if (route == null || route.size() < 2) {
            return 0.0;
        }

        double total = 0.0;

        for (int i = 0; i < route.size() - 1; i++) {
            LocationDto start = route.get(i);
            LocationDto end = route.get(i + 1);

            total += geoUtils.calculateDistanceKm(
                    start.getLatitude(), start.getLongitude(),
                    end.getLatitude(), end.getLongitude()
            );
        }

        double estimatedRoadDistance = total * TORTUOSITY_FACTOR;

        return Math.round(estimatedRoadDistance * 100.0) / 100.0;
    }
}