package com.example.timetrack.repo;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findAllByProject(Project project);
}
