package api.media.kg.repository;

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
public interface SmsHistoryRepository extends JpaRepository<SmsHistoryEntity, String> {
    @Query("SELECT COUNT(s) FROM SmsHistoryEntity s WHERE s.phone = :phone AND s.createdDate BETWEEN :from AND :to")
    Long countByPhoneAndCreatedDateBetween(@Param("phone") String phone, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    Optional<SmsHistoryEntity> findTop1ByPhoneOrderByCreatedDateDesc(String phone);
    @Transactional
    @Modifying
    @Query("update SmsHistoryEntity s set s.attemptCount = coalesce(s.attemptCount, 0) + 1 where s.id = :id")
    void updateAttemptCount(@Param("id") String id);


}