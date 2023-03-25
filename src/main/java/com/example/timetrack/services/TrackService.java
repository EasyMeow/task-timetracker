package com.example.timetrack.services;

import com.example.timetrack.repo.TrackRepository;
import org.springframework.stereotype.Service;

@Service
public class TrackService {

    private final TrackRepository trackRepository;

    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }
}
