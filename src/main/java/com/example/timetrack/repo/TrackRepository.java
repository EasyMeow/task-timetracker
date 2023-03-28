package com.example.timetrack.repo;

import com.example.timetrack.entity.Task;
import com.example.timetrack.entity.Track;
import com.example.timetrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrackRepository extends JpaRepository<Track, UUID> {

    List<Track> getAllByTask(Task task);

    List<Track> getAllByUser(User user);
}
