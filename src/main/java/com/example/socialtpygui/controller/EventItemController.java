package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.EventCursor;
import com.example.socialtpygui.service.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class EventItemController {
    @FXML
    private Label titleEventLabel, numberOfParticipantsLabel, timeEventLabel, locationEventLabel, descriptionEventLabel;

    @FXML
    private ImageView previousEventImageView, nextEventImageView;

    @FXML
    private CheckBox checkBoxParticipateEvent, notificationCheckBox;

    @FXML
    private RadioButton ratioBtn1min, ratioBtn5min, ratioBtn1hour, ratioBtn1day;

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
        String time = service.timeNotifiedFromEvent(this.loggedUser.getId(), eventDTO.getId());
        if (time != null) {
            notificationCheckBox.setSelected(true);
            switch (time) {
                case "1" -> ratioBtn1min.setSelected(true);
                case "5" -> ratioBtn5min.setSelected(true);
                case "60" -> ratioBtn1hour.setSelected(true);
                case "1440" -> ratioBtn1day.setSelected(true);
            }
        }
        else {
            disableAllRadioButton();
        }


    }

    public void setService(SuperService service){this.service = service;}

    public void setLoggedUser(User loggedUser) {this.loggedUser = loggedUser;}

    public void handlerCheckBoxParticipateEvent(ActionEvent actionEvent) {
        if (checkBoxParticipateEvent.isSelected()){
            if (! notificationCheckBox.isSelected())
                service.addParticipants(loggedUser, eventDTO.getId(), null);
            else if (notificationCheckBox.isSelected()){
                if (ratioBtn1day.isSelected())  service.addParticipants(loggedUser, eventDTO.getId(), "1440");
                else if (ratioBtn1min.isSelected())  service.addParticipants(loggedUser, eventDTO.getId(), "1");
                else if (ratioBtn1hour.isSelected())  service.addParticipants(loggedUser, eventDTO.getId(), "60");
                else if (ratioBtn5min.isSelected())  service.addParticipants(loggedUser, eventDTO.getId(), "5");
            }
        }
        else if (! checkBoxParticipateEvent.isSelected()){
            disableAllRadioButton();
            notificationCheckBox.setSelected(false);
            service.removeParticipants(loggedUser.getId(), eventDTO.getId());
        }
    }

    public void handlerNotificationCheckBox(ActionEvent actionEvent) {
        if (notificationCheckBox.isSelected())
        {
            ratioBtn1day.setDisable(false);
            ratioBtn1hour.setDisable(false);
            ratioBtn1min.setDisable(false);
            ratioBtn5min.setDisable(false);
        }
        else if (! notificationCheckBox.isSelected()){
            disableAllRadioButton();
            service.updateNotificationEvent(eventDTO.getId(), loggedUser.getId(), null);
        }
    }

    public void handler1minRadioBtn(ActionEvent actionEvent) {
        service.updateNotificationEvent(eventDTO.getId(), loggedUser.getId(), "1");
    }

    public void handler5minRadioBtn(ActionEvent actionEvent) {
        service.updateNotificationEvent(eventDTO.getId(), loggedUser.getId(), "5");
    }

    public void handler1hourRadioBtn(ActionEvent actionEvent) {
        service.updateNotificationEvent(eventDTO.getId(), loggedUser.getId(), "60");
    }

    public void handler1dayRadioBtn(ActionEvent actionEvent) {
        service.updateNotificationEvent(eventDTO.getId(), loggedUser.getId(), "1440");
    }

    private void disableAllRadioButton()
    {
        ratioBtn1day.setSelected(false);
        ratioBtn5min.setSelected(false);
        ratioBtn1min.setSelected(false);
        ratioBtn1hour.setSelected(false);
        ratioBtn1day.setDisable(true);
        ratioBtn1hour.setDisable(true);
        ratioBtn1min.setDisable(true);
        ratioBtn5min.setDisable(true);
    }
}
