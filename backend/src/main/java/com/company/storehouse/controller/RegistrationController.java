package com.company.storehouse.controller;

import com.company.storehouse.controller.request.RegistrationRequest;
import com.company.storehouse.controller.response.TokenResponse;
import com.company.storehouse.model.User;
import com.company.storehouse.security.jwt.JwtTokenProvider;
import com.company.storehouse.service.RegistrationService;
import com.company.storehouse.validation.ValidationType;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;

    @Autowired
    public RegistrationController(RegistrationService registrationService, JwtTokenProvider jwtTokenProvider, ModelMapper modelMapper) {
        this.registrationService = registrationService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(value = "Registration form for users. Returns token", notes = "No need to use token.")
    @PostMapping("/registration")
    public ResponseEntity<TokenResponse> registration(@RequestBody @Validated(ValidationType.RegistrationValidation.class) RegistrationRequest request) {
        User user = modelMapper.map(request, User.class);
        registrationService.createUser(user);
        final String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(new TokenResponse(token));
    }

}
