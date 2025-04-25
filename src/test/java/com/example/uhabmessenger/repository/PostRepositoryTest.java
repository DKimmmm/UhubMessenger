package com.example.uhabmessenger.repository;

import com.example.uhabmessenger.bd.init.BaseIntegrationTest;
import com.example.uhabmessenger.model.ImageModel;
import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.repository.entity.ImageRepository;
import com.example.uhabmessenger.repository.entity.PostRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    public void repositoryDel() {
        postRepository.deleteAll();
        imageRepository.deleteAll();
    }

    @Test
    void saveAndFindTest() {
        PostModel postModel = postRepository.save(
                PostModel.builder()
                        .title("tt")
                        .description("dd")
                        .build());
        PostModel model = postRepository.findById(postModel.getPostId()).orElse(null);
        assertThat(model).isNotNull();
    }

    @Test
    void saveAndDelete() {
        PostModel postModel = postRepository.save(
                PostModel.builder()
                        .title("tt")
                        .description("dd")
                        .build());
        assertThat(postRepository.findAll().size()).isEqualTo(1);
        postRepository.deleteById(postModel.getPostId());
        assertThat(postRepository.findAll().size()).isEqualTo(0);
    }


    @Test
    @Transactional
    void orphanRemovalImagesTest() {
        List<ImageModel> images = imageInitialized();
        PostModel postModel = postModelInitialized(images);
        PostModel postModel1 = postRepository.findById(postModel.getPostId()).orElse(null);
        assertThat(postModel1).isNotNull();
        List<ImageModel> images1 = postModel1.getImages();
        assertThat(images1.size()).isEqualTo(2);

        images1.remove(1);
        assertThat(images1.size()).isEqualTo(1);

        assertThat(imageRepository.findAll().size()).isEqualTo(1);

    }

    private PostModel postModelInitialized(List<ImageModel> images) {
        return postRepository.save(PostModel.builder()
                .title("title")
                .description("description")
                .title("title")
                .images(images)
                .build());
    }

    private List<ImageModel> imageInitialized() {
        List<ImageModel> result = new ArrayList<>();
        result.add(imageRepository.save(ImageModel.builder()
                .fileName("name")
                .size(1234L)
                .contentType("image-json")
                .build()));
        result.add(imageRepository.save(ImageModel.builder()
                .fileName("2name2")
                .size(11111L)
                .contentType("image-json")
                .build()));
        return result;
    }


    @Test
    @Transactional
    void imageSaveWithPost() {
        ImageModel imageModel = ImageModel.builder()
                .contentType("ct")
                .size(12345L)
                .fileName("FName")
                .build();
        assertThat(imageRepository.findAll().size()).isEqualTo(0);
        PostModel postBuilt = PostModel.builder()
                .title("tt")
                .description("des")
                .build();

        PostModel postFromBD = postRepository.save(postBuilt);
        assertThat(imageRepository.findAll().size()).isEqualTo(0);
        postFromBD.getImages().add(imageModel);
        postRepository.save(postFromBD);
        assertThat(imageRepository.findAll().size()).isEqualTo(1);
    }


}
