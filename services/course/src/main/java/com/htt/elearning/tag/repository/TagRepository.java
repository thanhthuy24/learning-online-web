package com.htt.elearning.tag.repository;

import com.htt.elearning.tag.pojo.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
