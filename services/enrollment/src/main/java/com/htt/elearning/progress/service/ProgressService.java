package com.htt.elearning.progress.service;

import com.htt.elearning.progress.pojo.Progress;

import java.util.Optional;

public interface ProgressService {
    float calculateProgress(Long courseId);
    Optional<Progress> getProgressByAdmin(Long userId, Long courseId);
    Boolean checkProgressForCertificate(Long userId, Long courseId);
    Optional<Progress> getProgressByUser(Long courseId);
    float createNewProgress(Long courseId);
}
