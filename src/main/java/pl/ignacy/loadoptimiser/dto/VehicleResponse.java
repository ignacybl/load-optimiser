package pl.ignacy.loadoptimiser.dto;

import pl.ignacy.loadoptimiser.enums.VehicleType;

public record VehicleResponse(Long id, String plateNumber, VehicleType vehicleType) {
}
