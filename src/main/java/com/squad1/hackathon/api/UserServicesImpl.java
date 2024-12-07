package com.squad1.hackathon.api;

import com.squad1.hackathon.dto.UserDTO;
import com.squad1.hackathon.dto.UserMapper;
import com.squad1.hackathon.entity.User;
import com.squad1.hackathon.exception.UserNotFoundException;
import com.squad1.hackathon.repository.UserRepoImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServicesImpl implements UserServices {

    private final UserRepoImpl userRepository;
    private final UserMapper userMapper;

    public UserServicesImpl(UserRepoImpl userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Boolean verifyUser(String email, String password) {
        Boolean ret = false;
        try {
            var user = userRepository.findByEmail(email);
            if (user != null && user.getPassword().equals(password)) {
                ret = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
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
}
