package com.example.timetrack.ui.components;

import com.example.timetrack.entity.Task;
import com.example.timetrack.enums.TaskStatus;
import com.example.timetrack.ui.uitls.Utils;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.html.Span;
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
        topLayout.add(taskTitleAnchor, status);
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.add(assignee, creationDate);
        card.add(topLayout,bottomLayout);
        card.setWidthFull();
        add(card);
    }

    protected void fillData(Task task) {
        taskTitleAnchor.setText(task.getTitle());
        taskTitleAnchor.setTarget(AnchorTarget.BLANK);
        taskTitleAnchor.setHref("task/" + task.getId());
        assignee.setText(task.getAssignee().getName() + " " + task.getAssignee().getSecondName());
        creationDate.setText(task.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        if(task.getStatus() == TaskStatus.NEW) {
            status = Utils.createDefaultBadge(TaskStatus.NEW.getName());
        } else if(task.getStatus() == TaskStatus.IN_PROGRES) {
            status = Utils.createContrastBadge(TaskStatus.IN_PROGRES.getName());
        } else  if(task.getStatus() == TaskStatus.ON_REVIEW) {
            status = Utils.createSuccessBadge(TaskStatus.ON_REVIEW.getName());
        } else if(task.getStatus() == TaskStatus.DONE) {
            status = Utils.createSuccessPrimaryBadge(TaskStatus.DONE.getName());
        }
    }
}
