package com.example.UhabMessenger.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "\"name\"")
    private String name;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    @Column(name = "approved_phone")
    private Boolean approvedPhone;
    @Column(name = "approved_email")
    private Boolean approvedEmail;

    public UserModel(String name, String lastname, String password, boolean approvedPhone, boolean approvedEmail) {
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.approvedPhone = approvedPhone;
        this.approvedEmail = approvedEmail;
    }
}
