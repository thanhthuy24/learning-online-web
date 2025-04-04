package com.htt.elearning.videocompleted.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoCompletedDTO {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("video_id")
    private Long videoId;
}
