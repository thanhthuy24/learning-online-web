package com.htt.elearning.view.response;

import com.htt.elearning.view.pojo.View;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewResponse {
    private Long id;
    private Date createdDate;
    private Long courseId;
    private Long userId;
    private Long viewCount;

    public static ViewResponse fromView(View view) {
        ViewResponse viewResponse1 = ViewResponse.builder()
                .id(view.getId())
                .courseId(view.getCourseId())
                .userId(view.getUserId())
                .viewCount(view.getViewCount())
                .build();
        viewResponse1.setCreatedDate(new Date());
        return viewResponse1;
    }
}
