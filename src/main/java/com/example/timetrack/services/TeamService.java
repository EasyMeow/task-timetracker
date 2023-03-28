package com.example.timetrack.services;

import com.example.timetrack.entity.Team;
import com.example.timetrack.entity.User;
import com.example.timetrack.repo.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
