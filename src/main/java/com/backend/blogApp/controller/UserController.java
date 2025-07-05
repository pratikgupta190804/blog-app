package com.backend.blogApp.controller;

import com.backend.blogApp.payloads.ApiResponse;
import com.backend.blogApp.dto.UserDto;
import com.backend.blogApp.payloads.UserResponse;
import com.backend.blogApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User APIs", description = "Read, Update, adn Delete User")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @PutMapping("/{userId}")
    @Operation(summary = "for updating a user")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer userId){
        UserDto updatedUser = userService.updateUser(userDto, userId);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<UserResponse> getAllUser(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
            ){
        return ResponseEntity.ok(userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
