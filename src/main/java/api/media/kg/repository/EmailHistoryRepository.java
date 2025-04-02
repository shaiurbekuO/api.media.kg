package api.media.kg.repository;

import api.media.kg.entity.EmailHistoryEntity;
import api.media.kg.entity.SmsHistoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity, String> {
    @Query("SELECT COUNT(s) FROM EmailHistoryEntity s WHERE s.email = :email AND s.createdDate BETWEEN :from AND :to")
    Long countByEmailAndCreatedDateBetween(@Param("email") String email, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    Optional<EmailHistoryEntity> findTop1ByEmailOrderByCreatedDateDesc(String email);
    @Transactional
    @Modifying
    @Query("update EmailHistoryEntity s set s.attemptCount = coalesce(s.attemptCount, 0) + 1 where s.id = :id")
    void updateAttemptCount(@Param("id") String id);
}