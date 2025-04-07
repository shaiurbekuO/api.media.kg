package api.media.kg.repository;

import api.media.kg.entity.PostEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {
    @Modifying
    @Transactional
    @Query("select p from PostEntity p where p.profile.id = :profileId and p.visible = true")
    List<PostEntity> getAllByProfileAndVisibleTrue(Long profileId);

    @Modifying
    @Transactional
    @Query("UPDATE PostEntity p SET p.title = :#{#entity.title}, p.content = :#{#entity.content}, p.photoId = :#{#entity.photoId} WHERE p.id = :#{#entity.id}")
    void update(@Param("entity") PostEntity entity);
    @Modifying
    @Transactional
    @Query("update PostEntity p set p.visible = false where p.id = :id")
    void delete(String id);
}