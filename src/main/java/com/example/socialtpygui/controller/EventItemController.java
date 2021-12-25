package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.EventCursor;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class EventItemController {
    @FXML
    private Label titleEventLabel, numberOfParticipantsLabel, timeEventLabel, locationEventLabel, descriptionEventLabel;

    @FXML
    private ImageView previousEventImageView, nextEventImageView;

    @FXML
    private CheckBox checkBoxParticipateEvent;

    private EventDTO eventDTO;
    private SuperService service;
    private User loggedUser;


    public void handlerNextEvent(MouseEvent mouseEvent) {
        nextEventImageView.fireEvent(new EventCursor(EventCursor.NEXT_EVENT));
    }

    public void handlerPreviousEvent(MouseEvent mouseEvent) {
        previousEventImageView.fireEvent(new EventCursor(EventCursor.PREVIOUS_EVENT));
    }

    public void load(EventDTO eventDTO) {
        this.eventDTO = eventDTO;
        titleEventLabel.setText(eventDTO.getName());
        numberOfParticipantsLabel.setText(String.valueOf(0));
        timeEventLabel.setText(eventDTO.getDate().toString());
        locationEventLabel.setText(eventDTO.getLocation());
        descriptionEventLabel.setText(eventDTO.getDescription());
        numberOfParticipantsLabel.setText(String.valueOf(service.numberOfParticipantsFromAnEvent(eventDTO.getId())));
        if (service.isUserEnrolledInAnEvent(this.loggedUser.getId(), eventDTO.getId())) checkBoxParticipateEvent.setSelected(true);
    }

    public void setService(SuperService service){this.service = service;}

    public void setLoggedUser(User loggedUser) {this.loggedUser = loggedUser;}
}
