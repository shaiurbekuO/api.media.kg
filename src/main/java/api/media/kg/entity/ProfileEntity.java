package api.media.kg.entity;

import api.media.kg.enums.AppLanguage;
import api.media.kg.enums.GeneralStatus;
import api.media.kg.enums.ProfileRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "profile")
@Getter
@Setter
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "username")
    private String username; //* email/phone
    @Column(name = "temp_username")
    private String tempUsername;
    @Column(name = "password")
    private String password;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GeneralStatus status;
    @Column(name = "visible")
    private Boolean visible=Boolean.TRUE;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "photo_id")
    private String photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private AttachEntity photo;

    @OneToMany(mappedBy = "profile",fetch = FetchType.LAZY)
    private List<ProfileRoleEntity> roleList;



}
