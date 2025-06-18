package com.example.uhabmessenger.repository;

import com.example.uhabmessenger.bd.init.BaseIntegrationTest;
import com.example.uhabmessenger.model.CommentModel;
import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.model.likes.CommentLike;
import com.example.uhabmessenger.model.likes.PostLike;
import com.example.uhabmessenger.repository.entity.CommentLikeRepository;
import com.example.uhabmessenger.repository.entity.CommentRepository;
import com.example.uhabmessenger.repository.entity.PostRepository;
import com.example.uhabmessenger.repository.entity.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentLikeRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void repositoryDel() {
        commentLikeRepository.deleteAll();
        commentRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    private UserModel saveIntoDBAndGetUser(String name, String email) {

        UserModel userModel = UserModel.builder()
                .name(name)
                .password("Password1")
                .email(email)
                .lastname("Lastname")
                .build();

        return userRepository.save(userModel);

    }

    private PostModel saveIntoDBAndGetPost() {

        PostModel postModel = PostModel.builder()
                .title("Title")
                .description("des")
                .build();
        return postRepository.save(postModel);

    }

    private CommentModel saveIntoDBAndGetComment(UserModel user, PostModel post) {

        CommentModel postModel = CommentModel.builder()
                .text("Text")
                .user(user)
                .post(post)
                .build();

        return commentRepository.save(postModel);

    }

    @Test
    @Transactional
    void saveAndFindTest() {

        UserModel userModel = saveIntoDBAndGetUser("Name", "email@gmail.com");
        UserModel userModel1 = saveIntoDBAndGetUser("Name1", "email1@gmail.com");
        PostModel postModel = saveIntoDBAndGetPost();

        CommentModel commentModel = saveIntoDBAndGetComment(userModel, postModel);

        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(commentRepository.count()).isEqualTo(1);

        CommentLike like = CommentLike.builder()
                .comment(commentModel)
                .user(userModel)
                .build();

        CommentLike like1 = CommentLike.builder()
                .comment(commentModel)
                .user(userModel1)
                .build();

        CommentLike likeNew = commentLikeRepository.save(like);
        commentLikeRepository.save(like1);

        assertThat(commentLikeRepository.count()).isEqualTo(2);
        assertThat(commentLikeRepository.findByLikeId(likeNew.getLikeId()).orElse(null)).isNotNull();

    }

    @Test
    @Transactional
    void cascadeSaveTest() {

        UserModel userModel = saveIntoDBAndGetUser("Name", "email@gmail.com");
        UserModel userModel1 = saveIntoDBAndGetUser("Name1", "email1@gmail.com");
        PostModel postModel = saveIntoDBAndGetPost();

        CommentModel commentModel = saveIntoDBAndGetComment(userModel, postModel);

        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(commentRepository.count()).isEqualTo(1);

        CommentLike like = CommentLike.builder()
                .comment(commentModel)
                .user(userModel)
                .build();

        CommentLike like1 = CommentLike.builder()
                .comment(commentModel)
                .user(userModel1)
                .build();

        commentModel.addLike(like);
        commentModel.addLike(like1);

        commentRepository.save(commentModel);

        assertThat(commentLikeRepository.count()).isEqualTo(2);

    }

    @Test
    @Transactional
    void orphanRemoveTest() {

        UserModel userModel = saveIntoDBAndGetUser("Name", "email@gmail.com");
        UserModel userModel1 = saveIntoDBAndGetUser("Name1", "email1@gmail.com");
        PostModel postModel = saveIntoDBAndGetPost();

        CommentModel commentModel = saveIntoDBAndGetComment(userModel, postModel);

        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(commentRepository.count()).isEqualTo(1);

        CommentLike like = CommentLike.builder()
                .comment(commentModel)
                .user(userModel)
                .build();

        CommentLike like1 = CommentLike.builder()
                .comment(commentModel)
                .user(userModel1)
                .build();


        commentModel.addLike(like);
        commentModel.addLike(like1);

        CommentLike likeFromDB = commentLikeRepository.findByUser(userModel).orElseThrow(
                () -> new RuntimeException("error")
        );

        commentModel.removeLike(likeFromDB);

        assertThat(commentLikeRepository.count()).isEqualTo(1);

        CommentModel comment = commentRepository.findAll().getFirst();
        assertThat(comment.getLikes().size()).isEqualTo(1);

    }

    @Test
    @Transactional
    void cascadeRemoveTest() {

        UserModel userModel = saveIntoDBAndGetUser("Name", "email@gmail.com");
        UserModel userModel1 = saveIntoDBAndGetUser("Name1", "email1@gmail.com");
        PostModel postModel = saveIntoDBAndGetPost();

        CommentModel commentModel = saveIntoDBAndGetComment(userModel, postModel);

        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(commentRepository.count()).isEqualTo(1);

        CommentLike like = CommentLike.builder()
                .comment(commentModel)
                .user(userModel)
                .build();

        CommentLike like1 = CommentLike.builder()
                .comment(commentModel)
                .user(userModel1)
                .build();


        commentModel.addLike(like);
        commentModel.addLike(like1);

        CommentModel commentFromDB = commentRepository.save(commentModel);

        commentRepository.delete(commentFromDB);

        assertThat(commentRepository.count()).isEqualTo(0);
        assertThat(commentLikeRepository.count()).isEqualTo(0);

    }
}