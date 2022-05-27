package com.tweetapp.service;

import com.tweetapp.model.LoginStatus;

public interface LoginStatusService {

    LoginStatus saveLoginStatus(LoginStatus status);
    LoginStatus saveLogoutStatus(LoginStatus status);
}
