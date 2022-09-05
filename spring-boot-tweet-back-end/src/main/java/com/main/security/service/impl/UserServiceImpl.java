package com.main.security.service.impl;

import com.main.security.model.User;
import com.main.security.repository.UserRepository;
import com.main.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUser(String userName) {
        return userRepository.findById(userName).orElseThrow(
                ()-> new UsernameNotFoundException("User is not exists!!"));
    }

    @Override
    public boolean isUserExist(String userName) {
        return userRepository.findById(userName).orElse(null) != null;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUserPassword(User user) {
        // user will retrieve then set password and save
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserForNotAdmin(String username, String systemUser) {
        return userRepository.findUserForNotAdmin(username, systemUser);
    }
}
