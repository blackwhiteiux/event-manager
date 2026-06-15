package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.domain.EventStatus;
import dev.sorokin.eventmanager.entity.EventEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    List<EventEntity> findAllByLocationId(Long id);

    List<EventEntity> findAllByOwnerId(Long ownerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EventEntity e WHERE e.id = :id")
    Optional<EventEntity> findByIdWithLock(
            @Param("id") Long id
    );


    @Query("""
    SELECT e.id FROM EventEntity e
    WHERE e.status = :status
    AND e.startAt <= CURRENT_TIMESTAMP 
    """)
    List<Long> findEventIdsByStatusAndStartAtBefore (
            @Param("status") EventStatus status
    );


    @Query(value = """
    SELECT e.id FROM EventEntity e
    WHERE e.status = :status
    AND (e.startAt + (e.durationMinutes * INTERVAL '1 minute')) <= CURRENT_TIMESTAMP
    """, nativeQuery = true)
    List<Long> findEventIdsByStatusAndEndAtBefore (
            @Param("status") EventStatus status
    );


    @Modifying
    @Query("""
    UPDATE EventEntity e
    SET e.status = :newStatus
    WHERE e.id IN :ids
    """)
    void updateEventsStatusByIds(
            @Param("ids") List<Long> ids,
            @Param("newStatus") EventStatus newStatus
    );
}
