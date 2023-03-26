package com.example.timetrack.repo;

import com.example.timetrack.entity.Team;
import com.example.timetrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {

    List<Team> findTeamsByProjectManager(User pm);
}
