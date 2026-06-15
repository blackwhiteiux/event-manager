package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
@Slf4j
public class RegistrationController {

    private final RegistrationService registrationService;
    private final EventMapper eventMapper;

    public RegistrationController(
            RegistrationService registrationService,
            EventMapper eventMapper
    ) {
        this.registrationService = registrationService;
        this.eventMapper = eventMapper;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> registrationToEvent(
            @PathVariable Long id
    ) {
        log.info("POST /events/registrations/{} - request to register current user for event", id);

        registrationService.registrationToEventById(id);

        log.info("POST /events/registrations/{} - user successfully registered for event", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelRegistration(
            @PathVariable Long id
    ) {
        log.info("DELETE /events/registrations/cancel/{} - request to cancel registration for event", id);

        registrationService.deleteRegistrationByEventId(id);

        log.info("DELETE /events/registrations/cancel/{} - registration cancelled successfully", id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/my")
    public List<EventDto> getUserRegistrationsEvents() {
        log.info("GET /events/registrations/my - request to get events registered by current user");

        var result = registrationService.getUserRegisteredEvents();
        var response = result.stream()
                .map(eventMapper::toDto)
                .toList();

        log.info("GET /events/registrations/my - found {} events where current user is registered", response.size());
        return response;
    }
}
