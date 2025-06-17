package com.example.web.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationReq {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String email;
    String fullName;
    String role;
    LocalDate birthday; // thêm dòng này
    String phone;
}
