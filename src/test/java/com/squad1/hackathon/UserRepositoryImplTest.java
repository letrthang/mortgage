package com.squad1.hackathon;

import com.squad1.hackathon.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.squad1.hackathon.repository.UserRepository;
import com.squad1.hackathon.repository.UserRepoImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
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

    private User createTestUser(String email) {
        User user = new User();
        user.setEmail(email);
        user.setName("Test User");
        user.setPassword("password");
        user.setDob(Timestamp.valueOf("2000-01-01 00:00:00"));
        user.setGender("M");
        user.setRole("USER");
        return user;
    }

    @Test
    void saveAndUpdate_ShouldReturnSavedUser() {
        User user = new User("test@email.com", "Test User", "password", Timestamp.valueOf("2000-01-01 00:00:00"), "M", "USER");
        when(repository.save(any(User.class))).thenReturn(user);

        User result = userRepoImpl.saveAndUpdate(user);

        verify(repository).save(user);
        assertEquals(user, result);
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        String email = "test@email.com";
        User user = new User(email, "Test User", "password", Timestamp.valueOf("2000-01-01 00:00:00"), "M", "USER");
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

        List<User> expectedUsers = Arrays.asList(
                new User("test1@email.com", "User1", "pass1",  Timestamp.valueOf("2000-01-01 00:00:00"), "M", "USER"),
                new User("test2@email.com", "User2", "pass2",  Timestamp.valueOf("2001-01-01 00:00:00"), "M", "USER")
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

        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userRepoImpl.findAll();

        // Assert
        verify(repository).findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void findByName_ShouldReturnUser_WhenNameExists() {
        String name = "Test User";
        User expectedUser = createTestUser("test@email.com");
        expectedUser.setName(name);
        when(repository.findByName(name)).thenReturn(Optional.of(expectedUser));

        Optional<User> result = userRepoImpl.findByName(name);

        // Assert
        verify(repository).findByName(name);
        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
        assertEquals(name, result.get().getName());
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenNameNotFound() {
        String name = "Nonexistent User";
        when(repository.findByName(name)).thenReturn(Optional.empty());

        Optional<User> result = userRepoImpl.findByName(name);

        // Assert
        verify(repository).findByName(name);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllPageable_ShouldReturnPageOfUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = Arrays.asList(
                new User("email1@test.com", "name1", "pass1", Timestamp.valueOf("1990-01-01 00:00:00"), "M", "USER"),
                new User("email2@test.com", "name2", "pass2", Timestamp.valueOf("1990-01-01 00:00:00"), "F", "USER")
        );
        Page<User> expectedPage = new PageImpl<>(users, pageable, users.size());

        when(repository.findAll(pageable)).thenReturn(expectedPage);

        Page<User> result = userRepoImpl.findAllPageable(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(users.get(0).getEmail(), result.getContent().get(0).getEmail());
        assertEquals(users.get(1).getEmail(), result.getContent().get(1).getEmail());

        verify(repository).findAll(pageable);
    }

    @Test
    void findAllPageable_ShouldReturnEmptyPage_WhenNoUsers() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(repository.findAll(pageable)).thenReturn(emptyPage);

        Page<User> result = userRepoImpl.findAllPageable(pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(repository).findAll(pageable);
    }

    @Test
    void findAllPageable_ShouldHandleDifferentPageSizes() {
        Pageable pageable = PageRequest.of(1, 5); // Page 1 with size 5
        List<User> users = Arrays.asList(
                new User("email5@test.com", "name5", "pass5", Timestamp.valueOf("1990-01-01 00:00:00"), "M", "USER"),
                new User("email6@test.com", "name6", "pass6", Timestamp.valueOf("1990-01-01 00:00:00"), "F", "USER")
        );
        Page<User> expectedPage = new PageImpl<>(users, pageable, 10); // Total 10 elements

        when(repository.findAll(pageable)).thenReturn(expectedPage);

        Page<User> result = userRepoImpl.findAllPageable(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(10, result.getTotalElements());
        assertEquals(1, result.getNumber()); // Page number
        assertEquals(5, result.getSize()); // Page size

        verify(repository).findAll(pageable);
    }

    @Test
    void delete_ShouldDeleteUser() {

        User user = new User("test@email.com", "Test User", "password",  Timestamp.valueOf("1990-01-01 00:00:00"), "M", "USER");
        doNothing().when(repository).delete(user);
        // Act
        userRepoImpl.delete(user);
        // Assert
        verify(repository, times(1)).delete(user);
    }

    @Test
    void delete_ShouldHandleNullUser() {

        User user = null;
        doThrow(new IllegalArgumentException("Entity must not be null")).when(repository).delete(user);
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userRepoImpl.delete(user);
        });
        verify(repository, times(1)).delete(user);
    }
}