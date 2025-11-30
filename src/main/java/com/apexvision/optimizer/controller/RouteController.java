package com.apexvision.optimizer.controller;

import com.apexvision.optimizer.dtos.RouteRequest;
import com.apexvision.optimizer.dtos.RouteResponse;
import com.apexvision.optimizer.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base-path}")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Route Optimizer", description = "Endpoints for logistic calculations")
@CrossOrigin(origins = "*")
public class RouteController {

    private final RouteService routeService;

    @Operation(summary = "Optimize delivery locations",
            description = "Receives a list of coordinates and returns the optimal order using Nearest Neighbor algorithm.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (incorrect Lat/Lon)")
    })

    @PostMapping("/optimize")
    public ResponseEntity<RouteResponse> optimize(@Valid @RequestBody RouteRequest request) {

        log.info("Optimization request received for fleet ID: {}", request.getFleetId());

        RouteResponse response = routeService.optimizeRoute(request);

        log.info("Route optimized successfully. Total distance: {} Km", response.getTotalDistanceKm());

        return ResponseEntity.ok(response);
    }
}