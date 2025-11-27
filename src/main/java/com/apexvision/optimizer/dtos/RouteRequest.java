package com.apexvision.optimizer.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class RouteRequest {
    private String fleetId;

    @NotEmpty(message = "La lista de ubicaciones no puede estar vacía")
    private List<LocationDto> locations;
}