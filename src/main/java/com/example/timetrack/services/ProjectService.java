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

    public Project save(Project project) {
       return projectRepository.save(project);
    }

    public boolean hasProjects(User pm) {
        return projectRepository.findAll().stream().anyMatch(pr -> pr.getTeam().getProjectManager().equals(pm));
    }

    public Project findByUser(User user) {
        return projectRepository.findAll().stream()
                .filter(project -> project.getTeam().getProjectManager().equals(user) ||
                        (project.getTeam().getTeamLead() !=null && project.getTeam().getTeamLead().equals(user)) ||
                project.getTeam().getDevelopers().stream().anyMatch(dev-> dev.equals(user))).findFirst().orElse(null);
    }
}
