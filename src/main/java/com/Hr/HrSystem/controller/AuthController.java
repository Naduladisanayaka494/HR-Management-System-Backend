package com.Hr.HrSystem.controller;

import com.Hr.HrSystem.dto.AuthenticationRequest;
import com.Hr.HrSystem.dto.AuthenticationResponse;
import com.Hr.HrSystem.dto.SignUpRequest;
import com.Hr.HrSystem.dto.UserDto;
import com.Hr.HrSystem.entity.Account;
import com.Hr.HrSystem.entity.User;
import com.Hr.HrSystem.repository.AccountRepository;
import com.Hr.HrSystem.repository.UserRepository;
import com.Hr.HrSystem.services.auth.AuthServiceImpl;
import com.Hr.HrSystem.services.jwt.UserServiceImpl;
import com.Hr.HrSystem.utill.JWTUtill;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;
    private final UserServiceImpl userService;
    private final JWTUtill jwtUtill;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthServiceImpl authService, AuthenticationManager authenticationManager, UserServiceImpl userService, JWTUtill jwtUtill, UserRepository userRepository, AccountRepository accountRepository) {
        this.authService = authService;
        this.userService = userService;
        this.jwtUtill = jwtUtill;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
    }

    // API to create an Account
    @PostMapping("/create-account")
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        Account createdAccount = accountRepository.save(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    // Modified signup method to associate a user with an account
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signupRequest) {
        Optional<Account> accountOptional = accountRepository.findById(signupRequest.getAccountId());
        if (!accountOptional.isPresent()) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        Account account = accountOptional.get();
        if(authService.hasAdminwithemail(signupRequest.getEmail()))
            return new ResponseEntity<>("Email already exists", HttpStatus.NOT_ACCEPTABLE);

        UserDto createdUserDto = authService.createUserWithAccount(signupRequest, account);
        if (createdUserDto == null) {
            return new ResponseEntity<>("User not created", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public AuthenticationResponse createauthenticationtoken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException, DisabledException, UsernameNotFoundException, BadRequestException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
        }catch(BadCredentialsException e){
            throw new BadRequestException("incorrect username or passoword");
        }
        final UserDetails userDetails= userService.userDetailService().loadUserByUsername(authenticationRequest.getEmail());
        System.out.print(userDetails.getUsername());
        Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());
        final String jwt = jwtUtill.generateToken(userDetails);
        AuthenticationResponse authenticationResponse= new AuthenticationResponse();
        if(optionalUser.isPresent()){
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;

    }
}
