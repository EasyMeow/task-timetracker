package com.example.timetrack.ui.components;

import com.example.timetrack.entity.Task;
import com.example.timetrack.enums.TaskStatus;
import com.example.timetrack.ui.uitls.Utils;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import org.vaadin.addons.badge.Badge;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskComponent extends HorizontalLayout {

    private final Card card = new Card();
    private final Anchor taskTitleAnchor = new Anchor();

    private Badge status;
    private Span assignee = new Span();
    private Span creationDate = new Span();
    private Span doneDate = new Span();
    private ProgressBar progressBar = new ProgressBar();

    public TaskComponent(Task task) {
        fillData(task);
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthFull();
        topLayout.setPadding(true);
        topLayout.setAlignItems(Alignment.BASELINE);
        Div spacer = new Div();
        spacer.setWidth("30px");
        Div spacer2 = new Div();
        spacer2.setWidth("30px");
        Div spacer3 = new Div();
        spacer3.setWidth("50px");
        taskTitleAnchor.setWidth("150px");
        assignee.setWidth("150px");
        creationDate.setWidth("50px");
        topLayout.add(taskTitleAnchor, spacer, new Label("Исполнитель:"), assignee, spacer2, new Label("Дата создания:"), creationDate, spacer3, new Label("Статус задачи:"), status);

        progressBar.setWidthFull();

        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidthFull();
        bottomLayout.setPadding(true);
        bottomLayout.setAlignItems(Alignment.BASELINE);

        bottomLayout.add(progressBar, doneDate);

        card.add(topLayout, bottomLayout);
        card.setWidthFull();
        add(card);
    }

    protected void fillData(Task task) {
        taskTitleAnchor.setText(task.getTitle());
        taskTitleAnchor.setTarget(AnchorTarget.BLANK);
        taskTitleAnchor.setHref("task/" + task.getId());
        assignee.setText(task.getAssignee().getName() + " " + task.getAssignee().getSecondName());
        creationDate.setText(task.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        doneDate.setText(task.getDoneDate().format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        if (task.getStatus() == TaskStatus.NEW) {
            status = Utils.createDefaultBadge(TaskStatus.NEW.getName());
        } else if (task.getStatus() == TaskStatus.IN_PROGRES) {
            status = Utils.createContrastBadge(TaskStatus.IN_PROGRES.getName());
        } else if (task.getStatus() == TaskStatus.ON_REVIEW) {
            status = Utils.createSuccessBadge(TaskStatus.ON_REVIEW.getName());
        } else if (task.getStatus() == TaskStatus.DONE) {
            status = Utils.createSuccessPrimaryBadge(TaskStatus.DONE.getName());
        }

        calculateProgress(task.getCreationDate(), task.getDoneDate(), task.getStatus());
    }

    private void calculateProgress(LocalDate creationDate, LocalDate doneDate, TaskStatus status) {
        if (status == TaskStatus.DONE) {
            progressBar.setValue(1);
            progressBar.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);
        } else {
            int today = LocalDate.now().getDayOfYear();
            int shouldBeDoneDay = doneDate.getDayOfYear();
            int createDay = creationDate.getDayOfYear();
            if (LocalDate.now().isAfter(doneDate) || today == shouldBeDoneDay) {
                progressBar.setValue(1);
                progressBar.addThemeVariants(ProgressBarVariant.LUMO_ERROR);
            } else {
                double duration = shouldBeDoneDay - createDay;
                double daysLeft = shouldBeDoneDay - today;
                double percent = daysLeft / duration;
                if (percent > 0.6) {
                    progressBar.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);
                } else if (percent > 0.3) {
                    progressBar.addThemeVariants(ProgressBarVariant.LUMO_CONTRAST);
                } else {
                    progressBar.addThemeVariants(ProgressBarVariant.LUMO_ERROR);
                }
                progressBar.setValue(percent == 1 ? 0.05 : Math.abs(percent - 1));
            }
        }

    }
}
