package com.example.web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String content;
    private Date createAt;
    private double rating;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
