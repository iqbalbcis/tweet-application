package com.tweetapp.security.service;

import com.tweetapp.security.model.User;

import java.util.List;

public interface UserService {

    User userRegistration(User user);

    User updateUser(User user);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> getAllUsers();
}
