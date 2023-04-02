package com.example.timetrack.ui.pages.dashboard.charts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class TrackWrapper {
    private LocalDate date;
    private BigDecimal time;
}
