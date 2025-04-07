package api.media.kg.dto.post;

import api.media.kg.dto.AttachCreateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
public class PostCreateDTO {
    @NotBlank(message = "Title is required")
    @Length(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;
    @NotBlank(message = "Content is required")
    private String content;
    @NotNull(message = "Photo is required")
    private AttachCreateDTO photo;

}
