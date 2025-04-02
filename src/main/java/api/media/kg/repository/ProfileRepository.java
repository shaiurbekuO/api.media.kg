package api.media.kg.repository;

import api.media.kg.entity.ProfileEntity;
import api.media.kg.enums.GeneralStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findByUsernameAndVisibleTrue(String username);

    @Modifying
    @Query("update ProfileEntity p set p.status = :status where p.id = :id")
    void changeStatus(Long id, GeneralStatus status);

    @Modifying
    @Query("update ProfileEntity p set p.password = :encode where p.id = :id")
    void updatePassword(Long id, String encode);
}
