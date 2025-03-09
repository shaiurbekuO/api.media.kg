package api.media.kg.dto;


import api.media.kg.enums.ProfileRole;

import java.util.List;

public class JwtResponseDTO {
    private Long id;
    private String username;
    private List<ProfileRole> roleList;

    public JwtResponseDTO(Long id, String username, List<ProfileRole> roleList) {
        this.id = id;
        this.username = username;
        this.roleList = roleList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
