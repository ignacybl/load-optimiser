package pl.ignacy.loadoptimiser.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import pl.ignacy.loadoptimiser.enums.VehicleType;

public record VehicleRequest(@NotBlank @Pattern(regexp = "^[A-Z]{1,3}[A-Z0-9]{4,5}$", message = "invalid plate number") String plateNumber,
                             @Positive double maxWeight, @Positive double maxVolume, @NotNull VehicleType vehicleType) {
}
