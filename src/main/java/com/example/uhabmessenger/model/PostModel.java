package com.example.uhabmessenger.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Entity
@Table(name = "posts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id")
    private UUID postId;

    @Column(name = "tittle")
    private String tittle;

    @Column(name = "description")
    private String description;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(
            name = "post_images", // Имя промежуточной таблицы
            joinColumns = @JoinColumn(name = "post_id"), // Колонка для post
            inverseJoinColumns = @JoinColumn(name = "image_id") // Колонка для Image
    )
    private List<ImageModel> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentModel> comments;

    public List<CommentModel> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }

    public void addComment(CommentModel comment) {
        comment.setPost(this);
        getComments().add(comment);
    }

    public List<ImageModel> getImages() {
        if (images == null) {
            images = new ArrayList<>();
        }
        return images;
    }

}
