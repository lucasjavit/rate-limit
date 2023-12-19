package com.vicarius.vicariuschallenge.controller;

import com.vicarius.vicariuschallenge.dto.UserResponseDto;
import com.vicarius.vicariuschallenge.service.RateLimitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quota")
@AllArgsConstructor
public class RateLimitController {

    private final RateLimitService rateLimitService;

    @GetMapping("/{userId}")
    public ResponseEntity<Void> consumeQuota(@PathVariable Long userId) {
        rateLimitService.consumeQuota(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getUsersQuota() {
        return new ResponseEntity<>(rateLimitService.getUsersQuota(), HttpStatus.OK);
    }
}
