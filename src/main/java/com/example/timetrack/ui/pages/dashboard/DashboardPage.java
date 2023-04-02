package com.example.timetrack.ui.pages.dashboard;

import com.example.timetrack.entity.Track;
import com.example.timetrack.entity.User;
import com.example.timetrack.services.TrackService;
import com.example.timetrack.services.UserService;
import com.example.timetrack.ui.RootLayout;
import com.example.timetrack.ui.pages.DefaultPage;
import com.example.timetrack.ui.pages.dashboard.charts.LastWeekThisWeekChart;
import com.example.timetrack.ui.pages.dashboard.charts.MonthChart;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.util.Strings;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@PageTitle("Дашборд")
@Route(value = "dashboard", layout = RootLayout.class)
public class DashboardPage extends VerticalLayout implements DefaultPage, HasUrlParameter<String> {
    private User user;
    private final UserService userService;
    private final TrackService trackService;

    private Card firstCard = new Card();
    public DashboardPage(UserService userService, TrackService trackService) {
        this.userService = userService;
        this.trackService = trackService;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if(!Strings.isBlank(parameter)) {
            UUID id = UUID.fromString(parameter);
            user = userService.getById(id);
        } else {
            user = (User) VaadinSession.getCurrent().getAttribute("user");
        }
        initContent();
    }

    private void initContent() {
        firstCard.setSizeFull();
        List<Track> tracks = trackService.getByUser(user);
        Comparator<Track> byDate = Comparator.comparing(Track::getDate);
        tracks.sort(byDate);
        List<Track> thisMonthTracks = tracks.stream().filter(date -> YearMonth.from(date.getDate()).equals(YearMonth.now().minusMonths(1)))
                .collect(Collectors.toList());
        MonthChart monthChart = new MonthChart(thisMonthTracks);
        monthChart.setSizeFull();
        VerticalLayout monthLayout = new VerticalLayout();
        monthLayout.setPadding(true);
        monthLayout.setSizeFull();
        monthLayout.setAlignItems(Alignment.CENTER);
        monthLayout.add(new Label("Количество часов в день за последний месяц"), monthChart);
        firstCard.add(monthLayout);
        add(firstCard);

        LocalDate today = LocalDate.now();

        LocalDate lastWeekStart = today.minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate lastWeekEnd = today.minusWeeks(1).with(DayOfWeek.SUNDAY);

        List<Track> lastWeekTracks = tracks.stream()
                .filter(track -> {
                    LocalDate date = track.getDate();
                    return date.isAfter(lastWeekStart.minusDays(1)) && date.isBefore(lastWeekEnd.plusDays(1));
                }).collect(Collectors.toList());

        LocalDate thisWeekStart = today.with(DayOfWeek.MONDAY);
        LocalDate thisWeekEnd = today.with(DayOfWeek.SUNDAY);

        List<Track> thisWeekTracks = tracks.stream()
                .filter(track -> {
                    LocalDate date = track.getDate();
                    return date.isAfter(thisWeekStart.minusDays(1)) && date.isBefore(thisWeekEnd.plusDays(1));
                })
                .collect(Collectors.toList());

        LastWeekThisWeekChart lastWeekThisWeekChart = new LastWeekThisWeekChart(lastWeekTracks,thisWeekTracks);
        lastWeekThisWeekChart.setSizeFull();
        VerticalLayout lastWeekThisWeekLayout = new VerticalLayout();
        lastWeekThisWeekLayout.setPadding(true);
        lastWeekThisWeekLayout.setSizeFull();
        lastWeekThisWeekLayout.setAlignItems(Alignment.CENTER);
        lastWeekThisWeekLayout.add(new Label("Эффективность по сравнению с прошлой неделей"), lastWeekThisWeekChart);
        firstCard.add(lastWeekThisWeekLayout);
        add(firstCard);
    }
}
