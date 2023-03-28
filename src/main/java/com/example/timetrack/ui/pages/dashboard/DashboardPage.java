package com.example.timetrack.ui.pages.dashboard;

import com.example.timetrack.entity.Track;
import com.example.timetrack.entity.User;
import com.example.timetrack.services.TrackService;
import com.example.timetrack.services.UserService;
import com.example.timetrack.ui.RootLayout;
import com.example.timetrack.ui.pages.DefaultPage;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.util.Strings;

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
        List<Track> thisMonthTracks = tracks.stream().filter(date -> YearMonth.from(date.getDate()).equals(YearMonth.now()))
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
    }
}
