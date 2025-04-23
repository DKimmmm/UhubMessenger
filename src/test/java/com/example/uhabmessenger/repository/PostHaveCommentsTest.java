package com.example.uhabmessenger.repository;

import com.example.uhabmessenger.bd.init.BaseIntegrationTest;
import com.example.uhabmessenger.model.CommentModel;
import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.repository.entity.CommentRepository;
import com.example.uhabmessenger.repository.entity.PostRepository;
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

    @BeforeEach
    public void delBD() {
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @Transactional
    void cascadeTest() {
        PostModel postModel = PostModel.builder().tittle("title").description("des").build();
        CommentModel commentModel = CommentModel.builder().text("text").build();
        postModel.getComments().add(commentModel);
        postRepository.save(postModel);
        Assertions.assertThat(postRepository.count()).isEqualTo(1);
        Assertions.assertThat(commentRepository.count()).isEqualTo(1);
    }

}
