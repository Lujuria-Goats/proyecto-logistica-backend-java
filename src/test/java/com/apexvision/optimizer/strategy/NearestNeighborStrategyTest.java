package com.apexvision.optimizer.strategy;

import com.apexvision.optimizer.dtos.LocationDto;
import com.apexvision.optimizer.strategy.impl.NearestNeighborStrategyImpl;
import com.apexvision.optimizer.utils.GeoUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class NearestNeighborStrategyTest {

    @Spy
    private GeoUtils geoUtils;

    @InjectMocks
    private NearestNeighborStrategyImpl strategy;

    @Test
    void shouldOrderLocationsByProximity() {
        // 1. GIVEN
        LocationDto pA = new LocationDto(1L, 0.0, 0.0, null); // Inicio
        LocationDto pB = new LocationDto(2L, 10.0, 0.0, null); // Lejano
        LocationDto pC = new LocationDto(3L, 1.0, 0.0, null);  // Cercano

        List<LocationDto> input = Arrays.asList(pA, pB, pC);

        // 2. WHEN
        List<LocationDto> result = strategy.calculateOptimalRoute(input);

        // 3. THEN
        // The expected order is: Home -> Nearby -> Far away (A -> C -> B)
        Assertions.assertEquals(3, result.size());

        Assertions.assertEquals(1L, result.get(0).getId()); // A
        Assertions.assertEquals(3L, result.get(1).getId()); // C
        Assertions.assertEquals(2L, result.get(2).getId()); // B

        // We verify sequence assignment
        Assertions.assertEquals(0, result.get(0).getSequenceNumber());
        Assertions.assertEquals(1, result.get(1).getSequenceNumber());
    }
}