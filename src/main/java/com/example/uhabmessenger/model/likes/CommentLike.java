package com.example.uhabmessenger.model.likes;

import com.example.uhabmessenger.model.CommentModel;
import com.example.uhabmessenger.model.UserModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "comment_likes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "comment_like_id")
    private UUID likeId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "comment_id")
    private CommentModel comment;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private UserModel user;

}
