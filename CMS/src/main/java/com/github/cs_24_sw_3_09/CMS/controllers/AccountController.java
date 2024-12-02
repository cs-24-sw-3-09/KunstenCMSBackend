package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.AuthLoginDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.AuthResetDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.AuthResetNewDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.UserDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.EmailDetailsEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.services.EmailService;
import com.github.cs_24_sw_3_09.CMS.services.JwtService;
import com.github.cs_24_sw_3_09.CMS.services.UserService;
import com.github.cs_24_sw_3_09.CMS.services.serviceImpl.JwtServiceImpl.TOKEN_TYPE;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final UserService userService;
    private final EmailService emailService;
    
        @Autowired
        public AccountController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService, Mapper<UserEntity, UserDto> userMapper, EmailService emailService) {
            this.authenticationManager = authenticationManager;
            this.jwtService = jwtService;
            this.userService = userService;
            this.userMapper = userMapper;
            this.emailService = emailService;
    }

    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody AuthLoginDto authRequest) {
        // Authenticate with provided email and password from AuthRequestDto
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        //If valid email and password return jwt token for provided email (unique for our user).
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail(), TOKEN_TYPE.AUTH_TOKEN);
        } else {
            throw new UsernameNotFoundException("Invalid request!");
        }
    }

    @GetMapping
    public ResponseEntity<UserDto> getLoggedInUser() {
        UserDto userDto = userMapper.mapTo(userService.getLoggedInUser().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged-in user not found")
        ));
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping
    public ResponseEntity<UserDto> partialUpdateLoggedInUser(@Valid @RequestBody UserDto userDto) {
        UserDto loggedInUserDto = userMapper.mapTo(userService.getLoggedInUser().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged-in user not found")
        ));

        if (loggedInUserDto == null) {
            return new ResponseEntity(null, HttpStatus.FORBIDDEN);
        }

        UserEntity userEntity = userMapper.mapFrom(userDto);
        UserEntity savedUserEntity = userService.partialUpdate((long) loggedInUserDto.getId(), userEntity);

        return new ResponseEntity<>(userMapper.mapTo(savedUserEntity), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<HttpStatus> resetPasswordForUser(@Valid @RequestBody AuthResetDto resetPasswordDto) {
        Optional<UserEntity> user = userService.findByEmail(resetPasswordDto.getEmail());
        if(!user.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String resetToken = jwtService.generateToken(resetPasswordDto.getEmail(), TOKEN_TYPE.RESET_TOKEN);
        EmailDetailsEntity resetEmail = EmailDetailsEntity.builder()
        .recipient(resetPasswordDto.getEmail())
        .msgBody("Reset your password using the following link (valid for 5 minutes):<br>http://example.com/reset-password?token=" + resetToken)
        .subject("Password reset request - " + resetPasswordDto.getEmail())
        .build();
        emailService.sendSimpleMail(resetEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset-password/new")
    public ResponseEntity<HttpStatus> resetPasswordForUserWithtToken(@Valid @RequestBody AuthResetNewDto resetPasswordNewDto) {
        Optional<UserEntity> optionalUser = userService.findByEmail(resetPasswordNewDto.getEmail());
        if(!optionalUser.isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(!jwtService.validateResetToken(resetPasswordNewDto.getToken(), resetPasswordNewDto.getEmail())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        UserEntity user = optionalUser.get();
        user.setPassword(new BCryptPasswordEncoder().encode(resetPasswordNewDto.getPassword()));
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
