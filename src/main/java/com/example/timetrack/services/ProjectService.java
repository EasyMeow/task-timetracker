package com.example.timetrack.services;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.Team;
import com.example.timetrack.entity.User;
import com.example.timetrack.repo.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamService teamService;

    public ProjectService(ProjectRepository projectRepository, TeamService teamService) {
        this.projectRepository = projectRepository;
        this.teamService = teamService;
    }

    public Project save(Project project) {
       return projectRepository.save(project);
    }

    public boolean hasProjects(User pm) {
        return projectRepository.findAll().stream().anyMatch(pr -> pr.getTeam().getProjectManager().equals(pm));
    }

    public Project findFirstByUser(User user) {
        return projectRepository.findAll().stream()
                .filter(project -> {
                    Team team = teamService.getTeamById(project.getTeam().getId());
                    return team.getProjectManager().equals(user) ||
                            (team.getTeamLead() !=null && team.getTeamLead().equals(user)) ||
                            team.getDevelopers().stream().anyMatch(dev-> dev.equals(user));
                }).collect(Collectors.toList()).get(0);
    }

    public List<Project> findAllByUser(User user) {
        return projectRepository.findAll().stream()
                .filter(project -> {
                    Team team = teamService.getTeamById(project.getTeam().getId());
                    return team.getProjectManager().equals(user) ||
                            (team.getTeamLead() !=null && team.getTeamLead().equals(user)) ||
                            team.getDevelopers().stream().anyMatch(dev-> dev.equals(user));
                }).collect(Collectors.toList());
    }
}
