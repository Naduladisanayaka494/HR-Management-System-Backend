package com.Hr.HrSystem.services.auth;

import com.Hr.HrSystem.dto.SignUpRequest;
import com.Hr.HrSystem.dto.UserDto;
import com.Hr.HrSystem.entity.Account;
import com.Hr.HrSystem.entity.User;
import com.Hr.HrSystem.enums.UserRole;
import com.Hr.HrSystem.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUserWithAccount(SignUpRequest signupRequest, Account account) {
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.DataEntry);
        user.setAccount(account);

        User createdUser = userRepository.save(user);
        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        return userDto;
    }

    @Override
    public UserDto createdDataEntry(SignUpRequest signuprequest) {
        return null;
    }

    @Override
    public UserDto createdAdmin(SignUpRequest signuprequest) {
        return null;
    }

    @Override
    public boolean hasAdminwithemail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
