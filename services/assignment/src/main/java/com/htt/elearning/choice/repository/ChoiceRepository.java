package com.htt.elearning.choice.repository;

import com.htt.elearning.choice.pojo.Choice;
import com.htt.elearning.question.pojo.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    List<Choice> findByQuestionIn(List<Question> questions);
}
