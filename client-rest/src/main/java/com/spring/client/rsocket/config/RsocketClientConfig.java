package com.spring.client.rsocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;

@Configuration
public class RsocketClientConfig {

    @Bean
    public RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
        return builder
//                .dataMimeType(MediaType.APPLICATION_JSON)
                .tcp("localhost", 7001);
    }
}
