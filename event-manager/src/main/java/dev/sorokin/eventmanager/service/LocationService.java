package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.domain.Location;
import dev.sorokin.eventmanager.dto.LocationDto;
import dev.sorokin.eventmanager.mapper.LocationMapper;
import dev.sorokin.eventmanager.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LocationService {

    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;

    public LocationService(
            LocationMapper locationMapper,
            LocationRepository locationRepository
    ) {
        this.locationMapper = locationMapper;
        this.locationRepository = locationRepository;
    }


    public List<LocationDto> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(locationMapper::toDto)
                .toList();
    }


    public Location createLocation(Location location) {
        if(locationRepository.existsByName(location.name())) {
            throw new IllegalArgumentException("Location already exist");
        }

        var locationToSave = locationMapper.toEntity(location);

        return locationMapper.toDomain(
                locationRepository.save(locationToSave)
        );
    }


    public void deleteLocationById(Long id) {
        if(!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Location with id=%s not found"
                    .formatted(id));
        }

        locationRepository.deleteById(id);
    }


    public Location findLocationById(Long id) {
        return locationMapper.toDomain(
                locationRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Location with id=%s not found".formatted(id)
                        ))
        );
    }


    public Location updateLocation(
            Long id,
            LocationDto locationDtoToUpdate
    ) {
        var locationToUpdate = locationMapper.toDomain(locationDtoToUpdate);

        var locationEntityToUpdate = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Location with id=%s not found".formatted(id)
                ));

        String newName = locationToUpdate.name();
        String oldName = locationEntityToUpdate.getName();

        if(!Objects.equals(newName, oldName) &&
                locationRepository.existsByName(locationToUpdate.name())) {
            throw new IllegalArgumentException("Location with name '%s' already exists"
                    .formatted(newName));
        }

        locationEntityToUpdate.setName(locationToUpdate.name());
        locationEntityToUpdate.setAddress(locationToUpdate.address());
        locationEntityToUpdate.setCapacity(locationToUpdate.capacity());
        locationEntityToUpdate.setDescription(locationToUpdate.description());

        return locationMapper.toDomain(
                locationRepository.save(locationEntityToUpdate)
        );
    }
}
