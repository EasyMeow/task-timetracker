package com.example.timetrack.ui.pages.tasks;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.Task;
import com.example.timetrack.entity.User;
import com.example.timetrack.services.TaskService;
import com.example.timetrack.services.TeamService;
import com.example.timetrack.ui.RootLayout;
import com.example.timetrack.ui.components.TaskComponent;
import com.example.timetrack.ui.pages.DefaultPage;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
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
    private final TeamService teamService;
    private final User user = (User) VaadinSession.getCurrent().getAttribute("user");
    private final Project project = (Project) VaadinSession.getCurrent().getAttribute("project");

    private List<Task> tasks = new ArrayList<>();
    VerticalLayout tasksLayout;

    public TasksPage(TaskService taskService,  TeamService teamService) {
        this.taskService = taskService;
        this.teamService = teamService;
    }

    private void initContent() {
        Button createTaskButton = new Button("Новая задача", this::openDialog);

        tasksLayout = new VerticalLayout();
        tasksLayout.setSizeFull();
        tasksLayout.setPadding(true);
        tasksLayout.add(createTaskButton);
        tasks.forEach(task -> {
            TaskComponent taskComponent = new TaskComponent(task);
            taskComponent.setWidthFull();
            tasksLayout.add(taskComponent);
        });

        add(tasksLayout);
    }

    private void openDialog(ClickEvent<Button> event) {
        TaskEditor taskEditor = new TaskEditor(taskService,teamService, user, project);
        taskEditor.setCloseOnEsc(true);
        taskEditor.setCloseOnOutsideClick(true);
        taskEditor.open();
    }

    private void bindData() {
        tasks = taskService.getByProject(project);
        tasks.sort(this::comp);
        initContent();
    }

    private int comp(Task task1, Task task2) {
        if(task1.getCreationDate().isAfter(task2.getCreationDate())) {
            return 1;
        } else if(task1.getCreationDate().isBefore(task2.getCreationDate())){
            return -1;
        }
        return 0;
    }

    @Override
    public void beforeEnterOver(BeforeEnterEvent event) {
        bindData();
    }
}
