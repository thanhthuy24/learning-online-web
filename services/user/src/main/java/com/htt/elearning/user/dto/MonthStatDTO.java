package com.htt.elearning.user.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthStatDTO {
    private String month;
    private Long total;
}
