package com.example.timetrack.ui.pages.team;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.Team;
import com.example.timetrack.entity.User;
import com.example.timetrack.enums.Position;
import com.example.timetrack.mail.EmailSender;
import com.example.timetrack.services.TeamService;
import com.example.timetrack.services.UserService;
import com.example.timetrack.ui.RootLayout;
import com.example.timetrack.ui.pages.DefaultPage;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Команда")
@Route(value = "team", layout = RootLayout.class)
public class TeamPage extends VerticalLayout implements DefaultPage {

    private final TeamService teamService;
    private final UserService userService;
    private final EmailSender emailSender;
    private final User user = (User) VaadinSession.getCurrent().getAttribute("user");
    private final Project project = (Project) VaadinSession.getCurrent().getAttribute("project");

    private Select<Team> teamSelect = new Select<>();

    private Card pmCard = new Card();
    private Card tmCard = new Card();
    private Card devCard = new Card();

    private VerticalLayout mainLayout = new VerticalLayout();

    public TeamPage(TeamService teamService, UserService userService, EmailSender emailSender) {
        this.teamService = teamService;
        this.userService = userService;
        this.emailSender = emailSender;

        Card card = new Card();
        card.setWidthFull();
        teamSelect.setWidthFull();
        teamSelect.setLabel("Команда");
        teamSelect.setItemLabelGenerator(Team::getName);
        teamSelect.setItems(this.teamService.getAllByPm(user));
        teamSelect.addValueChangeListener(event -> loadData());
        Button inviteButton = new Button("Добавить в команду", this::openDialog);
        inviteButton.setWidth("250px");
        HorizontalLayout hl = new HorizontalLayout(teamSelect, inviteButton);
        hl.setAlignItems(Alignment.BASELINE);
        hl.setWidthFull();
        hl.setPadding(true);
        card.add(hl);

        mainLayout.setSizeFull();

        add(card, mainLayout);
    }

    private void openDialog(ClickEvent<Button> event) {
        InviteDialog inviteDialog = new InviteDialog(teamService, emailSender,user,teamSelect.getValue(),project);
        inviteDialog.open();
    }

    private void loadData() {
        mainLayout.removeAll();
        Team team = teamSelect.getValue();
        User pm = team.getProjectManager();

        pmCard.setWidthFull();
        HorizontalLayout pmLayuot = new HorizontalLayout();
        pmLayuot.setPadding(true);
        pmLayuot.setWidthFull();
        pmLayuot.setAlignItems(Alignment.BASELINE);
        pmLayuot.add(buildUserComponent(pm));
        pmCard.add(pmLayuot);

        User tm = team.getTeamLead();

        tmCard.setWidthFull();
        HorizontalLayout tmLayuot = new HorizontalLayout();
        tmLayuot.setPadding(true);
        tmLayuot.setWidthFull();
        tmLayuot.setAlignItems(Alignment.BASELINE);
        tmLayuot.add(buildUserComponent(tm));
        tmCard.add(tmLayuot);

        List<User> devs = team.getDevelopers();

        devCard.setSizeFull();

        ListBox<User> developers = new ListBox<>();
        developers.setSizeFull();
        developers.setItems(devs);
        developers.setRenderer(new ComponentRenderer<>(this::buildUserComponent));
        VerticalLayout vl = new VerticalLayout(new Label("Разработчики"), developers);
        vl.setSizeFull();
        vl.setPadding(true);
        devCard.add(vl);

        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidthFull();
        hl.add(pmCard, tmCard);
        mainLayout.add(hl, devCard);
    }

    private HorizontalLayout buildUserComponent(User user) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.CENTER);

        Avatar avatar = new Avatar();
        avatar.setName(user.getName() +" " + user.getSecondName());
        avatar.setImage("https://img.freepik.com/premium-vector/avatar-portrait-of-a-young-caucasian-boy-man-in-round-frame-vector-cartoon-flat-illustration_551425-19.jpg");

        Span name = new Span(user.getName() +" " + user.getSecondName());
        Span profession = new Span(user.getPosition() == Position.DEVELOPER ? user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) :
                user.getPosition().getName() + " "+ user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        profession.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "var(--lumo-font-size-s)");

        VerticalLayout column = new VerticalLayout(name, profession);
        column.setPadding(false);
        column.setSpacing(false);

        row.add(avatar, column);
        row.getStyle().set("line-height", "var(--lumo-line-height-m)");
        return row;
    }
}
