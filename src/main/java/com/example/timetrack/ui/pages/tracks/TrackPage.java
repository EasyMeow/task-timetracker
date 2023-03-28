package com.example.timetrack.ui.pages.tracks;

import com.example.timetrack.entity.Project;
import com.example.timetrack.entity.Task;
import com.example.timetrack.entity.Track;
import com.example.timetrack.entity.User;
import com.example.timetrack.services.TaskService;
import com.example.timetrack.services.TrackService;
import com.example.timetrack.ui.RootLayout;
import com.example.timetrack.ui.components.TrackComponent;
import com.example.timetrack.ui.pages.DefaultPage;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Трекинг")
@Route(value = "track", layout = RootLayout.class)
public class TrackPage extends VerticalLayout implements DefaultPage {

    private final TrackService trackService;
    private final TaskService taskService;

    private Card card = new Card();
    private DatePicker datePicker;
    private Select<Task> taskSelectField;
    private Button addButton;

    private VerticalLayout tasksLayout = new VerticalLayout();
    private List<TrackComponent> trackComponents = new ArrayList<>();

    private final User user = (User) VaadinSession.getCurrent().getAttribute("user");
    private final Project project = (Project) VaadinSession.getCurrent().getAttribute("project");

    public TrackPage(TrackService trackService, TaskService taskService) {
        this.trackService = trackService;
        this.taskService = taskService;

        initContent();
    }

    private void initContent() {
        card.setHeight("15%");
        card.setWidthFull();

        taskSelectField = new Select<>();
        taskSelectField.setWidthFull();
        taskSelectField.setLabel("Задача");
        taskSelectField.setItems(taskService.getByProject(project));
        taskSelectField.setItemLabelGenerator(Task::getTitle);

        datePicker = new DatePicker();
        datePicker.setLabel("Дата");

        addButton = new Button(VaadinIcon.PLUS.create(), this::addNewTrack);
        addButton.setEnabled(false);

        taskSelectField.addValueChangeListener(event -> loadTasks());
        datePicker.addValueChangeListener(event -> loadTasks());

        HorizontalLayout cardLayout = new HorizontalLayout();
        cardLayout.setWidthFull();
        cardLayout.setPadding(true);
        cardLayout.getStyle().set("align-items","baseline");
        cardLayout.add(taskSelectField, datePicker, addButton);

        card.add(cardLayout);
        tasksLayout.setWidthFull();
        tasksLayout.setHeight("600px");
        tasksLayout.getStyle().set("overflow","auto");

        Button saveButton =  new Button("Сохранить", this::saveAll);
        HorizontalLayout footer = new HorizontalLayout(saveButton);
        footer.setJustifyContentMode(JustifyContentMode.END);
        footer.setWidthFull();
        add(card, tasksLayout, footer);
    }

    private void saveAll(ClickEvent<Button> event) {
        Task task = taskSelectField.getValue();
        trackComponents.forEach(trackComponent -> {
            Track track = trackComponent.getValue();
            track.setTask(task);
            track.setUser(user);
            trackService.save(track);
        });
    }

    private void addNewTrack(ClickEvent<Button> event) {
        Track track = new Track();
        track.setDate(datePicker.getValue());

        TrackComponent trackComponent = new TrackComponent();
        trackComponent.setValue(track);
        trackComponents.add(trackComponent);
        tasksLayout.add(trackComponent);
    }

    private void loadTasks() {
        if (!taskSelectField.isEmpty() && !datePicker.isEmpty()) {
            tasksLayout.removeAll();
            addButton.setEnabled(true);
            List<Track> tracks = trackService.getByTask(taskSelectField.getValue());;
            tracks.forEach(track -> {
                if(track.getDate().equals(datePicker.getValue())) {
                    TrackComponent trackComponent = new TrackComponent();
                    trackComponent.setValue(track);
                    trackComponents.add(trackComponent);
                    tasksLayout.add(trackComponent);
                }
            });
        } else {
            trackComponents.clear();
            addButton.setEnabled(false);
            tasksLayout.removeAll();
        }
    }
}
