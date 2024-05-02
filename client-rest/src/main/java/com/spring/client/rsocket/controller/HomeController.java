package com.spring.client.rsocket.controller;


import com.spring.client.rsocket.dto.Data;
import com.spring.client.rsocket.dto.PingDto;
import com.spring.client.rsocket.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/client")
public class HomeController {

    private static final String CLIENT = "Client";
    private static final String SERVER = "Server";

    private final RSocketRequester rSocketRequester;

//    @GetMapping("/request-response")
//    public Mono<PingDto> requestResponse() {
//        PingDto ping = PingDto.builder()
//                .source(CLIENT)
//                .destination(SERVER)
//                .data("Test the Request-Response interaction model")
//                .build();
//        log.info("Ping for my-request-response: {}", ping);
//        return rSocketRequester
//                .route("my-request-response")
//                .data(ping)
//                .retrieveMono(PingDto.class).log();
//    }

    @GetMapping("/request-response-mono")
    public Mono<Data> requestResponse(@Payload UserDTO userDTO) {
        PingDto ping = PingDto.builder()
                .source(CLIENT)
                .destination(SERVER)
                .data("Test the Request-Response interaction model")
                .build();
        log.info("Ping for my-request-response: {}", ping);
        return rSocketRequester
                .route("my-request-response")
                .data(userDTO)
                .retrieveMono(Data.class).log();
    }

    @GetMapping("/request-response-flux")
//    public ResponseEntity<Flux<List<UserDTO>>> requestResponseFlux(@Payload UserDTO userDTO) {
    public ResponseEntity<Flux<UserDTO>> requestResponseFlux(@Payload UserDTO userDTO) {

        PingDto ping = PingDto.builder()
                .source(CLIENT)
                .destination(SERVER)
                .data("Test the Request-Response interaction model")
                .build();
        log.info("Ping for my-request-response: {}", ping);
//        Flux<List<UserDTO>> resp= rSocketRequester
//                .route("my-request-response-flux")
//                .data(userDTO)
//                .retrieveFlux(new ParameterizedTypeReference<List<UserDTO>>() {})
//                .log();

        Flux<UserDTO> resp= rSocketRequester
                .route("my-request-response-flux")
                .data(userDTO)
                .retrieveFlux(UserDTO.class)
                .log();

        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(resp);
    }

    @GetMapping("/fire-and-forget")
    public Mono<Void> fireAndForget() {
        PingDto ping = PingDto.builder()
                .source(CLIENT)
                .destination(SERVER)
                .data("Test the Fire-And-Forget interaction model")
                .build();
        log.info("Ping for my-fire-and-forget: {}", ping);
        return rSocketRequester
                .route("my-fire-and-forget")
                .data(ping)
                .retrieveMono(Void.class);
    }

    @GetMapping("/request-stream")
    public ResponseEntity<Flux<PingDto>> requestStream() {
        PingDto ping = PingDto.builder()
                .source(CLIENT)
                .destination(SERVER)
                .data("Test the Request-Stream interaction model")
                .build();
        log.info("Ping for my-request-stream: {}", ping);
        Flux<PingDto> pingDtoFlux = rSocketRequester
                .route("my-request-stream")
                .data(ping)
                .retrieveFlux(PingDto.class);
        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(pingDtoFlux);
    }


    @GetMapping("/channel")
    public ResponseEntity<Flux<Long>> channel() {
        Mono<PingDto> pingDtoMono = Mono.just(PingDto.builder()
                .source(CLIENT)
                .destination(SERVER)
                .data("Test the Channel interaction model")
                .build());
        Mono<PingDto> pingDtoMono1 = Mono.just(PingDto.builder()
                .source(CLIENT)
                .destination(SERVER)
                .data("Test the Channel interaction model")
                .build()).delayElement(Duration.ofSeconds(2));
        Mono<PingDto> pingDtoMono2 = Mono.just(PingDto.builder()
                .source(CLIENT)
                .destination(SERVER)
                .data("Test the Channel interaction model")
                .build()).delayElement(Duration.ofSeconds(5));

        Flux<PingDto> notifications = Flux.concat(pingDtoMono, pingDtoMono2, pingDtoMono, pingDtoMono1, pingDtoMono2, pingDtoMono)
                .doOnNext(d -> log.info("Send notification for my-channel"));

        Flux<Long> numberOfNotifications = this.rSocketRequester
                .route("my-channel")
                .data(notifications)
                .retrieveFlux(Long.class);

        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(numberOfNotifications);
    }

    @GetMapping("/close")
    public void close() {
        rSocketRequester.rsocketClient().dispose();
    }




//    @GetMapping(value = "/my-music-stream",produces = "audio/mp3")
//    public ResponseEntity<Flux<Resource>> music() {
//
//        Flux<Resource> pingDtoFlux = rSocketRequester
//                .route("stream.mp3")
//                .retrieveFlux(Resource.class);
//        return ResponseEntity.ok().body(pingDtoFlux);
//    }

    @GetMapping(value = "/my-music-stream")

    public Mono<PingDto> music() {

        Mono<PingDto> pingDtoFlux = rSocketRequester
                .route("stream.mp3")
                .retrieveMono(PingDto.class)
                .log();
        return pingDtoFlux;
    }
}
