package pl.ignacy.loadoptimiser.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import pl.ignacy.loadoptimiser.enums.Priority;

public record PackageRequest(@Positive double weight, @Positive double volume,
                             @NotNull Priority priority, @NotBlank String deliveryAddress) {
}
