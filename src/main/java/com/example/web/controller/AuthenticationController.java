package com.example.web.controller;

import com.example.web.dto.request.AuthenticationReq;
import com.example.web.dto.request.IntrospectReq;
import com.example.web.dto.request.LogRequest;
import com.example.web.dto.response.ApiResponse;
import com.example.web.dto.response.AuthenticationResponse;
import com.example.web.dto.response.IntrospectResponse;
import com.example.web.entity.User;
import com.example.web.repository.UserRepository;
import com.example.web.service.AuthenticationService;
import com.example.web.service.JWTService;
import com.example.web.service.LogService;
import com.example.web.service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;
    LogService logService;
    private final UserRepository userRepository;
    @Autowired
    private final JWTService jWTService;

    @PostMapping("/login")
    AuthenticationResponse authenticate(@RequestBody AuthenticationReq req, HttpServletRequest request)
            throws JOSEException {
        try {
            var result = authenticationService.authenticate(req);
            String token = result.getToken();
            User user = userRepository.findByUsername(jWTService.extractUsername(token)).orElse(null);

            // Log login success
            logService.addLog(LogRequest.builder()
                    .action("LOGIN_SUCCESS")
                    .user(user)
                    .ip(request.getRemoteAddr())
                    .level("INFO")
                    .dataIn(req)
                    .dataOut("authenticated: " + result.isAuthenticated() + " " + ", role: " + result.getRole())
                    .date(new Date())
                    .resource("LOGIN")
                    .build());

            return AuthenticationResponse.builder()
                    .token(result.getToken())
                    .authenticated(result.isAuthenticated())
                    .role(result.getRole())
                    .build();
        } catch (Exception e) {
            // Log login failure
            logService.addLog(LogRequest.builder()
                    .action("LOGIN_FAILED")
                    .user(null)
                    .ip(request.getRemoteAddr())
                    .level("ERROR")
                    .dataIn(req)
                    .dataOut(e.getMessage())
                    .date(new Date())
                    .resource("LOGIN")
                    .build());

            throw e;
        }
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectReq req) throws JOSEException, ParseException {
        var result = authenticationService.introspect(req);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
