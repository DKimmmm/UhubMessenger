package com.example.uhabmessenger.service.post;

import com.example.uhabmessenger.model.CommentModel;
import com.example.uhabmessenger.model.PostModel;
import com.example.uhabmessenger.model.likes.PostLike;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class PostSpecifications {
    public static Specification<PostModel> withFilters(Boolean isSortedByLikes,
                                                       Boolean isSortedByComments, String searchedBy) {

        return (Root<PostModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Predicate predicate = cb.conjunction(); // Start with true

            // Search by text if provided
            if (searchedBy != null && !searchedBy.trim().isEmpty()) {

                String searchPattern = "%" + searchedBy.trim().toLowerCase() + "%";
                Predicate titlePredicate = cb.like(cb.lower(root.get("title")), searchPattern);
                Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), searchPattern);
                predicate = cb.and(predicate, cb.or(titlePredicate, descriptionPredicate));

            }

            // Exclude removed posts
            predicate = cb.and(predicate, cb.equal(root.get("removeMark"), false));

            // Sorting
            if (Objects.nonNull(query)) {

                // Sorting
                if (Boolean.TRUE.equals(isSortedByLikes)) {
                    // Join with postLikes and count
                    Join<PostModel, PostLike> likesJoin = root.join("postLikes", JoinType.LEFT);
                    Expression<Long> likeCount = cb.count(likesJoin);
                    query.groupBy(root.get("postId"));
                    // Add the count to the select list to satisfy DISTINCT and ORDER BY requirements
                    query.multiselect(root, likeCount);
                    query.orderBy(cb.desc(likeCount));
                } else if (Boolean.TRUE.equals(isSortedByComments)) {
                    // Join with comments and count
                    Join<PostModel, CommentModel> commentsJoin = root.join("comments", JoinType.LEFT);
                    Expression<Long> commentCount = cb.count(commentsJoin);
                    query.groupBy(root.get("postId"));
                    query.multiselect(root, commentCount);
                    query.orderBy(cb.desc(commentCount));
                } else {
                    // Default sorting by creation date
                    query.orderBy(cb.desc(root.get("createAt")));
                }

            }

            return predicate;

        };

    }

}