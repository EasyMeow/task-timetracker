package com.example.timetrack.ui.pages.tasks;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.Task;
import com.example.timetrack.entity.User;
import com.example.timetrack.enums.TaskStatus;
import com.example.timetrack.services.ProjectService;
import com.example.timetrack.services.TaskService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDate;
import java.util.List;


public class TaskEditor extends Dialog {

    private TextField titleField = new TextField();
    private TextArea descriptionField = new TextArea();
    private Select<User> assigneeField = new Select<>();
    private Select<User> reviewerField = new Select<>();

    private final TaskService taskService;
    private final ProjectService projectService;

    private final User user;
    private final Project project;

    public TaskEditor(TaskService taskService, ProjectService projectService, User user, Project project) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.user = user;
        this.project = project;

        List<User> users = project.getTeam().getDevelopers();
        if (project.getTeam().getTeamLead() != null) {
            users.add(project.getTeam().getTeamLead());
        }
        users.add(project.getTeam().getProjectManager());

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        Label label = new Label("Создание задачи");

        titleField.setWidthFull();
        titleField.setLabel("Название");

        descriptionField.setWidthFull();
        descriptionField.setLabel("Описание");

        assigneeField.setWidthFull();
        assigneeField.setItems(users);
        assigneeField.setItemLabelGenerator(item-> item.getName() + " " + item.getSecondName());
        assigneeField.setLabel("Исполнитель");

        reviewerField.setWidthFull();
        reviewerField.setItems(users);
        reviewerField.setItemLabelGenerator(item-> item.getName() + " " + item.getSecondName());
        reviewerField.setLabel("Ревьюер");

        Button saveButton = new Button("Сохранить", this::save);
        saveButton.setWidthFull();
        layout.add(label, titleField, descriptionField, assigneeField, reviewerField, saveButton);
        add(layout);
        this.setWidth("400px");
        this.setHeight("550px");
    }

    private void save(ClickEvent<Button> event) {
        Task task = new Task();
        task.setTitle(titleField.getValue());
        task.setDescription(descriptionField.getValue());
        task.setAssignee(assigneeField.getValue());
        task.setReviewer(reviewerField.getValue());
        task.setReporter(user);
        task.setStatus(TaskStatus.NEW);
        task.setCreationDate(LocalDate.now());
        taskService.save(task);

        project.getTasks().add(task);

        projectService.save(project);
        close();
        UI.getCurrent().getPage().reload();
    }
}
