package com.example.timetrack.services;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.User;
import com.example.timetrack.repo.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void save(Project project) {
        projectRepository.save(project);
    }

    public boolean hasProjects(User pm) {
        return projectRepository.findAll().stream().anyMatch(pr -> pr.getTeam().getProjectManager().equals(pm));
    }
}
