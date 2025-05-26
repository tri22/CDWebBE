package com.example.web.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    long id;
    String username;
    String password;
    String firstname;
    String lastname;
    String email;
    String phone;
    LocalDate birthday;

    String role;
}
