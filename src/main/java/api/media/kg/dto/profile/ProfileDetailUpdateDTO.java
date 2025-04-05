package api.media.kg.dto.profile;

import jakarta.validation.constraints.NotBlank;

public class ProfileDetailUpdateDTO {
    @NotBlank(message = "Name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
