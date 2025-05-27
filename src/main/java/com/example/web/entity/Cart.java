package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart")
    private Set<CartItem> items;
}
