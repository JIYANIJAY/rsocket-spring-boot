package com.spring.server.rsocket.dto;

import lombok.*;
import org.springframework.core.io.Resource;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PingDto {
    private String source;
    private String destination;
    private String data;
    private Object resource;
}
