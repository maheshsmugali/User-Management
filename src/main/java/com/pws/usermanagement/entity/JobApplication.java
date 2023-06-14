package com.pws.usermanagement.entity;

import com.pws.usermanagement.utility.AuditModel;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_application")
public class JobApplication extends AuditModel {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "applier_id",nullable = false)
    private User applierId;

    @ManyToOne
    @JoinColumn(name = "job_post_id")
    private JobPost jobPostId;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status")
    private Keyword applicationStatus;

    public enum Keyword {
        PENDING, ACCEPTED, REJECTED

    }

}
