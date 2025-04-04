package com.htt.elearning.userassignmentdone.service;

import com.htt.elearning.userassignmentdone.dto.AssignmentDoneDTO;
import com.htt.elearning.userassignmentdone.pojo.Userassignmentdone;

public interface AssignmentDoneService {
    Userassignmentdone createAssignmentDone(AssignmentDoneDTO assignmentDoneDTO);
    Userassignmentdone getAssignmentDone(Long assignmentId);
    Long getCountByAssignmentId(Long assignmentId);
}
