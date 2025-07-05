package com.backend.blogApp.service;

import com.backend.blogApp.dto.UserDto;
import com.backend.blogApp.payloads.UserResponse;

public interface UserService {

    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user, Integer id);
    UserDto getUserById(Integer id);
    UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deleteUser(Integer id);

}
