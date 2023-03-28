package com.example.timetrack.ui.pages.tasks;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.Task;
import com.example.timetrack.entity.User;
import com.example.timetrack.services.ProjectService;
import com.example.timetrack.services.TaskService;
import com.example.timetrack.ui.RootLayout;
import com.example.timetrack.ui.components.TaskComponent;
import com.example.timetrack.ui.pages.DefaultPage;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Задачи")
@Route(value = "tasks", layout = RootLayout.class)
public class TasksPage extends VerticalLayout implements DefaultPage {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final User user = (User) VaadinSession.getCurrent().getAttribute("user");
    private final Project project = (Project) VaadinSession.getCurrent().getAttribute("project");

    private List<Task> tasks = new ArrayList<>();
    VerticalLayout tasksLayout;

    public TasksPage(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    private void initContent() {

        Card card = new Card();
        card.setHeight("15%");
        card.setWidthFull();
        Button createTaskButton = new Button("Новая задача", this::openDialog);
        HorizontalLayout topToolbar = new HorizontalLayout();
        topToolbar.setWidthFull();
        topToolbar.add(new Label("Задачи на проекте " + project.getTitle()), createTaskButton);
        card.add(topToolbar);

        tasksLayout = new VerticalLayout();
        tasksLayout.setSizeFull();
        tasksLayout.setPadding(true);
        tasksLayout.add(card);
        tasks.forEach(task -> {
            TaskComponent taskComponent = new TaskComponent(task);
            taskComponent.setWidthFull();
            tasksLayout.add(taskComponent);
        });

        add(tasksLayout);
    }

    private void openDialog(ClickEvent<Button> event) {
        TaskEditor taskEditor = new TaskEditor(taskService, projectService, user, project);
        taskEditor.setCloseOnEsc(true);
        taskEditor.setCloseOnOutsideClick(true);
        taskEditor.open();
    }

    private void bindData() {
        tasks = project.getTasks();
        initContent();
    }

    @Override
    public void beforeEnterOver(BeforeEnterEvent event) {
        bindData();
    }
}
