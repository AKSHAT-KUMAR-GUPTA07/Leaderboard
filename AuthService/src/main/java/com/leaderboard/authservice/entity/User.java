package com.leaderboard.authservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String role;

    private boolean enabled=true;

    private Long createdAt = System.currentTimeMillis();
}
