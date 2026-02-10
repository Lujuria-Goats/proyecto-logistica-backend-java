package com.apexvision.optimizer.controller;

import com.apexvision.optimizer.dtos.RouteRequest;
import com.apexvision.optimizer.dtos.RouteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Route Optimizer", description = "Endpoints for logistic calculations")
public interface RouteControllerApi {

    @Operation(summary = "Optimize delivery locations", description = "Receives a list of coordinates and returns the optimal order using Nearest Neighbor algorithm.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (incorrect Lat/Lon)"),
            @ApiResponse(responseCode = "500", description = "Internal calculation error")
    })
    @PostMapping("/optimize")
    ResponseEntity<RouteResponse> optimize(@RequestBody RouteRequest request);
}
