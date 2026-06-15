package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.domain.EventStatus;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.RegistrationEntity;
import dev.sorokin.eventmanager.repository.RegistrationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserService userService;
    private final EventService eventService;

    public RegistrationService(
            RegistrationRepository registrationRepository,
            UserService userService,
            EventService eventService
    ) {
        this.registrationRepository = registrationRepository;
        this.userService = userService;
        this.eventService = eventService;
    }


    @Transactional
    public void registrationToEventById(Long eventId) {
        var event = eventService.getEventByIdWithLock(eventId);

        if(event.getStatus() != EventStatus.WAIT_START) {
            throw new IllegalArgumentException("You can only sign up for a non-scheduled event.");
        }

        if(event.getOccupiedPlaces() >= event.getMaxPlaces()) {
            throw new IllegalArgumentException("There are no empty seats");
        }

        var currentUser = userService.getCurrentUserEntity();

        if(registrationRepository.existsByEvent_IdAndUser_Id(eventId, currentUser.getId())) {
            throw new IllegalArgumentException("User already registered for this event");
        }

        var registration = new RegistrationEntity();
        registration.setEvent(event);
        registration.setUser(currentUser);
        registration.setCreatedAt(LocalDateTime.now());

        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);

        registrationRepository.save(registration);
    }


    @Transactional
    public void deleteRegistrationByEventId(Long eventId) {
        var event = eventService.getEventByIdWithLock(eventId);

        if(event.getStatus() != EventStatus.WAIT_START) {
            throw new IllegalArgumentException("You can cancel the registration only for a non-scheduled event.");
        }

        var currentUser = userService.getCurrentUserEntity();

        var registration = registrationRepository.getRegistrationEntitiesByEvent_IdAndUser_Id(eventId, currentUser.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Registration not found"));

        event.setOccupiedPlaces(event.getOccupiedPlaces() - 1);
        registrationRepository.delete(registration);
    }

    public List<EventEntity> getUserRegisteredEvents() {
        var currentUser = userService.getCurrentUserEntity();

        return registrationRepository.findAllByUser_Id(currentUser.getId()).stream()
                .map(RegistrationEntity::getEvent)
                .toList();
    }

}
