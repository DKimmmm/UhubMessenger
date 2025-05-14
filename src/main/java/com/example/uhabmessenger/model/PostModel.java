package com.example.uhabmessenger.model;

import com.example.uhabmessenger.model.likes.PostLike;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(
            name = "post_images",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<ImageModel> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentModel> comments;

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PostLike> postLikes;

    public List<CommentModel> getComments() {

        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;

    }

    public List<PostLike> getPostLikes() {

        if (Objects.isNull(postLikes)) {
            postLikes = new ArrayList<>();
        }
        return postLikes;

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

    public void addLike(PostLike postLike) {

        postLike.setPost(this);
        getPostLikes().add(postLike);

    }

    public void removeLike(PostLike first) {

        getPostLikes().remove(first);

    }
}
