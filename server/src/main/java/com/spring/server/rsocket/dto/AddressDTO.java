package com.spring.server.rsocket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDTO implements Serializable {

    private Long id;

    private String uuid;

    private String street;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    private double latitude;

    private double longitude;

    private UserDTO user;

}
