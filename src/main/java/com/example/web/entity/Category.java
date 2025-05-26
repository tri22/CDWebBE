package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "category")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;
    private String name;
    @OneToMany(mappedBy = "category")
    private Set<Product> productList = new HashSet<>();


}
