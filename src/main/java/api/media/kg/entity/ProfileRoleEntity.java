package api.media.kg.entity;

import api.media.kg.enums.ProfileRole;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "profile_role")
public class ProfileRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "profile_id")
    private Long profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;
    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private ProfileRole roles;
    @Column(name = "created_date")
    private LocalDate createdDate;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public ProfileRole getRoles() {
        return roles;
    }

    public void setRoles(ProfileRole roles) {
        this.roles = roles;
    }

    public ProfileEntity getProfile() {
        return profile;
    }

    public void setProfile(ProfileEntity profile) {
        this.profile = profile;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
