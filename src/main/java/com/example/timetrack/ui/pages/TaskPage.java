package com.example.timetrack.ui.pages;

import com.example.timetrack.entity.*;
import com.example.timetrack.enums.TaskStatus;
import com.example.timetrack.services.CommentService;
import com.example.timetrack.services.TaskService;
import com.example.timetrack.services.TeamService;
import com.example.timetrack.ui.RootLayout;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@PageTitle("Задача")
@Route(value = "task", layout = RootLayout.class)
public class TaskPage extends VerticalLayout implements DefaultPage, HasUrlParameter<String> {

    private final TaskService taskService;
    private final TeamService teamService;
    private final CommentService commentService;
    private final User user = (User) VaadinSession.getCurrent().getAttribute("user");
    private final Project project = (Project) VaadinSession.getCurrent().getAttribute("project");
    List<MessageListItem> messages = new ArrayList<>();
    private Task task;

    private TextField titleField = new TextField();
    private TextArea descriptionField = new TextArea();
    private Select<TaskStatus> statusSelect = new Select<>();
    private Select<User> assigneeSelect = new Select<>();
    private TextField reporterField = new TextField();
    private Select<User> reviewerSelect = new Select<>();
    private DatePicker dateField = new DatePicker();
    private DatePicker doneDateField = new DatePicker();

    private MessageList messageList = new MessageList();
    private TextArea messageField = new TextArea();

    private Card topCard = new Card();
    private Card leftCard = new Card();
    private Card rightCard = new Card();

    public TaskPage(TaskService taskService, TeamService teamService, CommentService commentService) {
        this.taskService = taskService;
        this.teamService = teamService;
        this.commentService = commentService;
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
        messageLayout.getStyle().set("overflow", "auto");
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
        cardLayout.add(descriptionField, messageLayout, messageField, footer);
        leftCard.add(cardLayout);
    }

    private void sendMessage(ClickEvent<Button> event) {
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setDate(LocalDate.now());
        comment.setUser(user);
        comment.setText(messageField.getValue());

        commentService.save(comment);

        MessageListItem message = new MessageListItem(
                comment.getText(),
                comment.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant(),
                comment.getUser().getName() + " " + comment.getUser().getSecondName());
        messages.add(message);
        messageList.setItems(messages);
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

        dateField.setLabel("Дата создания");
        dateField.setEnabled(false);
        dateField.setWidthFull();

        doneDateField.setLabel("Дата завершения задачи");
        doneDateField.setWidthFull();

        VerticalLayout cardLayout = new VerticalLayout(assigneeSelect, reporterField, reviewerSelect, dateField, doneDateField);
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
            initChangeListeners();
        }
    }

    private void initChangeListeners() {
        titleField.addValueChangeListener(event -> save());
        statusSelect.addValueChangeListener(event -> save());
        descriptionField.addValueChangeListener(event -> save());
        assigneeSelect.addValueChangeListener(event -> save());
        reviewerSelect.addValueChangeListener(event -> save());
        doneDateField.addValueChangeListener(event -> save());
    }

    private void fillFields() {
        titleField.setValue(task.getTitle());
        statusSelect.setItems(TaskStatus.values());
        statusSelect.setValue(task.getStatus());
        reporterField.setValue(task.getReporter().getName() + " " + task.getReporter().getSecondName());
        descriptionField.setValue(task.getDescription());
        dateField.setValue(task.getCreationDate());
        doneDateField.setValue(task.getDoneDate());

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

        fillMessageList();
    }

    private void fillMessageList() {
        commentService.getByTask(task).forEach(comment -> {
            MessageListItem message = new MessageListItem(
                    comment.getText(),
                    comment.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant(),
                    comment.getUser().getName() + " " + comment.getUser().getSecondName());
            messages.add(message);
        });
        messageList.setItems(messages);
    }

    private void save() {
        task.setTitle(titleField.getValue());
        task.setStatus(statusSelect.getValue());
        task.setAssignee(assigneeSelect.getValue());
        task.setReviewer(reviewerSelect.getValue());
        task.setDescription(descriptionField.getValue());
        task.setDoneDate(doneDateField.getValue());

        taskService.save(task);
    }
}
