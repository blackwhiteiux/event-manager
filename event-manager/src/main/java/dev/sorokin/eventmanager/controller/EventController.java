package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.dto.EventCreateRequestDto;
import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.dto.EventSearchRequestDto;
import dev.sorokin.eventmanager.dto.EventUpdateRequestDto;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.service.EventService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    public EventController(
            EventService eventService,
            EventMapper eventMapper
    ) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }


    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestBody @Valid EventCreateRequestDto eventDtoToCreate
    ) {
        log.info("POST /events - request to create event: {}", eventDtoToCreate);

        var response = eventMapper.toDto(
                eventService.createEvent(eventDtoToCreate)
        );

        log.info("POST /events - event created successfully with id: {}", response.id());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id
    ) {
        log.info("DELETE /events/{} - request to delete event", id);

        eventService.deleteEventById(id);

        log.info("DELETE /events/{} - event cancelled successfully", id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/{id}")
    public EventDto getEventById(
            @PathVariable Long id
    ) {
        log.info("GET /events/{} - request to get event by id", id);
        var result = eventService.getEventById(id);
        var response = eventMapper.toDto(result);

        log.info("GET /events/{} - event found: {}", id, response);
        return response;
    }


    @PutMapping("/{id}")
    public EventDto updateEvent(
            @PathVariable Long id,
            @RequestBody @Valid EventUpdateRequestDto eventDtoToUpdate
    ) {
        log.info("PUT /events/{} - request to update event with data: {}", id, eventDtoToUpdate);

        var result = eventService.updateEventById(id, eventDtoToUpdate);
        var response = eventMapper.toDto(result);

        log.info("PUT /events/{} - event updated successfully: {}", id, response);
        return response;
    }


    @PostMapping("/search")
    public List<EventDto> searchEvents(
            @RequestBody @Valid EventSearchRequestDto filters
    ) {
        log.info("POST /events/search - request to search events with filters: {}", filters);
        var result = eventService.searchEvents(filters);
        var response = result.stream()
                .map(eventMapper::toDto)
                .toList();

        log.info("POST /events/search - found {} events", response.size());
        return response;
    }


    @GetMapping("/my")
    public List<EventDto> getOwnerEvents() {
        log.info("GET /events/my - request to get events owned by current user");

        var result = eventService.getOwnerEvents();

        log.info("GET /events/my - found {} events owned by current user", result.size());
        return result;
    }
}
