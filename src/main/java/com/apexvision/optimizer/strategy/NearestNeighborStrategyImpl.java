package com.apexvision.optimizer.strategy;

import com.apexvision.optimizer.dtos.LocationDto;
import com.apexvision.optimizer.utils.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NearestNeighborStrategyImpl implements OptimizationStrategy{

    private final GeoUtils geoUtils;

    @Override
    public List<LocationDto> calculateOptimalRoute(List<LocationDto> locations) {

        List<LocationDto> pendingLocations = new ArrayList<>(locations);

        if(pendingLocations.isEmpty()){
            return new ArrayList<>();
        }

        List<LocationDto> optimalRoute = new ArrayList<>();

        LocationDto currentLocation = pendingLocations.remove(0);
        currentLocation.setSequenceNumber(0);
        optimalRoute.add(currentLocation);

        int sequenceCounter = 1;

        while(!pendingLocations.isEmpty()){

            LocationDto nearestNode = null;
            int narestIndex = -1;
            double minDistance = Double.MAX_VALUE;

            for (int i = 0; i < pendingLocations.size(); i++) {

                LocationDto candidateNode = pendingLocations.get(i);
                double distanceNodes = geoUtils.calculateDistanceKm(
                        currentLocation.getLatitude(), currentLocation.getLongitude(),
                        candidateNode.getLatitude(), candidateNode.getLongitude());

                if (distanceNodes < minDistance){
                    nearestNode = candidateNode;
                    minDistance = distanceNodes;
                    narestIndex = i;
                }

            }

            if (nearestNode != null){
                currentLocation = pendingLocations.remove(narestIndex);
                currentLocation.setSequenceNumber(sequenceCounter++);
                optimalRoute.add(currentLocation);
            }
        }

        return optimalRoute;
    }
}
