package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domainEvent.EventCursor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class EventItemController {
    @FXML
    Label titleEventLabel, numberOfParticipantsLabel, timeEventLabel, locationEventLabel, descriptionEventLabel;

    @FXML
    ImageView previousEventImageView, nextEventImageView;

    EventDTO eventDTO;


    public void handlerNextEvent(MouseEvent mouseEvent) {
        nextEventImageView.fireEvent(new EventCursor(EventCursor.NEXT_EVENT));
    }

    public void handlerPreviousEvent(MouseEvent mouseEvent) {
        previousEventImageView.fireEvent(new EventCursor(EventCursor.PREVIOUS_EVENT));
    }

    public void setEventDTO(EventDTO eventDTO) {
        this.eventDTO = eventDTO;
        titleEventLabel.setText(eventDTO.getName());
        numberOfParticipantsLabel.setText(String.valueOf(0));
        timeEventLabel.setText(eventDTO.getDate().toString());
        locationEventLabel.setText(eventDTO.getLocation());
        descriptionEventLabel.setText(eventDTO.getDescription());
    }
}
