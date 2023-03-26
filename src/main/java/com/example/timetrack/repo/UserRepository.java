package com.example.timetrack.repo;

import com.example.timetrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByLogin(@NotNull @NotEmpty String login);
}
