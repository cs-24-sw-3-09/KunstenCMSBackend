package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.AuthRequestDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.UserDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.services.JwtService;
import com.github.cs_24_sw_3_09.CMS.services.UserService;
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

    @Autowired
    public AccountController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody AuthRequestDto authRequest) {
        // Authenticate with provided email and password from AuthRequestDto
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        //If valid email and password return jwt token for provided email (unique for our user).
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail());
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
}
