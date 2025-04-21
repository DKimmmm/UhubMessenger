package com.example.uhabmessenger.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "\"groups\"")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GroupModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "group_id")
    private UUID groupId;

    private String title;

    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "group_users",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference // Для предотвращения рекурсии при сериализации
    private List<UserModel> users = new ArrayList<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(
            name = "group_posts",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<PostModel> posts = new ArrayList<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(
            name = "group_images",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<ImageModel> images = new ArrayList<>();

    //методы сериализации которые необходимы для связи многие к многим
    public void addUser(UserModel user){
        this.getUsers().add(user);
        user.getGroups().add(this);
    }
    public void removeUser(UserModel user){
        this.getUsers().remove(user);
        user.getGroups().remove(this);
    }

    public List<UserModel> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    public List<PostModel> getPosts() {
        if (posts == null) {
            posts = new ArrayList<>();
        }
        return posts;
    }

    public List<ImageModel> getImages() {
        if (images == null) {
            images = new ArrayList<>();
        }
        return images;
    }
}
