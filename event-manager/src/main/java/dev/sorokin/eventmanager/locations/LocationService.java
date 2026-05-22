package dev.sorokin.eventmanager.locations;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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


    public List<Location> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(locationMapper::toDomain)
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
            Location locationToUpdate
    ) {
        var locationEntityToUpdate = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Location with id=%s not found".formatted(id)
                ));

        String newName = locationToUpdate.name();
        String oldName = locationEntityToUpdate.getName();

        if(!newName.equals(oldName) &&
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
