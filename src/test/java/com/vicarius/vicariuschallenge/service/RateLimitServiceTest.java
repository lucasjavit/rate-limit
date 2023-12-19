package com.vicarius.vicariuschallenge.service;

import com.vicarius.vicariuschallenge.dto.UserResponseDto;
import com.vicarius.vicariuschallenge.exceptions.RateLimitExceededException;
import com.vicarius.vicariuschallenge.model.UserEntity;
import com.vicarius.vicariuschallenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RateLimitServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RateLimitService rateLimitService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsumeQuota_UserFound_QuotaIncremented() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setQuota(2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        rateLimitService.consumeQuota(userId);

        assertEquals(3, user.getQuota());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testConsumeQuota_UserFound_MaxRequestsPerUser_ExceptionThrown() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setQuota(6);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        RateLimitExceededException exception = assertThrows(RateLimitExceededException.class, () -> rateLimitService.consumeQuota(userId));

        assertTrue(exception.getMessage().contains("User quotas exceeded"));

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void testConsumeQuota_UserNotFound_ExceptionThrown() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RateLimitExceededException exception = assertThrows(RateLimitExceededException.class, () -> rateLimitService.consumeQuota(userId));

        assertTrue(exception.getMessage().contains("User not found with this userId: 1"));


        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void testGetUsersQuota() {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setQuota(2);

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setQuota(4);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponseDto> result = rateLimitService.getUsersQuota();

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).quota());
        assertEquals(4, result.get(1).quota());
    }

    @Test
    public void testGetUsersQuota_EmptyUserList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserResponseDto> result = rateLimitService.getUsersQuota();

        assertEquals(0, result.size());
    }

}