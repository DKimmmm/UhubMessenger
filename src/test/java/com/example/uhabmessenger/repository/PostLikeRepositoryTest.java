package com.example.uhabmessenger.repository;

import com.example.uhabmessenger.bd.init.BaseIntegrationTest;
import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.model.UserModel;
import com.example.uhabmessenger.model.likes.PostLike;
import com.example.uhabmessenger.repository.entity.PostLikeRepository;
import com.example.uhabmessenger.repository.entity.PostRepository;
import com.example.uhabmessenger.repository.entity.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

public class PostLikeRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void repositoryDel() {
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
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

    @Test
    @Transactional
    void saveAndFindTest() {

        UserModel userModel = saveIntoDBAndGetUser("Name", "email@gmail.com");
        UserModel userModel1 = saveIntoDBAndGetUser("Name1", "email1@gmail.com");
        PostModel postModel = saveIntoDBAndGetPost();

        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(postRepository.count()).isEqualTo(1);

        PostLike postLike = PostLike.builder()
                .post(postModel)
                .user(userModel)
                .build();

        PostLike postLike1 = PostLike.builder()
                .post(postModel)
                .user(userModel1)
                .build();

        postLike = postLikeRepository.save(postLike);
        postLikeRepository.save(postLike1);

        assertThat(postLikeRepository.count()).isEqualTo(2);
        assertThat(postLikeRepository.findByPostLikeId(postLike.getPostLikeId()).orElse(null)).isNotNull();


    }

    @Test
    @Transactional
    void cascadeSaveTest() {

        UserModel userModel = saveIntoDBAndGetUser("Name", "email@gmail.com");
        UserModel userModel1 = saveIntoDBAndGetUser("Name1", "email1@gmail.com");
        PostModel postModel = saveIntoDBAndGetPost();

        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(postRepository.count()).isEqualTo(1);

        PostLike postLike = PostLike.builder()
                .post(postModel)
                .user(userModel)
                .build();

        PostLike postLike1 = PostLike.builder()
                .post(postModel)
                .user(userModel1)
                .build();

        postModel.addLike(postLike);
        postModel.addLike(postLike1);

        postRepository.save(postModel);

        assertThat(postLikeRepository.count()).isEqualTo(2);

    }

    @Test
    @Transactional
    void orphanRemoveTest() {

        UserModel userModel = saveIntoDBAndGetUser("Name", "email@gmail.com");
        UserModel userModel1 = saveIntoDBAndGetUser("Name1", "email1@gmail.com");
        PostModel postModel = saveIntoDBAndGetPost();

        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(postRepository.count()).isEqualTo(1);

        PostLike postLike = PostLike.builder()
                .post(postRepository.findByPostId(postModel.getPostId()).get())
                .user(userRepository.findByUserId(userModel.getUserId()).get())
                .build();

        PostLike postLike1 = PostLike.builder()
                .post(postRepository.findByPostId(postModel.getPostId()).get())
                .user(userRepository.findByUserId(userModel1.getUserId()).get())
                .build();

        postModel.addLike(postLike);
        postModel.addLike(postLike1);

        PostLike like = postLikeRepository.findByUser(userModel).orElseThrow(
                () -> new RuntimeException("asd")
        );

        postModel.removeLike(like);

        assertThat(postLikeRepository.count()).isEqualTo(1);

        PostModel post = postRepository.findAll().getFirst();
        assertThat(post.getPostLikes().size()).isEqualTo(1);

    }

    @Test
    @Transactional
    void cascadeRemoveTest() {

        UserModel userModel = saveIntoDBAndGetUser("Name", "email@gmail.com");
        UserModel userModel1 = saveIntoDBAndGetUser("Name1", "email1@gmail.com");
        PostModel postModel = saveIntoDBAndGetPost();

        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(postRepository.count()).isEqualTo(1);

        PostLike postLike = PostLike.builder()
                .post(postModel)
                .user(userModel)
                .build();

        PostLike postLike1 = PostLike.builder()
                .post(postModel)
                .user(userModel1)
                .build();

        postModel.addLike(postLike);
        postModel.addLike(postLike1);

        postModel = postRepository.save(postModel);

        postRepository.delete(postModel);

        assertThat(postRepository.count()).isEqualTo(0);
        assertThat(postLikeRepository.count()).isEqualTo(0);

    }
}