package api.media.kg.repository;

import api.media.kg.entity.AttachEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachRepository extends JpaRepository<AttachEntity, String> {
    @Modifying
    @Transactional
    @Query("update AttachEntity a set a.visible = false where a.id = :id")
    void delete(String id);
}