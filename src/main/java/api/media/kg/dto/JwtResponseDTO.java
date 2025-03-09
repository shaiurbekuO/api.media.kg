package api.media.kg.dto;


import api.media.kg.enums.ProfileRole;

import java.util.List;

public class JwtResponseDTO {
    private Long id;
    private List<ProfileRole> roleList;

    public JwtResponseDTO(Long id, List<ProfileRole> roleList) {
        this.id = id;
        this.roleList = roleList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProfileRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<ProfileRole> roleList) {
        this.roleList = roleList;
    }
}
