package com.pws.usermanagement.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pws.usermanagement.utility.AuditModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_offered")
public class JobOffered extends AuditModel {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Column(name = "job_id",nullable = false)
    private UUID jobId;

    @Column(name = "poster_id", nullable = false)
    private UUID posterId;

    @Column(name = "applier_id", nullable = false)
    private UUID applierId;

    @Column(name = "final_amount")
    private double finalAmount;

}
