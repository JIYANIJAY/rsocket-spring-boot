package com.spring.server.rsocket.entity;

import jakarta.persistence.PrePersist;

import java.util.UUID;

public class BaseEntityListener {
    @PrePersist
    public void prePersist(BaseEntity baseEntity) {
        if (baseEntity.getId() == null) {
            baseEntity.setUuid(UUID.randomUUID().toString());
        }
    }
}
