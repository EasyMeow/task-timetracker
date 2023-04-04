package com.example.timetrack.services;

import com.example.timetrack.entity.Team;
import com.example.timetrack.entity.User;
import com.example.timetrack.enums.Position;
import com.example.timetrack.repo.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team  save(Team team) {
        return teamRepository.save(team);
    }

    public List<Team> getAllByPm(User pm) {
        return teamRepository.findTeamsByProjectManager(pm);
    }

    private List<Team> getAllByTm(User tm) {
        return teamRepository.findTeamsByTeamLead(tm);
    }

    public List<Team> getAllByUser(User user) {
        if(user.getPosition()== Position.PROJECT_MANAGER) {
            return getAllByPm(user);
        } else if(user.getPosition() == Position.TEAM_LEAD) {
            return getAllByTm(user);
        } else {
            return teamRepository.findAll().stream().filter(team-> team.getDevelopers().contains(user)).collect(Collectors.toList());
        }
    }

    public Team getTeamById(UUID id) {
        return teamRepository.findById(id).orElse(null);
    }
}
