package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "blogs")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String content;
    private String description;

}
