package com.spring.server.rsocket.entity;

import com.spring.server.rsocket.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
@Setter
@Getter
public class Data {

    private Object userDTO;
}
