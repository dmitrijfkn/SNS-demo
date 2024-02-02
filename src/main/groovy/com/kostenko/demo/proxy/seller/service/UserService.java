package com.kostenko.demo.proxy.seller.service;


import com.kostenko.demo.proxy.seller.dto.UserPageDTO;
import com.kostenko.demo.proxy.seller.dto.UserRegistrationDTO;
import com.kostenko.demo.proxy.seller.dto.UserResponse;
import com.kostenko.demo.proxy.seller.entity.Authority;
import com.kostenko.demo.proxy.seller.entity.User;
import com.kostenko.demo.proxy.seller.error.ResourceNotFoundException;
import com.kostenko.demo.proxy.seller.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;
    private final ModelMapper modelMapper;
    protected static String ID_NOT_FOUND_MESSAGE = "User with id: \"%d\" doesn't exist.";
    protected static String USERNAME_NOT_FOUND_MESSAGE = "User with username: \"%d\" doesn't exist.";
    protected static String ACCESS_DENIED_MESSAGE = "Access denied. Insufficient permissions.";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityService authorityService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityService = authorityService;
        this.modelMapper = modelMapper;
    }

    //TODO write docs
    public UserResponse saveNewUser(UserRegistrationDTO userRequest, Set<Authority> authorities) {

        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        //  user.setCreatedAt(Instant.now());
        //  user.setUpdatedAt(Instant.now());

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(String.format(USERNAME_NOT_FOUND_MESSAGE, user.getUsername()));
        } else {
            if (authorities != null && !authorities.isEmpty()) {
                user.setAuthorities(authorities);
            }
            userRepository.save(user);
        }

        return modelMapper.map(user, UserResponse.class);
    }

    public UserPageDTO getUserPage(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        User user = userOptional.orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND_MESSAGE, userId)));

        return modelMapper.map(user, UserPageDTO.class);
    }

    public User findByUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new ResourceNotFoundException(String.format(USERNAME_NOT_FOUND_MESSAGE, username));
        }
        return userRepository.findByUsername(username);
    }


    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}