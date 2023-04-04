package com.example.timetrack.ui.pages.projects;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.Task;
import com.example.timetrack.entity.User;
import com.example.timetrack.enums.Position;
import com.example.timetrack.enums.TaskStatus;
import com.example.timetrack.services.ProjectService;
import com.example.timetrack.services.TaskService;
import com.example.timetrack.services.TeamService;
import com.example.timetrack.services.UserService;
import com.example.timetrack.ui.RootLayout;
import com.example.timetrack.ui.pages.DefaultPage;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

@PageTitle("Проекты")
@Route(value = "projects", layout = RootLayout.class)
public class ProjectsPage extends VerticalLayout implements DefaultPage {

    private final User user = (User) VaadinSession.getCurrent().getAttribute("user");
    private final Project project = (Project) VaadinSession.getCurrent().getAttribute("project");

    private final TeamService teamService;
    private final UserService userService;
    private final ProjectService projectService;
    private final TaskService taskService;

    public ProjectsPage(TeamService teamService, UserService userService, ProjectService projectService, TaskService taskService) {
        this.teamService = teamService;
        this.userService = userService;
        this.projectService = projectService;
        this.taskService = taskService;

        initGrid(projectService);
    }

    private void initGrid(ProjectService projectService) {
        Grid<Project> grid = new Grid<>(Project.class, false);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(Project::getTitle)
                .setHeader("Название проекта");
        grid.addColumn(Project::getCode)
                .setHeader("Код");
        grid.addColumn(pr -> pr.getTeam().getName())
                .setHeader("Название команды");
        grid.addColumn(this::getTasks)
                .setHeader("Количетсво выполненных задач к общему количество").setAutoWidth(true);
        grid.addColumn(pr -> pr.getId().equals(project.getId()) ? "Да" : "Нет")
                .setHeader("Текущий проект");

        List<Project> projects = projectService.findAllByUser(user);
        grid.setItems(projects);
        grid.select(project);
        grid.addSelectionListener(selectionEvent -> {
            if (!selectionEvent.isFromClient()) {
                return;
            }
            selectionEvent.getFirstSelectedItem().ifPresent(project -> {
                if (grid.getSelectedItems().isEmpty()) {
                    grid.select(project);
                } else {
                    VaadinSession.getCurrent().setAttribute("project", project);
                    UI.getCurrent().getPage().reload();
                }
            });
        });
        grid.setHeightByRows(true);

        Button createProjectButton = new Button("Новый проект", event -> forward("newproject"));

        VerticalLayout vl = new VerticalLayout(grid);
        vl.setPadding(true);
        vl.setSizeFull();
        Card card = new Card(vl);
        card.setSizeFull();

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(true);
        if (user.getPosition() == Position.PROJECT_MANAGER) {
            content.add(createProjectButton, card);
        } else {
            content.add(card);
        }
        setSizeFull();
        add(content);
    }

    private String getTasks(Project project) {
        List<Task> tasks = taskService.getByProject(project);
        tasks.stream().filter(task -> task.getStatus() == TaskStatus.DONE).count();
        return tasks.stream().filter(task -> task.getStatus() == TaskStatus.DONE).count() + "/" + tasks.size();
    }

    private void forward(String href) {
        getUI().ifPresent(ui -> ui.navigate(href));
    }
}
