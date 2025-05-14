package com.example.uhabmessenger.service.post;

import com.example.uhabmessenger.model.PostModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OldScheduleRemoverService {

    private final SimplePostService simplePostService;

    public void checkForMarkAndRemove(Integer markThresholdDays, Integer alreadyDelThresholdDays) {

        List<PostModel> postList = simplePostService.findAll();
        //
        List<PostModel> postListForRemark = postList.stream()
                .filter(post -> isOlderThanXDays(post.getCreateAt(), markThresholdDays))
                .peek(post -> post.setRemoveMark(true))
                .toList();

        List<PostModel> postListForRemove = postList.stream()
                .filter(post -> isOlderThanXDays(post.getCreateAt(), alreadyDelThresholdDays))
                .toList();

        System.out.println("postListForRemark = " + postListForRemark);
        System.out.println("postListForRemove = " + postListForRemove);


//         remark
        simplePostService.save(postListForRemark);
        // del
        simplePostService.delete(postListForRemove);

    }

    public boolean isOlderThanXDays(LocalDateTime dateTime, int days) {
        LocalDateTime thresholdDateTime = LocalDateTime.now().minusDays(days);
        return dateTime.isBefore(thresholdDateTime);
    }

}
