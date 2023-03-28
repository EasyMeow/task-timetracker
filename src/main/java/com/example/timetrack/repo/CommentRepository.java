package com.example.timetrack.repo;

import com.example.timetrack.entity.Comment;
import com.example.timetrack.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> getAllByTask(Task task);
}
