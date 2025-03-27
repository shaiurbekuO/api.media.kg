package api.media.kg.repository;

import api.media.kg.entity.SmsProviderHolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsProviderHolderRepository extends JpaRepository<SmsProviderHolderEntity, Long> {

    Optional<SmsProviderHolderEntity> findTop1By();
}
