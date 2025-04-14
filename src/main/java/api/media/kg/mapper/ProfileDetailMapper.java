package api.media.kg.mapper;

import api.media.kg.enums.GeneralStatus;

import java.time.LocalDateTime;

public interface ProfileDetailMapper {
    Long getId();
    String getName();
    String getUsername();
    String getPhotoId();
    GeneralStatus getStatus();
    LocalDateTime getCreatedDate();
    Long getPostCount();
    String getRoles();
}
