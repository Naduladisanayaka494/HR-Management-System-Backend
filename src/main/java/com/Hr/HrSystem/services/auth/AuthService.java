package com.Hr.HrSystem.services.auth;


import com.Hr.HrSystem.dto.SignUpRequest;
import com.Hr.HrSystem.dto.UserDto;
import com.Hr.HrSystem.entity.Account;

public interface AuthService {
    UserDto createUserWithAccount(SignUpRequest signupRequest, Account account);

    UserDto createdDataEntry(SignUpRequest signuprequest);
    UserDto createdAdmin(SignUpRequest signuprequest);
    boolean hasAdminwithemail(String email);
}
