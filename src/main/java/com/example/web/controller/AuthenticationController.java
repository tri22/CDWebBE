package com.example.web.controller;

import com.example.web.dto.request.AuthenticationReq;
import com.example.web.dto.request.IntrospectReq;
import com.example.web.dto.response.ApiResponse;
import com.example.web.dto.response.AuthenticationResponse;
import com.example.web.dto.response.IntrospectResponse;
import com.example.web.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    AuthenticationResponse authenticate(@RequestBody @Valid AuthenticationReq req) throws JOSEException {
        var result = authenticationService.authenticate(req);
        return AuthenticationResponse.builder()
                .token(result.getToken())
                .authenticated(result.isAuthenticated())
                .role(result.getRole())
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectReq req) throws JOSEException, ParseException {
        var result = authenticationService.introspect(req);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
