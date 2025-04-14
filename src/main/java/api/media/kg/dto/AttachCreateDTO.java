package api.media.kg.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachCreateDTO {
    @NotBlank(message = "File id is required")
    private String id;
}
