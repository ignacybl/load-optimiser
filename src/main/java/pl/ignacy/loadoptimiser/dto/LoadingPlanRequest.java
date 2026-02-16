package pl.ignacy.loadoptimiser.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record LoadingPlanRequest(@NotEmpty(message = "No vehicles") List<Long> vehicleIds, @NotEmpty(message = "No packages") List<Long> packagesIds, String strategyType) {
}
