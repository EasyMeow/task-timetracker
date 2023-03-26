package com.example.timetrack.services;

import com.example.timetrack.entity.User;
import com.example.timetrack.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean checkPassword(String login, String password) {
        User user = userRepository.findByLogin(login);

        if (user == null) {
            return false;
        }

        return user.getPassword().equals(password);
    }

    public User authorize(String login) {
        return userRepository.findByLogin(login);
    }

    public boolean checkLoginExists(String login) {
        return userRepository.findByLogin(login) !=null;
    }

}
