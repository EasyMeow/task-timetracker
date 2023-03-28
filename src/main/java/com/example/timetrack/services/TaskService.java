package com.example.timetrack.services;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.Task;
import com.example.timetrack.repo.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Task getById(UUID taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public List<Task> getByProject(Project project) {
        return taskRepository.findAllByProject(project);
    }
}
