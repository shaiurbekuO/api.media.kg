package api.media.kg.dto.profile;

import api.media.kg.enums.GeneralStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileStatusDTO {
    private GeneralStatus status;
}
