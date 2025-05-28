package com.example.web.service;

import com.example.web.dto.request.UserCreationReq;
import com.example.web.dto.request.UserUpdateReq;
import com.example.web.dto.response.UserResponse;
import com.example.web.entity.User;
import com.example.web.exception.AppException;
import com.example.web.exception.ErrorCode;
import com.example.web.mapper.IUserMapper;
import com.example.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    IUserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationReq request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(long id, UserUpdateReq request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save((user)));
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(userRepository.save((user)));
    }

    public User getCurrentUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
