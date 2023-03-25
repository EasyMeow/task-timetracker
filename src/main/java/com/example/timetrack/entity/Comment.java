package com.example.timetrack.entity;

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
@Table(name = "comment")
public class Comment {

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @NotEmpty
    @Column(name = "text")
    private String text;

    @NotNull
    @Column(name = "date")
    private LocalDate date;
}
