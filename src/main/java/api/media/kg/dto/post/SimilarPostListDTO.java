package api.media.kg.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimilarPostListDTO {
    @NotBlank(message = "Except id is required")
    private String exceptId;
}
