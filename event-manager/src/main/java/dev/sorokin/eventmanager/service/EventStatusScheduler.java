package dev.sorokin.eventmanager.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class EventStatusScheduler {

    private final EventService eventService;


    public EventStatusScheduler(
            EventService eventService
    ) {
        this.eventService = eventService;
    }

    @Scheduled(cron = "${scheduler.event.status-cron}")
    public void updateStatuses() {
        eventService.updateEventStatuses();
    }
}
