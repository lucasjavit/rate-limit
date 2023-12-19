package com.vicarius.vicariuschallenge.service;

import com.vicarius.vicariuschallenge.dto.UserResponseDto;
import com.vicarius.vicariuschallenge.exceptions.RateLimitExceededException;
import com.vicarius.vicariuschallenge.model.UserEntity;
import com.vicarius.vicariuschallenge.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RateLimitService {

    private UserRepository userRepository;

    private static final int MAX_REQUESTS_PER_USER = 5;

    public void consumeQuota(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new RateLimitExceededException("User not found with this userId: " + userId));

        int currentRequests = user.getQuota();
        if (currentRequests > MAX_REQUESTS_PER_USER) {
            throw new RateLimitExceededException("User quotas exceeded.");
        }

        user.setQuota(currentRequests + 1);
        userRepository.save(user);
    }

    public List<UserResponseDto> getUsersQuota() {
        return userRepository.findAll().stream().map(user -> new UserResponseDto(user.getId(), user.getQuota())).collect(Collectors.toList());
    }
}

