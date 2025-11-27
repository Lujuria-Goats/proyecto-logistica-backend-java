package com.apexvision.optimizer.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private String id;
    private double latitude;
    private double longitude;
    private Integer sequenceNumber;
}