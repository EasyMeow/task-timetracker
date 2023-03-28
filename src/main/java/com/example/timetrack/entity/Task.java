package com.example.timetrack.entity;

import com.example.timetrack.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "task")
public class Task {

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "number")
    private Integer number;

    @NotNull
    @NotEmpty
    @Column(name = "title")
    private String title;

    @NotNull
    @NotEmpty
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "creationDate")
    private LocalDate creationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "assignee")
    private User assignee;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "reporter")
    private User reporter;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "reviewer")
    private User reviewer;

    @ManyToMany
    @JoinTable(
            name = "task_comment",
            joinColumns = @JoinColumn(name = "task_id"), // proj_id
            inverseJoinColumns = @JoinColumn(name = "comment_id") // task_id
    )
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Track> tracks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;
}
