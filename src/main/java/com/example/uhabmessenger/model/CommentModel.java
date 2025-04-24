package com.example.uhabmessenger.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "comments")
@Entity
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

}
