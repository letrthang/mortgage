package com.squad1.hackathon;

import com.squad1.hackathon.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.squad1.hackathon.repository.UserRepository;
import com.squad1.hackathon.repository.UserRepoImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserRepoImpl userRepoImpl;

    @Test
    void saveAndUpdate_ShouldReturnSavedUser() {
        User user = new User("test@email.com", "Test User", "password");
        when(repository.save(any(User.class))).thenReturn(user);

        User result = userRepoImpl.saveAndUpdate(user);

        verify(repository).save(user);
        assertEquals(user, result);
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        String email = "test@email.com";
        User user = new User(email, "Test User", "password");
        when(repository.findByEmail(email)).thenReturn(user);

        User result = userRepoImpl.findByEmail(email);

        verify(repository).findByEmail(email);
        assertEquals(user, result);
    }

    @Test
    void findByEmail_ShouldReturnNull_WhenUserNotFound() {
        String email = "nonexistent@email.com";
        when(repository.findByEmail(email)).thenReturn(null);

        User result = userRepoImpl.findByEmail(email);

        verify(repository).findByEmail(email);
        assertNull(result);
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(
                new User("test1@email.com", "User1", "pass1"),
                new User("test2@email.com", "User2", "pass2")
        );
        when(repository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> result = userRepoImpl.findAll();

        // Assert
        verify(repository).findAll();
        assertEquals(expectedUsers.size(), result.size());
        assertEquals(expectedUsers, result);
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoUsers() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = userRepoImpl.findAll();

        // Assert
        verify(repository).findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void findByPassword_ShouldReturnUser_WhenPasswordExists() {
        // Arrange
        String password = "testPass";
        User expectedUser = new User("test@email.com", "Test User", password);
        when(repository.findByPassword(password)).thenReturn(Optional.of(expectedUser));

        // Act
        Optional<User> result = userRepoImpl.findByPassword(password);

        // Assert
        verify(repository).findByPassword(password);
        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

    @Test
    void findByPassword_ShouldReturnEmpty_WhenPasswordNotFound() {
        // Arrange
        String password = "nonexistentPass";
        when(repository.findByPassword(password)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userRepoImpl.findByPassword(password);

        // Assert
        verify(repository).findByPassword(password);
        assertTrue(result.isEmpty());
    }

    @Test
    void delete_ShouldDeleteUser() {
        // Arrange
        User user = new User("test@email.com", "Test User", "password");
        doNothing().when(repository).delete(user);
        // Act
        userRepoImpl.delete(user);
        // Assert
        verify(repository, times(1)).delete(user);
    }

    @Test
    void delete_ShouldHandleNullUser() {
        // Arrange
        User user = null;
        doThrow(new IllegalArgumentException("Entity must not be null")).when(repository).delete(user);
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userRepoImpl.delete(user);
        });
        verify(repository, times(1)).delete(user);
    }
}