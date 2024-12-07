package com.squad1.hackathon.api;

import com.squad1.hackathon.dto.LoginResponse;
import com.squad1.hackathon.dto.UserDTO;
import com.squad1.hackathon.dto.UserMapper;
import com.squad1.hackathon.entity.User;
import com.squad1.hackathon.exception.UserNotFoundException;
import com.squad1.hackathon.repository.UserRepoImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    @Override
    public LoginResponse verifyUser(String email, String password) {
        try {
            var user = userRepository.findByEmail(email);
            if (user != null) {
                String hashedInputPassword = hashPassword(password);
                if (hashedInputPassword.equals(user.getPassword())) {
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
            // Hash the password before saving
            String hashedPassword = hashPassword(userDTO.getPassword());
            userDTO.setPassword(hashedPassword);

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

    @Override
    public List<UserDTO> findAllUsers(int page, int size, String sortBy) {
//        try {
//            List<UserDTO> allUsers = new ArrayList<>();
//            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
//            Page<User> userPage;
//
//            do {
//                userPage = userRepository.findAllPageable(pageable);
//                List<UserDTO> usersInPage = userPage.getContent()
//                        .stream()
//                        .map(userMapper::toDTO)
//                        .toList();
//
//                allUsers.addAll(usersInPage);
//
//                pageable = userPage.nextPageable();
//            } while (userPage.hasNext());
//
//            return allUsers;
//
//        } catch (Exception e) {
//            throw new UserNotFoundException("Error retrieving users: " + e.getMessage());
//        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage = userRepository.findAllPageable(pageable);

            // Convert users in this page to DTOs and return
            return userPage.getContent()
                    .stream()
                    .map(userMapper::toDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new UserNotFoundException("Error retrieving users: " + e.getMessage());
        }
    }
}
