package pl.ignacy.loadoptimiser.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ignacy.loadoptimiser.dto.PackageRequest;
import pl.ignacy.loadoptimiser.dto.PackageResponse;
import pl.ignacy.loadoptimiser.service.PackageService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PackageController {
    private final PackageService packageService;

    @PostMapping("/packages")
    public ResponseEntity<PackageResponse> createPackage(@Valid @RequestBody PackageRequest packageRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(packageService.createPackage(packageRequest));
    }
}
