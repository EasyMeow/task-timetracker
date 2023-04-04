package com.example.timetrack.ui.components;

import com.example.timetrack.entity.Track;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDate;


public class TrackComponent extends CustomField<Track> {

    private Card card;
    private HorizontalLayout layout;
    private TextField commentField;
    private BigDecimalField timeField;

    private Track track;
    private LocalDate date;

    public TrackComponent() {
        card = new Card();
        card.setWidthFull();
        card.setHeight("15%");

        layout = new HorizontalLayout();
        layout.setWidthFull();

        commentField = new TextField();
        commentField.setWidthFull();
        commentField.setLabel("Комментарий");

        timeField = new BigDecimalField();
        timeField.setWidth("15%");
        timeField.setLabel("Время");

        layout.add(commentField, timeField);
        layout.setPadding(true);

        card.add(layout);

        setWidthFull();
        add(card);
    }

    @Override
    protected Track generateModelValue() {
        track.setComment(commentField.getValue());
        track.setDate(date);
        track.setTime(timeField.getValue());
        return track;
    }

    @Override
    protected void setPresentationValue(Track track) {
        this.track = track;
        commentField.setValue(track.getComment() != null ? track.getComment()  : "");
        timeField.setValue(track.getTime());
        date = track.getDate();
    }
}
