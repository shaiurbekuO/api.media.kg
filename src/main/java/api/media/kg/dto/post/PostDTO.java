package api.media.kg.dto.post;

import api.media.kg.dto.AttachDTO;
import api.media.kg.dto.ProfileDTO;
import api.media.kg.enums.GeneralStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


import java.time.LocalDateTime;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    private String id;
    private String title;
    private String content;
    private AttachDTO photo;
    private LocalDateTime createdDate = LocalDateTime.now();
    private GeneralStatus status;
    private Boolean visible;
    private ProfileDTO profile;

}
