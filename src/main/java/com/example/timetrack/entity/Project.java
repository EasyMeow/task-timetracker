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

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team")
    private Team team;
}
