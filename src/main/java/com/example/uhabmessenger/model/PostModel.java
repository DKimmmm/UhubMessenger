package com.example.uhabmessenger.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private String title;

    private String description;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(
            name = "post_images",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
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
