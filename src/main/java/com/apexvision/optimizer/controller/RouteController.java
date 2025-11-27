package com.apexvision.optimizer.controller;

import com.apexvision.optimizer.dtos.RouteRequest;
import com.apexvision.optimizer.dtos.RouteResponse;
import com.apexvision.optimizer.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/optimize")
    public ResponseEntity<RouteResponse> optimize(@Valid @RequestBody RouteRequest request) {
        RouteResponse response = routeService.optimizeRoute(request);
        return ResponseEntity.ok(response);
    }
}
