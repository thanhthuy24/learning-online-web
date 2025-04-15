package com.htt.elearning.tag.response;

import com.htt.elearning.tag.pojo.Tag;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagResponse {
    private Long id;
    private String name;

    public static TagResponse fromTag(Tag tag){
        TagResponse tagResponse = TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
        return tagResponse;
    }
}
