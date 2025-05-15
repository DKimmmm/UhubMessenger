package com.example.uhabmessenger.service.post;

import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.repository.entity.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimplePostService {

    private final PostRepository postRepository;

    public void delete(PostModel postModel) {

        try {
            postRepository.delete(postModel);
        } catch (Exception e) {
            throw new RuntimeException("error at post delete");
        }

    }

    public void delete(List<PostModel> postModels) {

        try {
            postRepository.deleteAll(postModels);
        } catch (Exception e) {
            throw new RuntimeException("error at post delete");
        }

    }

    public PostModel save(PostModel post) {

        try {
            return postRepository.save(post);
        } catch (Exception e) {
            throw new RuntimeException("error at post save");
        }

    }

    public void save(List<PostModel> postList) {

        try {
            postRepository.saveAll(postList);
        } catch (Exception e) {
            throw new RuntimeException("error at post save");
        }

    }

    public List<PostModel> findAll() {

        try {
            return postRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("error at post find all");
        }

    }

    public PostModel findById(UUID postId) {

        return postRepository.findByPostId(postId).orElseThrow(
                () -> new EntityNotFoundException("post not found by " + postId)
        );

    }

    public List<PostModel> findAllByRemoveMark(boolean isRemoved) {

        return postRepository.findAllByRemoveMark(isRemoved);

    }

    public List<PostModel> getAllBySpecification(Specification<PostModel> postModelSpecification, Pageable pageable) {

        return postRepository.findAll(postModelSpecification, pageable)
                .stream()
                .toList();

    }

}
