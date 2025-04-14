package api.media.kg.dto;

import api.media.kg.enums.GeneralStatus;
import api.media.kg.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Long id;
    private String name;
    private String username;
    private List<ProfileRole> roleList;
    private String jwt;
    private AttachDTO photo;
    private GeneralStatus status;
    private LocalDateTime createdDate;
    private Long postCount;


}
