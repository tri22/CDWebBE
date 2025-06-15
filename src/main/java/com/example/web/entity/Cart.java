package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"user", "items"})
@Getter
@Setter
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items;

}
