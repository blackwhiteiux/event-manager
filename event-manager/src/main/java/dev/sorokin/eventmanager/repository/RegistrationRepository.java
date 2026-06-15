package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {

    boolean existsByEvent_IdAndUser_Id(Long eventId, Long userId);

    Optional<RegistrationEntity> getRegistrationEntitiesByEvent_IdAndUser_Id(Long eventId, Long userId);

    List<RegistrationEntity> findAllByUser_Id(Long userId);
}
