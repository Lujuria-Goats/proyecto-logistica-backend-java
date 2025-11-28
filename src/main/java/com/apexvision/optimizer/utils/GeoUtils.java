package com.apexvision.optimizer.utils;

public class GeoUtils {

    // Earth's radius in kilometers
    private static final double Earth_Radius_Km = 6371.0;

    public GeoUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate difference in latitudes and longitudes
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Apply Haversine formula
        // a = sin²(Δlat/2) + cos(lat1) * cos(lat2) * sin²(Δlon/2)

        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.pow(Math.sin(deltaLon / 2), 2);

        // c = 2 * atan2(√a, √(1-a))
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate final distance
        // d = R * c
        return Earth_Radius_Km * c;
    }













}
