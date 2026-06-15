package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.specification.EventSpecification;
import dev.sorokin.eventmanager.domain.EventStatus;
import dev.sorokin.eventmanager.domain.UserRole;
import dev.sorokin.eventmanager.dto.EventCreateRequestDto;
import dev.sorokin.eventmanager.dto.EventSearchRequestDto;
import dev.sorokin.eventmanager.dto.EventUpdateRequestDto;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final UserService userService;

    public EventService(
            EventRepository eventRepository,
            @Lazy LocationService locationService,
            UserService userService
    ) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.userService = userService;
    }


    public EventEntity createEvent(
            EventCreateRequestDto eventToCreate
    ) {
        var location = locationService.findLocationEntityById(eventToCreate.locationId());

        if(location.getCapacity() < eventToCreate.maxPlaces()) {
            throw new IllegalArgumentException("Location capacity is less than the event's maxPlaces");
        }

        var currentUser = userService.getCurrentUserEntity();

        var eventForSave = new EventEntity();
        eventForSave.setName(eventToCreate.name());
        eventForSave.setStartAt(eventToCreate.date().toLocalDateTime());
        eventForSave.setDurationMinutes(eventToCreate.duration());
        eventForSave.setMaxPlaces(eventToCreate.maxPlaces());
        eventForSave.setOccupiedPlaces(0);
        eventForSave.setCost(eventToCreate.cost());
        eventForSave.setStatus(EventStatus.WAIT_START);

        eventForSave.setLocation(location);
        eventForSave.setOwner(currentUser);

        return eventRepository.save(eventForSave);

    }


    public void deleteEventById(Long id) {
        var eventForDelete = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        var currentUser = userService.getCurrentUserEntity();

        if(!currentUser.getRole().equals(UserRole.ADMIN) &&
        !currentUser.getId().equals(eventForDelete.getOwner().getId())) {
            throw new AccessDeniedException("You don't have permission to delete this event");
        }

        if(!eventForDelete.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("The event cannot be canceled");
        }

        eventForDelete.setStatus(EventStatus.CANCELLED);
        eventRepository.save(eventForDelete);
    }


    public EventEntity getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
    }


    public EventEntity updateEventById(
            Long id,
            EventUpdateRequestDto updateRequestDto
    ) {
        var eventForUpdate = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        var currentUser = userService.getCurrentUserEntity();

        if(!currentUser.getRole().equals(UserRole.ADMIN) &&
                !currentUser.getId().equals(eventForUpdate.getOwner().getId())) {
            throw new AccessDeniedException("You don't have permission to update this event");
        }

        if (eventForUpdate.getStatus() != EventStatus.WAIT_START) {
            throw new IllegalArgumentException("Cannot update event that has already started or finished");
        }

        if(updateRequestDto.maxPlaces() < eventForUpdate.getOccupiedPlaces()) {
            throw new IllegalArgumentException("maxPlaces cannot be less than currently occupied places");
        }

        LocationEntity newLocation = null;
        if(!eventForUpdate.getLocation().getId().equals(updateRequestDto.locationId())) {
            newLocation = locationService.findLocationEntityById(updateRequestDto.locationId());

            if(newLocation.getCapacity() < updateRequestDto.maxPlaces()) {
                throw new IllegalArgumentException("New location capacity is less than maxPlaces");
            }
        } else {
            newLocation = eventForUpdate.getLocation();
        }

        eventForUpdate.setName(updateRequestDto.name());
        eventForUpdate.setStartAt(updateRequestDto.date().toLocalDateTime());
        eventForUpdate.setDurationMinutes(updateRequestDto.duration());
        eventForUpdate.setMaxPlaces(updateRequestDto.maxPlaces());
        eventForUpdate.setCost(updateRequestDto.cost());
        eventForUpdate.setLocation(newLocation);

        return eventRepository.save(eventForUpdate);
    }


    public List<EventEntity> getOwnerEvents() {
        var currentUser = userService.getCurrentUserEntity();
        return eventRepository.findAllByOwnerId(currentUser.getId());
    }


    public List<EventEntity> searchEvents(EventSearchRequestDto filters) {
        Specification<EventEntity> specification = EventSpecification.searchEvents(filters);
        return eventRepository.findAll(specification);
    }


    public List<EventEntity> getAllEventsByLocationId(Long id) {
        return eventRepository.findAllByLocationId(id);
    }

    public EventEntity getEventByIdWithLock(Long id) {
        return eventRepository.findByIdWithLock(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
    }


    @Transactional
    public void updateEventStatuses() {
        var startedIds = eventRepository.findEventIdsByStatusAndStartAtBefore(EventStatus.WAIT_START);

        if(!startedIds.isEmpty()) {
            eventRepository.updateEventsStatusByIds(startedIds, EventStatus.STARTED);
        }

        var finishedIds = eventRepository.findEventIdsByStatusAndEndAtBefore(EventStatus.STARTED);

        if(!finishedIds.isEmpty()) {
            eventRepository.updateEventsStatusByIds(finishedIds, EventStatus.FINISHED);
        }
    }
}
