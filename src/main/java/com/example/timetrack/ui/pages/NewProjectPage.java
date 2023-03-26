package com.example.timetrack.ui.pages;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.Team;
import com.example.timetrack.entity.User;
import com.example.timetrack.services.ProjectService;
import com.example.timetrack.services.TeamService;
import com.example.timetrack.ui.RootLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.util.Strings;


@PageTitle("Новый проект")
@Route(value = "newproject", layout = RootLayout.class)
public class NewProjectPage extends VerticalLayout implements DefaultPage {

    private final ProjectService projectService;
    private final TeamService teamService;
    private final User pm = (User) VaadinSession.getCurrent().getAttribute("user");

    private final Dialog dialog = new Dialog();
    private TextField titleField;
    private TextField codeField;
    private Select<Team> teamField;

    public NewProjectPage(ProjectService projectService, TeamService teamService) {
        this.projectService = projectService;
        this.teamService = teamService;

        initContent();
    }

    private void initContent() {
        titleField = new TextField();
        titleField.setWidthFull();
        titleField.setLabel("Название проекта");

        codeField = new TextField();
        codeField.setWidthFull();
        codeField.setLabel("Код проекта");

        teamField = new Select<>();
        teamField.setWidthFull();
        teamField.setLabel("Команда");
        teamField.setEmptySelectionAllowed(false);
        teamField.setItemLabelGenerator(Team::getName);
        teamField.setItems(teamService.getAllByPm(pm));

        Label label = new Label("Создание проекта");

        Button saveButton = new Button("Создать", this::create);
        saveButton.setWidthFull();

        VerticalLayout dialogLayout = new VerticalLayout(label, titleField, codeField, teamField, saveButton);

        if(!projectService.hasProjects(pm)) {
            dialog.setCloseOnOutsideClick(false);
            dialog.setCloseOnEsc(false);
        }
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void create(ClickEvent<Button> event) {
        if (Strings.isBlank(titleField.getValue())) {
            titleField.setInvalid(true);
            titleField.setErrorMessage("Название проекта не может быть пустым");
        } else if (Strings.isBlank(codeField.getValue())) {
            codeField.setInvalid(true);
            codeField.setErrorMessage("Код проекта не может быть пустым");
        } else if (teamField.getValue() == null) {
            teamField.setInvalid(true);
            teamField.setErrorMessage("Команда не может быть пустой");
        } else {
            Project project = new Project();
            project.setTitle(titleField.getValue());
            project.setCode(codeField.getValue());
            project.setTeam(teamField.getValue());
            projectService.save(project);

            dialog.close();
            forward("tasks");
        }
    }

    private void forward(String href) {
        getUI().ifPresent(ui -> ui.navigate(href));
    }
}
