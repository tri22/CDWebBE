package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long pdId;

    private String pdName;
    private double pdPrice;
    private String pdImage;
    private String pdCategory;
    private String pdDescribe;
    private String pdColor;
    private int pdRating;

}
