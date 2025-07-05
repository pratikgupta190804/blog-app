package com.backend.blogApp.service.impl;

import com.backend.blogApp.entity.User;
import com.backend.blogApp.exception.ResourceNotFoundException;
import com.backend.blogApp.dto.UserDto;
import com.backend.blogApp.payloads.UserResponse;
import com.backend.blogApp.repository.UserRepo;
import com.backend.blogApp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private PasswordEncoder passwordEncoder;

    private UserRepo userRepo;

    private ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = dtoToUser(userDto);
        User savedUser = userRepo.save(user);
        return userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setName(userDto.getName() != null ? userDto.getName(): user.getName());
        user.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());
        user.setPassword(userDto.getPassword() != null ?
                passwordEncoder.encode(userDto.getPassword()) :
                passwordEncoder.encode(user.getPassword()));
        user.setAbout(userDto.getAbout() != null ? userDto.getAbout() : user.getAbout());
        userRepo.save(user);
        return userToDto(user);
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return userToDto(user);
    }

    @Override
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = sortBy.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> pageUser = userRepo.findAll(p);

        List<User> userList = pageUser.getContent();
        List<UserDto> userDtos = userList.stream().map(user -> userToDto(user)).collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(userDtos);
        userResponse.setPageNumber(pageUser.getNumber());
        userResponse.setPageSize(pageUser.getSize());
        userResponse.setTotalElements(pageUser.getTotalElements());
        userResponse.setTotalPages(pageUser.getTotalPages());
        userResponse.setLastPage(pageUser.isLast());
        return userResponse;
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepo.delete(user);
    }

    private User dtoToUser(UserDto userDto){
        User user = modelMapper.map(userDto, User.class);
        return user;
    }

    private UserDto userToDto(User user){
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

}
