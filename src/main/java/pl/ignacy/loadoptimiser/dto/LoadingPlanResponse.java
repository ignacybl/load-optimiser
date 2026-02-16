package pl.ignacy.loadoptimiser.dto;

import pl.ignacy.loadoptimiser.enums.VehicleType;

import java.time.LocalDateTime;
import java.util.List;

public record LoadingPlanResponse(Long id, String plateNumber,
                                  VehicleType vehicleType, List<PackageResponse> packages,
                                  double totalWeight, double totalVolume, LocalDateTime createdAt) {
}
