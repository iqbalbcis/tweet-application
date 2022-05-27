package com.tweetapp.service.impl;

import com.tweetapp.model.LoginStatus;
import com.tweetapp.repository.LoginStatusRepository;
import com.tweetapp.service.LoginStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginStatusServiceImpl implements LoginStatusService {

    private LoginStatusRepository statusRepository;

    @Autowired
    public LoginStatusServiceImpl(final LoginStatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    @Transactional
    public LoginStatus saveLoginStatus(LoginStatus status) {
        return statusRepository.save(status);
    }

    @Override
    @Transactional
    public LoginStatus saveLogoutStatus(LoginStatus status) {
        return statusRepository.save(status);
    }
}
