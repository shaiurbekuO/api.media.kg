package api.media.kg.dto.profile;

import jakarta.validation.constraints.NotBlank;

public class ProfilePhotoUpdateDTO {
    @NotBlank(message = "AttachId is required")
    private String photoId;

    public @NotBlank(message = "AttachId is required") String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(@NotBlank(message = "AttachId is required") String photoId) {
        this.photoId = photoId;
    }
}
