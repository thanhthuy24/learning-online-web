package com.htt.elearning.assignment.response;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentListResponse {
    private List<AssignmentResponse> assignments;
    private int totalPages;
}