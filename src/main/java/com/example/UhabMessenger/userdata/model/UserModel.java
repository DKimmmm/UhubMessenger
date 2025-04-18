package com.example.UhabMessenger.userdata.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
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

    @Column(name = "approved_phone", insertable = false)
    private Boolean approvedPhone;

    @Column(name = "approved_email", insertable = false)
    private Boolean approvedEmail;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_images", // Имя промежуточной таблицы
            joinColumns = @JoinColumn(name = "user_id"), // Колонка для User
            inverseJoinColumns = @JoinColumn(name = "image_id")// Колонка для Image
    )
    private List<ImageModel> images = new ArrayList<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_posts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<PostModel> posts = new ArrayList<>();


    @ManyToMany(mappedBy = "users")
    @JsonBackReference // Для предотвращения рекурсии
    private List<GroupModel> groups = new ArrayList<>();

    public List<ImageModel> getImages() {
        if (images == null) {
            images = new ArrayList<>();
        }
        return images;
    }

    public List<PostModel> getPosts() {
        if (posts == null) {
            posts = new ArrayList<>();
        }
        return posts;
    }

    public List<GroupModel> getGroups() {
        if (groups == null) {
            groups = new ArrayList<>();
        }
        return groups;
    }
}
