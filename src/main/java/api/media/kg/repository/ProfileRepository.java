package api.media.kg.repository;

import api.media.kg.entity.ProfileEntity;
import api.media.kg.enums.GeneralStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    @Query("SELECT p FROM ProfileEntity p WHERE p.username = :username AND p.visible = true")
    Optional<ProfileEntity> findByUsernameAndVisibleTrue(@Param("username") String username);

    @Modifying
    @Query("update ProfileEntity p set p.status = :status where p.id = :id")
    void changeStatus(Long id, GeneralStatus status);

    @Modifying
    @Query("update ProfileEntity p set p.password = :encode where p.id = :id")
    void updatePassword(Long id, String encode);

    @Modifying
    @Query("update ProfileEntity p set p.name = :name where p.id = :id")
    void updateDetail(Long id, String name);

    @Modifying
    @Query("update ProfileEntity p set p.tempUsername = :tempUsername where p.id = :id")
    void updateTempUsername(Long id,String tempUsername);

    @Modifying
    @Query("update ProfileEntity set username =?2  where id = ?1")
    void updateUsername(Long id, String tempUsername);
    @Modifying
    @Query("update ProfileEntity set photoId =?2  where id = ?1")
    void updatePhoto(Long id, String photoId);
}
