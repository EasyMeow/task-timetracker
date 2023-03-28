package com.example.timetrack.services;

import com.example.timetrack.entity.Comment;
import com.example.timetrack.entity.Task;
import com.example.timetrack.repo.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getByTask(Task task) {
        return commentRepository.getAllByTask(task);
    }
}
