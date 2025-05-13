package com.example.uhabmessenger.model;

import com.example.uhabmessenger.model.likes.CommentLike;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "comment_id")
    private UUID commentId;

    @Column(name = "\"text\"")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "post_id")
    private PostModel post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> likes;

    public List<CommentLike> getLikes() {

        if (Objects.isNull(likes)) {
            likes = new ArrayList<>();
        }
        return likes;

    }

    public void addLike(CommentLike like) {

        like.setComment(this);
        getLikes().add(like);

    }

    public void removeLike(CommentLike like) {

        getLikes().remove(like);

    }

}
