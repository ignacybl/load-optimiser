package pl.ignacy.loadoptimiser.dto;

import pl.ignacy.loadoptimiser.enums.Priority;

public record PackageResponse(Long id, double weight, double volume, Priority priority, String deliveryAddress) {
}
