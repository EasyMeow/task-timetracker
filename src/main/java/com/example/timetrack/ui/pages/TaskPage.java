package com.example.timetrack.ui.pages;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.Task;
import com.example.timetrack.entity.Team;
import com.example.timetrack.entity.User;
import com.example.timetrack.enums.TaskStatus;
import com.example.timetrack.services.TaskService;
import com.example.timetrack.services.TeamService;
import com.example.timetrack.ui.RootLayout;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;
import java.util.UUID;

@PageTitle("Задача")
@Route(value = "task", layout = RootLayout.class)
public class TaskPage extends VerticalLayout implements DefaultPage, HasUrlParameter<String> {

    private final TaskService taskService;
    private final TeamService teamService;
    private final User user = (User) VaadinSession.getCurrent().getAttribute("user");
    private final Project project = (Project) VaadinSession.getCurrent().getAttribute("project");
    private Task task;

    private TextField titleField = new TextField();
    private TextArea descriptionField = new TextArea();
    private Select<TaskStatus> statusSelect = new Select<>();
    private Select<User> assigneeSelect = new Select<>();
    private TextField reporterField = new TextField();
    private Select<User> reviewerSelect = new Select<>();

    private MessageList messageList = new MessageList();
    private TextArea messageField = new TextArea();

    private Card topCard = new Card();
    private Card leftCard = new Card();
    private Card rightCard = new Card();

    public TaskPage(TaskService taskService, TeamService teamService) {
        this.taskService = taskService;
        this.teamService = teamService;
        initContent();
    }

    private void initContent() {
        initTopLayout();
        initRightLayout();
        initLeftLayout();

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.add(leftCard, rightCard);
        add(topCard, mainLayout);
    }

    private void initLeftLayout() {
        leftCard.setHeightFull();
        leftCard.setWidthFull();

        descriptionField.setLabel("Описание");
        descriptionField.setWidthFull();
        descriptionField.setHeight("160px");

        VerticalLayout messageLayout = new VerticalLayout();
        messageLayout.setHeight("400px");
        messageLayout.getStyle().set("overflow","auto");
        messageLayout.add(messageList);
        messageList.setSizeFull();

        messageField.setLabel("Новое сообщение");
        messageField.setWidthFull();
        messageField.setHeight("160px");

        Button sendButton = new Button(VaadinIcon.COMMENT.create(), this::sendMessage);
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.setJustifyContentMode(JustifyContentMode.END);
        footer.add(sendButton);

        VerticalLayout cardLayout = new VerticalLayout();
        cardLayout.setSizeFull();
        cardLayout.setPadding(true);
        cardLayout.add(descriptionField, messageLayout,messageField, footer);
        leftCard.add(cardLayout);
    }

    private void sendMessage(ClickEvent<Button> event) {

    }

    private void initRightLayout() {
//        rightCard.setHeightFull();
        rightCard.setWidth("250px");

        assigneeSelect.setLabel("Исполнитель");
        assigneeSelect.setWidthFull();
        assigneeSelect.setItemLabelGenerator(user -> user.getName() + " " + user.getSecondName());

        reporterField.setWidthFull();
        reporterField.setEnabled(false);
        reporterField.setLabel("Создатель");

        reviewerSelect.setLabel("Ревьюер");
        reviewerSelect.setWidthFull();
        reviewerSelect.setItemLabelGenerator(user -> user.getName() + " " + user.getSecondName());

        VerticalLayout cardLayout = new VerticalLayout(assigneeSelect, reporterField, reviewerSelect);
        cardLayout.setSizeFull();
        cardLayout.setPadding(true);
        rightCard.add(cardLayout);
    }

    private void initTopLayout() {
        HorizontalLayout topLayout = new HorizontalLayout(titleField, statusSelect);
        topLayout.setPadding(true);
        topLayout.setWidthFull();
        topLayout.setAlignItems(Alignment.BASELINE);

        titleField.setWidthFull();
        statusSelect.setItemLabelGenerator(TaskStatus::getName);

        topCard.setWidthFull();
        topCard.setHeight("15%");
        topCard.add(topLayout);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if (parameter.isBlank()) {
            UI.getCurrent().getPage().getHistory().back();
        } else {
            UUID taskId = UUID.fromString(parameter);
            task = taskService.getById(taskId);

            fillFields();
        }
    }

    private void fillFields() {
        titleField.setValue(task.getTitle());
        statusSelect.setItems(TaskStatus.values());
        statusSelect.setValue(task.getStatus());
        reporterField.setValue(task.getReporter().getName() + " " + task.getReporter().getSecondName());
        descriptionField.setValue(task.getDescription());

        Team team = teamService.getTeamById(project.getTeam().getId());
        List<User> users = team.getDevelopers();
        if (team.getTeamLead() != null) {
            users.add(team.getTeamLead());
        }
        users.add(team.getProjectManager());

        assigneeSelect.setItems(users);
        reviewerSelect.setItems(users);

        reviewerSelect.setValue(task.getReviewer());
        assigneeSelect.setValue(task.getAssignee());
    }
}
