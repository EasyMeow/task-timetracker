package com.example.timetrack.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "project")
public class Project {

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @NotEmpty
    @Column(name = "code")
    private String code;

    @NotNull
    @NotEmpty
    @Column(name = "title")
    private String title;

    @ManyToMany
    @JoinTable(
            name = "project_task",
            joinColumns = @JoinColumn(name = "project_id"), // proj_id
            inverseJoinColumns = @JoinColumn(name = "task_id") // task_id
    )
    private List<Task> tasks;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team")
    private Team team;
}
