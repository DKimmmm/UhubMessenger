package com.example.uhabmessenger.repository;

import com.example.uhabmessenger.bd.init.BaseIntegrationTest;
import com.example.uhabmessenger.model.CommentModel;
import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.repository.entity.CommentRepository;
import com.example.uhabmessenger.repository.entity.PostRepository;
import com.example.uhabmessenger.repository.entity.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PostHaveCommentsTest extends BaseIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void delBD() {
        postRepository.deleteAll();
        commentRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void cascadeTest() {
        UserModel userModel = justUserCreatorRepository();

        Assertions.assertThat(userRepository.count()).isEqualTo(1);

        PostModel postModel = PostModel.builder().tittle("title").description("des").build();
        CommentModel commentModel = justCommentCreator(userModel);

        postModel.addComment(commentModel);
        postRepository.save(postModel);

        Assertions.assertThat(postRepository.count()).isEqualTo(1);
        Assertions.assertThat(commentRepository.count()).isEqualTo(1);
    }

    private UserModel justUserCreatorRepository() {
        return userRepository.save(
                UserModel.builder().name("name").lastname("last").password("pass").build()
        );
    }

    private CommentModel justCommentCreator(UserModel userModel) {
        return CommentModel.builder().text("text").user(userModel).build();
    }

    @Test
    @Transactional
    void orphanDelTest() {
        UserModel userModel = justUserCreatorRepository();

        CommentModel commentModel = justCommentCreator(userModel);
        CommentModel commentModel1 = justCommentCreator(userModel);

        PostModel postModel = PostModel.builder().tittle("title").description("des").build();

        postModel.addComment(commentModel);
        postModel.addComment(commentModel1);

        postRepository.save(postModel);

        Assertions.assertThat(commentRepository.count()).isEqualTo(2);

        postModel.getComments().remove(commentModel1);
        postRepository.save(postModel);

        Assertions.assertThat(commentRepository.count()).isEqualTo(1);

        postRepository.deleteAll();

        Assertions.assertThat(commentRepository.count()).isEqualTo(0);

    }




}
