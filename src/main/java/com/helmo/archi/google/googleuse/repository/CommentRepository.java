package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
