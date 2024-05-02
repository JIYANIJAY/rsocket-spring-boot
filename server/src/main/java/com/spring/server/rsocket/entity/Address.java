package com.spring.server.rsocket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@Entity
@Table(name = "ADDRESSES")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityScan
public class Address extends BaseEntity {

    @Column(name = "STREET")
    private String street;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "LATITUDE")
    private double latitude;

    @Column(name = "LONGITUDE")
    private double longitude;

    @ManyToOne
    @JoinColumn(name = "USER_ID",referencedColumnName = "ID")
    private User user;

}
