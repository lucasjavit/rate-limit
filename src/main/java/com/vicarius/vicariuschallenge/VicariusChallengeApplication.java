package com.vicarius.vicariuschallenge;

import com.vicarius.vicariuschallenge.model.UserEntity;
import com.vicarius.vicariuschallenge.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
@AllArgsConstructor
public class VicariusChallengeApplication implements ApplicationRunner {

    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(VicariusChallengeApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UserEntity user = UserEntity.builder()
                .firstName("Josh")
                .lastName("Siapnino")
                .lastLoginTimeUtc(LocalDateTime.now())
                .quota(4)
                .build();

        userService.save(user);
    }
}
