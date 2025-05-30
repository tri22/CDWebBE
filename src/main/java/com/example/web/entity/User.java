package com.example.web.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "cart")
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String username;
    private String password;
    @Column(name = "fullname")
    private String fullName;
    private String email;
    private String phone;
    private LocalDate birthday;
    private String role;
    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Cart cart;
    // @OneToMany(mappedBy = "users")
    // private Set<Order> orders;
    // @OneToMany(mappedBy = "users")
    // private Set<Comment> comments;

}
