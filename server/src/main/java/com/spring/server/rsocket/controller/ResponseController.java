package com.spring.server.rsocket.controller;

import com.spring.server.rsocket.dto.AddressDTO;
import com.spring.server.rsocket.dto.PingDto;
import com.spring.server.rsocket.dto.UserDTO;
import com.spring.server.rsocket.entity.Data;
import com.spring.server.rsocket.entity.User;
import com.spring.server.rsocket.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class ResponseController {

    @Autowired
    private UserRepository userRepository;

//    @MessageMapping("my-request-response")
//    public PingDto requestResponse(PingDto pingDto) throws InterruptedException {
//        log.info("Received ping for request-response: {}", pingDto);
//
//        List<User> users = userRepository.findAll();
//        return PingDto.builder()
//                .source(pingDto.getDestination())
//                .destination(pingDto.getSource())
//                .data("Response To : " + pingDto.getData())
//                .build();
//    }

    @MessageMapping("my-request-response")
    public Mono<Data> requestResponse() {
        log.info("Received ping for request-response: {}");
        List<User> users = userRepository.findAll();
        Map<String, Boolean> map = new HashMap<>();
        map.put("address", false);
        List<UserDTO> userDTOS = new ArrayList<>();
        users.forEach(user -> {
            userDTOS.add(convertToDTO(user, map));
        });

        Data data = new Data(userDTOS);
        return Mono.just(data);
    }

    private UserDTO convertToDTO(User user, Map<String, Boolean> map) {
        UserDTO userDTO = new UserDTO();

        if (map.get("address")) {
            List<AddressDTO> addressDTOs = user.getUserAddresses().stream()
                    .map(address -> {
                        AddressDTO addressDTO = new AddressDTO();
                        BeanUtils.copyProperties(address, addressDTO, "id", "uuid", "createdAt", "createdBy", "updatedAt", "updatedBy");
                        return addressDTO;
                    })
                    .collect(Collectors.toList());
            userDTO.setUserAddresses(addressDTOs);
        }

        BeanUtils.copyProperties(user, userDTO, "createdAt", "createdBy", "updatedAt", "updatedBy", "password");
        return userDTO;
    }

    @MessageMapping("my-fire-and-forget")
    public void fireAndForget(PingDto pingDto) {
        log.info("Received notification: {}", pingDto);
    }

    @MessageMapping("my-request-stream")
    Flux<PingDto> requestStream(PingDto pingDto) {
        log.info("Received ping for my-request-stream: {}", pingDto);
        return Flux
                .interval(Duration.ofSeconds(3))
                .map(i -> new PingDto(pingDto.getDestination(), pingDto.getSource(), "In response to: " + pingDto.getData(), null));
    }

    @MessageMapping("my-channel")
    public Flux<Long> channel(Flux<PingDto> pingDtos) {
        final AtomicLong notificationCount = new AtomicLong(0);
        return pingDtos.doOnNext(notification -> {
                    log.info("Received notification for channel: {}", notification);
                    notificationCount.incrementAndGet();
                })
                .switchMap(notification -> Flux.interval(Duration.ofSeconds(1)).map(new Object() {
                    private Function<Long, Long> numberOfMessages(AtomicLong notificationCount) {
                        long count = notificationCount.get();
                        log.info("Return flux with count: {}", count);
                        return i -> count;
                    }
                }.numberOfMessages(notificationCount))).log();
    }
}
