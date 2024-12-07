package com.squad1.hackathon.api;
import com.squad1.hackathon.dto.LoginResponse;
import com.squad1.hackathon.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Thang Le
@Tag(name = "UserServices")
@RestController
@RequestMapping("/UserServices")
public interface UserServices {

    @PostMapping(path = "/verifyUser")
    @Operation(summary = "verifyUser", description = "")
    @ApiResponse(responseCode = "200", description = "Success")
    LoginResponse verifyUser(
            @RequestHeader("email") String email, @RequestHeader("password") String password);

    @PostMapping(path = "/saveUser")
    @Operation(summary = "saveUser")
    @ApiResponse(responseCode = "200", description = "Success")
    UserDTO saveUser(@Valid @RequestBody UserDTO userDTO);

    @DeleteMapping(path = "/deleteUser/{email}")
    @Operation(summary = "Delete user by email")
    @ApiResponse(responseCode = "200", description = "Success")
    void deleteUser(@PathVariable String email);

    @GetMapping(path = "/findUser/{email}")
    @Operation(summary = "Find user by email")
    @ApiResponse(responseCode = "200", description = "Success")
    UserDTO findUserByEmail(@PathVariable String email);

    @GetMapping(path = "/findAllUsers")
    @Operation(summary = "Find all users")
    @ApiResponse(responseCode = "200", description = "Success")
    List<UserDTO> findAllUsers();

}
