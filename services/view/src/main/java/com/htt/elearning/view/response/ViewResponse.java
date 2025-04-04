package com.htt.elearning.view.response;

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
}
