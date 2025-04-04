package com.htt.elearning.essay.response;

import com.htt.elearning.essay.pojo.Essay;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EssayListResponse {
    private List<Essay> essays;
    private int totalPages;
}
