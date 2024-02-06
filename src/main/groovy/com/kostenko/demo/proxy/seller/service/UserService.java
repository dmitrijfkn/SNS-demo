package com.kostenko.demo.proxy.seller.service;


import com.kostenko.demo.proxy.seller.dto.AuthRequestDTO;
import com.kostenko.demo.proxy.seller.dto.UserEditDTO;
import com.kostenko.demo.proxy.seller.dto.UserPageDTO;
import com.kostenko.demo.proxy.seller.dto.UserResponse;
import com.kostenko.demo.proxy.seller.entity.Authority;
import com.kostenko.demo.proxy.seller.entity.User;
import com.kostenko.demo.proxy.seller.error.ResourceNotFoundException;
import com.kostenko.demo.proxy.seller.repository.PostRepository;
import com.kostenko.demo.proxy.seller.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserService {

    protected static final String ID_NOT_FOUND_MESSAGE = "User with id: \"%s\" doesn't exist.";
    protected static final String USERNAME_ALREADY_EXISTS_MESSAGE = "User with username: \"%s\" already exist.";
    protected static final String ACCESS_DENIED_MESSAGE = "Access denied. Insufficient permissions.";
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserService(UserRepository userRepository, PostRepository postRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.mongoTemplate = mongoTemplate;
    }

    //TODO write docs
    public UserResponse saveNewUser(AuthRequestDTO userRequest, Set<Authority> authorities) {
        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(String.format(USERNAME_ALREADY_EXISTS_MESSAGE, user.getUsername()));
        } else {
            if (authorities != null && !authorities.isEmpty()) {
                user.setAuthorities(authorities);
            }
            userRepository.save(user);
        }

        return modelMapper.map(user, UserResponse.class);
    }


    public UserResponse edit(UserEditDTO userRequest, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND_MESSAGE, userId)));

        if (userRequest.getUsername() != null && !user.getUsername().equals(userRequest.getUsername())) {
            if (userRepository.existsByUsername(userRequest.getUsername())) {
                throw new IllegalArgumentException(String.format(USERNAME_ALREADY_EXISTS_MESSAGE, userRequest.getUsername()));
            } else {
                user.setUsername(userRequest.getUsername());
            }
        }

        if (userRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        userRepository.save(user);

        return modelMapper.map(user, UserResponse.class);
    }


    public UserPageDTO getUserPage(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND_MESSAGE, userId)));

        return modelMapper.map(user, UserPageDTO.class);
    }

    public User findByUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new ResourceNotFoundException(String.format(USERNAME_ALREADY_EXISTS_MESSAGE, username));
        }
        return userRepository.findByUsername(username);
    }


    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND_MESSAGE, userId)));

        postRepository.deleteAllByPostCreator(user);
        userRepository.delete(user);
    }


    @Transactional
    public void followToUser(String requesterId, String userIdToFollow) {
        if (!userRepository.existsById(userIdToFollow)) {
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND_MESSAGE, userIdToFollow));
        }

        Query query = Query.query(Criteria.where("_id").is(requesterId));
        Update update = new Update().addToSet("following", userIdToFollow);
        mongoTemplate.updateFirst(query, update, User.class);


        query = Query.query(Criteria.where("_id").is(userIdToFollow));
        update = new Update().addToSet("followers", requesterId);
        mongoTemplate.updateFirst(query, update, User.class);
    }


    @Transactional
    public void unfollowFromUser(String requesterId, String userIdToFollow) {
        Query query = Query.query(Criteria.where("_id").is(requesterId));
        Update update = new Update().pull("following", userIdToFollow);
        mongoTemplate.updateFirst(query, update, User.class);


        query = Query.query(Criteria.where("_id").is(userIdToFollow));
        update = new Update().pull("followers", requesterId);
        mongoTemplate.updateFirst(query, update, User.class);
    }
}