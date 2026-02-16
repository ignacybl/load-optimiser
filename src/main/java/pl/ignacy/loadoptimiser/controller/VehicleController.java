package pl.ignacy.loadoptimiser.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ignacy.loadoptimiser.dto.VehicleRequest;
import pl.ignacy.loadoptimiser.dto.VehicleResponse;
import pl.ignacy.loadoptimiser.service.VehicleService;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping("/vehicles")
    public ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody VehicleRequest vehicle){
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.createVehicle(vehicle));
    }
}
