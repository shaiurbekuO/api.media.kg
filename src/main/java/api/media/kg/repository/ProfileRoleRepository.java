package api.media.kg.repository;

import api.media.kg.dto.SimpleResponse;
import api.media.kg.entity.ProfileRoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRoleRepository extends JpaRepository<ProfileRoleEntity, Long> {
    @Transactional
    @Modifying
    @Query("delete from ProfileRoleEntity where profileId = :profileId")
    int deleteByProfileId(@Param("profileId") Long profileId);

}