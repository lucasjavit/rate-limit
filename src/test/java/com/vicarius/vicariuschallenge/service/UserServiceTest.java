package com.vicarius.vicariuschallenge.service;
import com.vicarius.vicariuschallenge.exceptions.RateLimitExceededException;
import com.vicarius.vicariuschallenge.model.UserEntity;
import com.vicarius.vicariuschallenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_UserFound() {
        Long userId = 1L;
        UserEntity expectedUser = new UserEntity();
        expectedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        UserEntity actualUser = userService.findById(userId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testFindById_UserNotFound_ExceptionThrown() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findById(userId));
    }

    @Test
    public void testSave_UserNotNull_UserSaved() {
        UserEntity userToSave = new UserEntity();

        when(userRepository.save(userToSave)).thenReturn(userToSave);

        UserEntity savedUser = userService.save(userToSave);

        assertEquals(userToSave, savedUser);
        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    public void testSave_UserNull_ExceptionThrown() {
        assertThrows(RateLimitExceededException.class, () -> userService.save(null));

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void testUpdate_UserFound_UserUpdated() {
        Long userId = 1L;
        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);
        existingUser.setLastLoginTimeUtc(LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserEntity updatedUser = userService.update(existingUser, userId);

        assertNotNull(updatedUser.getLastLoginTimeUtc());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdate_UserNotFound_ExceptionThrown() {
        Long userId = 1L;
        UserEntity userToUpdate = new UserEntity();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.update(userToUpdate, userId));

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void testDelete_UserFound_UserDeleted() {
        Long userId = 1L;
        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.delete(userId);

        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    public void testDelete_UserNotFound_ExceptionThrown() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.delete(userId));

        verify(userRepository, never()).delete(any(UserEntity.class));
    }
}
