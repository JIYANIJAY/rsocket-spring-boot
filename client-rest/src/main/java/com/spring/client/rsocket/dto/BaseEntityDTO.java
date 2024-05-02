package com.spring.client.rsocket.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaseEntityDTO {
    private String uuid;
    private Timestamp createdAt;
    private String createdBy;
    private Timestamp updatedAt;
    private String updatedBy;
}