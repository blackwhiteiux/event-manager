package dev.sorokin.eventmanager.locations;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@Slf4j
public class LocationController {

    private final LocationMapper locationMapper;
    private final LocationService locationService;

    public LocationController(
            LocationMapper locationMapper,
            LocationService locationService
    ) {
        this.locationMapper = locationMapper;
        this.locationService = locationService;
    }

    @GetMapping
    public List<LocationDto> getAllLocations() {
        log.info("GET /locations - request to get all locations");
        var result = locationService.getAllLocations();
        log.info("GET /locations - returned {} locations", result.size());
        return result;
    }


    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestBody @Valid LocationDto locationToCreate
    ) {
        log.info("POST /locations - request to create location: {}", locationToCreate);

        var createdLocation = locationService.createLocation(
                locationMapper.toDomain(locationToCreate)
        );

        var response = locationMapper.toDto(createdLocation);
        log.info("POST /locations - created location with id: {}", response.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable Long id
    ) {
        log.info("DELETE /locations/{} - request to delete location", id);

        locationService.deleteLocationById(id);

        log.info("DELETE /locations/{} - location deleted successfully", id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/{id}")
    public LocationDto findLocationById(
            @PathVariable Long id
    ) {
        log.info("GET /locations/{} - request to get location by id", id);

        var location = locationService.findLocationById(id);
        var response = locationMapper.toDto(location);

        log.info("GET /locations/{} - found location: {}", id, response);
        return response;
    }


    @PutMapping("/{id}")
    public LocationDto updateLocation(
            @PathVariable Long id,
            @RequestBody @Valid LocationDto locationDtoToUpdate
    ) {
        log.info("PUT /locations/{} - request to update location with data: {}",
                id, locationDtoToUpdate);

        var updated = locationService.updateLocation(id, locationDtoToUpdate);
        var response = locationMapper.toDto(updated);

        log.info("PUT /locations/{} - location updated successfully: {}", id, response);
        return response;
    }
}
