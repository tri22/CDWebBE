package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private LocalDate birthday;
    private String role;
//    @OneToOne(mappedBy = "users")
//    private Cart cart;
//    @OneToMany(mappedBy = "users")
//    private Set<Order> orders;
//    @OneToMany(mappedBy = "users")
//    private Set<Comment> comments;

}
