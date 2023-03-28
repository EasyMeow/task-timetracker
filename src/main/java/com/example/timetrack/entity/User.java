package com.example.timetrack.entity;

import com.example.timetrack.enums.Position;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotNull
    @NotEmpty
    @Column(name = "second_name")
    private String secondName;

    @NotNull
    @Column(name = "birth_date")
    private LocalDate dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;

    @NotNull
    @NotEmpty
    @Column(name = "login")
    private String login;

    @NotNull
    @NotEmpty
    @Column(name = "password")
    private String password;

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof User) {
            return ((User) obj).getId().equals(getId());
        }

        return false;
    }
}
