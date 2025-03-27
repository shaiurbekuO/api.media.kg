package api.media.kg.repository;

import api.media.kg.entity.SmsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SmsHistoryRepository extends JpaRepository<SmsHistoryEntity, String> {
//    @Query("select count(*) from SmsHistoryEntity where phone = :phone and created_date between :from and :to")
//    Long countByPhoneAndCreatedDateBetween(String phone, LocalDateTime from, LocalDateTime to);

    @Query("SELECT COUNT(s) FROM SmsHistoryEntity s WHERE s.phone = :phone AND s.createdDate BETWEEN :from AND :to")
    Long countByPhoneAndCreatedDateBetween(@Param("phone") String phone, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}