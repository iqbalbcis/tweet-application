package com.main.security.service;

import com.main.security.model.User;

public interface UserService {

    User createUser(User user);

    User findUser(String userName);

    boolean isUserExist(String userName);

    User updateUser(User user);

    void deleteUser(User user);

    User findUserForNotAdmin(String username, String systemUser);

}
