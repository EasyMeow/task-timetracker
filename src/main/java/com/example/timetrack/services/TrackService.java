package com.example.timetrack.services;

import com.example.timetrack.entity.Task;
import com.example.timetrack.entity.Track;
import com.example.timetrack.entity.User;
import com.example.timetrack.repo.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    private final TrackRepository trackRepository;

    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public Track save(Track track) {
        return trackRepository.save(track);
    }

    public List<Track> saveAll(List<Track> tracks) {
        return trackRepository.saveAll(tracks);
    }

    public List<Track> getByTask(Task task) {
        return trackRepository.getAllByTask(task);
    }

    public List<Track> getByUser(User user) {
        return trackRepository.getAllByUser(user);
    }
}
