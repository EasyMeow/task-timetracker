package com.example.timetrack.services;

import com.example.timetrack.entity.Track;
import com.example.timetrack.repo.TrackRepository;
import org.springframework.stereotype.Service;

@Service
public class TrackService {

    private final TrackRepository trackRepository;

    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public Track save(Track track) {
        return trackRepository.save(track);
    }
}
