package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
