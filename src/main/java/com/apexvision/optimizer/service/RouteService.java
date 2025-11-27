package com.apexvision.optimizer.service;

import com.apexvision.optimizer.dtos.LocationDto;
import com.apexvision.optimizer.dtos.RouteRequest;
import com.apexvision.optimizer.dtos.RouteResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    /**
     * FASE 1: MOCK IMPLEMENTATION
     * Recibe la lista y la devuelve tal cual, solo asignando números de secuencia.
     */
    public RouteResponse optimizeRoute(RouteRequest request) {

        // Obtenemos la lista original
        List<LocationDto> route = request.getLocations();

        // Simulamos proceso: Asignamos secuencia 0, 1, 2 en el orden que llegaron
        for (int i = 0; i < route.size(); i++) {
            route.get(i).setSequenceNumber(i);
        }

        // Retornamos respuesta simulada
        return RouteResponse.builder()
                .totalDistanceKm(0.0)
                .optimizedOrder(route)
                .build();
    }
}