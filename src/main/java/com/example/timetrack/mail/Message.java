package com.example.timetrack.mail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {

    private String to;
    private String from;
    private String password;
    private String port;
    private String smtp;

    private String theme;
    private String body;
}
