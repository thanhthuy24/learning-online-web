package com.htt.elearning.tag.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    @NotEmpty(message = "Tag name can't be null")
    private String name;
}
