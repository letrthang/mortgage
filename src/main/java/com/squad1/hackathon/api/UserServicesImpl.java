package com.squad1.hackathon.api;

import com.squad1.hackathon.dto.LoginResponse;
import com.squad1.hackathon.dto.UserDTO;
import com.squad1.hackathon.dto.UserMapper;
import com.squad1.hackathon.entity.User;
import com.squad1.hackathon.exception.UserNotFoundException;
import com.squad1.hackathon.repository.UserRepoImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServicesImpl implements UserServices {

    private final UserRepoImpl userRepository;
    private final UserMapper userMapper;

    public UserServicesImpl(UserRepoImpl userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public LoginResponse verifyUser(String email, String password) {
        try {
            var user = userRepository.findByEmail(email);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    return new LoginResponse(true, "login success");
                } else {
                    return new LoginResponse(false, "wrong password");
                }
            }
            return new LoginResponse(false, "user not found");
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse(false, "error: " + e.getMessage());
        }
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        try {
            User user = userMapper.toEntity(userDTO);
            userRepository.saveAndUpdate(user);
            return userDTO;
        } catch (Exception e) {
            throw new UserNotFoundException("Error saving user: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UserNotFoundException("User not found with email: " + email);
            }
            userRepository.delete(user);
        } catch (Exception e) {
            throw new UserNotFoundException("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UserNotFoundException("User not found with email: " + email);
            }
            return userMapper.toDTO(user);
        } catch (Exception e) {
            throw new UserNotFoundException("Error finding user: " + e.getMessage());
        }
    }

    @Override
    public List<UserDTO> findAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(userMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new UserNotFoundException("Error retrieving users: " + e.getMessage());
        }
    }
}
