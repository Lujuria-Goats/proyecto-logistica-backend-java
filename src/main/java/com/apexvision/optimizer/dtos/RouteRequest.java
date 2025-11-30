package com.apexvision.optimizer.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class RouteRequest {
    private Long fleetId;

    @NotEmpty(message = "The list of locations cannot be empty")
    private List<LocationDto> locations;
}