package com.htt.elearning.essay.service;

import com.htt.elearning.essay.dto.EssayDTO;
import com.htt.elearning.essay.pojo.Essay;
import com.htt.elearning.essay.response.EssayResponseClient;
import com.htt.elearning.exceptions.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface EssayService {
    Essay createEssay(EssayDTO essayDTO) throws DataNotFoundException;
    Essay updateEssay(Long essayId, EssayDTO essayDTO) throws DataNotFoundException;
    //    Essay getEssayByAssignmentId(Long essayId);
    Page<EssayResponseClient> getEssaysByAssignment(Long assignmentId, PageRequest pageRequest);
}
