package api.media.kg.entity;

import api.media.kg.enums.SmsType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_history")
public class EmailHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "email")
    private String email;
    @Column(name = "code")
    private String code;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "email_type")
    @Enumerated(EnumType.STRING)
    private SmsType emailType;
    @Column(name = "attempt_count")
    private Integer attemptCount = 0;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public SmsType getEmailType() {
        return emailType;
    }

    public void setEmailType(SmsType emailType) {
        this.emailType = emailType;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }
}
