package com.backend.blogApp.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    private String description;

    public void setId(int id) {
        this.id = id;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
