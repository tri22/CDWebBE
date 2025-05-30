package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String image;
    private String description;
    private String color;
    private int rating;

}
