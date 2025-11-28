package com.apexvision.optimizer.utils;

import org.testng.annotations.Test;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.Assert.assertEquals;


public class GeoUtilsTest {

    @Test
    void testCalculateDistance_Bogota_Medellin() {
        // Approximate coordinates
        double latBogota = 4.7110;
        double lonBogota = -74.0721;

        double latMedellin = 6.2442;
        double lonMedellin = -75.5812;

        double distancia = GeoUtils.calculateDistance(latBogota, lonBogota, latMedellin, lonMedellin);

        System.out.println("Distancia calculada: " + distancia + " km");

        // We verify that it is within a reasonable range (between 230 and 260 km).
        assertTrue(distancia > 230 && distancia < 260, "La distancia debería ser lógica para Colombia");
    }

    @Test
    void testSamePoint() {
        // The distance between the same point must be 0.
        double result = GeoUtils.calculateDistance(10.0, 10.0, 10.0, 10.0);
        assertEquals(0.0, result, 0.001);
    }
}
