package dev.sorokin.eventmanager.specification;

import dev.sorokin.eventmanager.dto.EventSearchRequestDto;
import dev.sorokin.eventmanager.entity.EventEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EventSpecification {

    public static Specification<EventEntity> searchEvents(EventSearchRequestDto filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filters.name() != null && !filters.name().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("name"), filters.name()));
            }

            if(filters.placesMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("maxPlaces"), filters.placesMin()));
            }

            if(filters.placesMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxPlaces"), filters.placesMax()));
            }

            if(filters.dateStartAfter() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startAt"), filters.dateStartAfter()));
            }

            if(filters.dateStartBefore() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("startAt"), filters.dateStartBefore()));
            }

            if(filters.costMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), filters.costMin()));
            }

            if(filters.costMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("cost"), filters.costMax()));
            }

            if(filters.durationMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("durationMinutes"), filters.durationMin()));
            }

            if(filters.durationMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("durationMinutes"), filters.durationMax()));
            }

            if(filters.locationId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("location"), filters.locationId()));
            }

            if(filters.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filters.status()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
