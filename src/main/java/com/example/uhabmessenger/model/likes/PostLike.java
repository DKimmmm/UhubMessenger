package com.example.uhabmessenger.model.likes;

import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.model.UserModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "post_likes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_like_id")
    private UUID postLikeId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "post_id")
    private PostModel post;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private UserModel user;

}
