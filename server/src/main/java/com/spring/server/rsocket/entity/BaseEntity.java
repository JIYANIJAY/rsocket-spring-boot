package com.spring.server.rsocket.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@MappedSuperclass
@Embeddable
@Data
@EntityListeners(BaseEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @Column(name = "UUID", unique = true, updatable = false)
    private String uuid;

    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_AT", insertable = true, updatable = true)
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
}