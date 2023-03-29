package com.example.timetrack.ui.components;

import com.example.timetrack.entity.Task;
import com.example.timetrack.enums.TaskStatus;
import com.example.timetrack.ui.uitls.Utils;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.vaadin.addons.badge.Badge;

import java.time.format.DateTimeFormatter;

public class TaskComponent extends HorizontalLayout {

    private final Card card = new Card();
    private final Anchor taskTitleAnchor = new Anchor();

    private Badge status;
    private Span assignee = new Span();
    private Span creationDate = new Span();

    public TaskComponent(Task task) {
        fillData(task);
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setPadding(true);
        topLayout.setAlignItems(Alignment.BASELINE);
        Div spacer = new Div();
        spacer.setWidth("30px");
        Div spacer2 = new Div();
        spacer2.setWidth("30px");
        Div spacer3 = new Div();
        spacer3.setWidth("50px");
        topLayout.add(taskTitleAnchor, spacer, new Label("Исполнитель:"), assignee, spacer2, new Label("Дата создания:"), creationDate, spacer3, new Label("Статус задачи:"), status);
        card.add(topLayout);
        card.setWidthFull();
        add(card);
    }

    protected void fillData(Task task) {
        taskTitleAnchor.setText(task.getTitle());
        taskTitleAnchor.setTarget(AnchorTarget.BLANK);
        taskTitleAnchor.setHref("task/" + task.getId());
        assignee.setText(task.getAssignee().getName() + " " + task.getAssignee().getSecondName());
        creationDate.setText(task.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        if (task.getStatus() == TaskStatus.NEW) {
            status = Utils.createDefaultBadge(TaskStatus.NEW.getName());
        } else if (task.getStatus() == TaskStatus.IN_PROGRES) {
            status = Utils.createContrastBadge(TaskStatus.IN_PROGRES.getName());
        } else if (task.getStatus() == TaskStatus.ON_REVIEW) {
            status = Utils.createSuccessBadge(TaskStatus.ON_REVIEW.getName());
        } else if (task.getStatus() == TaskStatus.DONE) {
            status = Utils.createSuccessPrimaryBadge(TaskStatus.DONE.getName());
        }
    }
}
