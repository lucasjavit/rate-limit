package com.vicarius.vicariuschallenge.service;

import com.vicarius.vicariuschallenge.exceptions.RateLimitExceededException;
import com.vicarius.vicariuschallenge.model.UserEntity;
import com.vicarius.vicariuschallenge.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }


    public UserEntity save(UserEntity users) {
        if (users == null) {
            throw new RateLimitExceededException("Users not found!");
        }
        return userRepository.save(users);
    }

    public UserEntity update(UserEntity users, Long userId) {
        UserEntity userFound = findById(userId);

        userFound.setLastLoginTimeUtc(LocalDateTime.now());

        return userRepository.save(userFound);
    }

    public void delete(Long userId) {
        UserEntity userFound = findById(userId);
        userRepository.delete(userFound);
    }
}
