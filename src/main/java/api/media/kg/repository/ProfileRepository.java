package api.media.kg.repository;

import api.media.kg.entity.ProfileEntity;
import api.media.kg.enums.GeneralStatus;
import api.media.kg.mapper.ProfileDetailMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long>, PagingAndSortingRepository<ProfileEntity, Long> {
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

    @Query(value = "select p.id as id, p.name as name, p.username as username, p.photo_id as photoId, p.status as status, p.created_date as createdDate, " +
            "(select count(*) from post as pt where pt.profile_id = p.id) as postCount, " +
            "(select String_agg(pr.roles, ',') from profile_role as pr where pr.profile_id = p.id) as roles " +
            "from profile as p " +
            "where p.visible = true order by p.created_date desc",
            nativeQuery = true,
    countQuery = "select count(*) from profile  where visible = true")
    Page<ProfileDetailMapper> filter(PageRequest pageRequest);

    @Query(value = "select p.id as id, p.name as name, p.username as username, p.photo_id as photoId, p.status as status, p.created_date as createdDate, " +
            "(select count(*) from post as pt where pt.profile_id = p.id) as postCount, " +
            "(select String_agg(pr.roles, ',') from profile_role as pr where pr.profile_id = p.id) as roles " +
            "from profile as p " +
            "where (lower(p.username) like lower(concat('%', ?1, '%')) or lower(p.name) like lower(concat('%', ?1, '%'))) " +
            "and p.visible = true " +
            "order by p.created_date desc",
            nativeQuery = true,
            countQuery = "select count(*) from profile p where (lower(p.username) like lower(concat('%', ?1, '%')) or lower(p.name) like lower(concat('%', ?1, '%'))) and p.visible = true")
    Page<ProfileDetailMapper> filter(String query, PageRequest pageRequest);


//    @Query("select p from ProfileEntity p join fetch p.roleList where lower(p.username) " +
//            "like lower(concat('%', ?1, '%')) or lower(p.name) like lower(concat('%', ?1, '%') order by p.createdDate desc")")
//    Page<ProfileEntity> filter(String query, PageRequest pageRequest);


    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update ProfileEntity p set p.visible = false where p.id = :id")
    void delete(@Param("id") Long id);



}
