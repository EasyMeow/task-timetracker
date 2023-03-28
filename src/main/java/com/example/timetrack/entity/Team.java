package com.example.timetrack.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "team")
public class Team {

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @NotEmpty
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "team_lead")
    private User teamLead;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "project_manager")
    private User projectManager;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "developers",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "dev_id")
    )
    private List<User> developers = new ArrayList<>();

}
