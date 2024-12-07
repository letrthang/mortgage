package com.squad1.hackathon.dto;

import com.squad1.hackathon.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User toEntity(UserDTO dto) {
        return new User(dto.getEmail(), dto.getName(), dto.getPassword(), dto.getDob(), dto.getGender(), dto.getRole());
    }

    public UserDTO toDTO(User entity) {
        return new UserDTO(
                entity.getEmail(),
                entity.getName(),
                entity.getPassword(),
                entity.getDob(),
                entity.getGender(),
                entity.getRole()
        );
    }
}
