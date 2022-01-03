package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.EventCursor;
import com.example.socialtpygui.domainEvent.LoadView;
import com.example.socialtpygui.service.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    @FXML
    private ImageView removeEventBtn;

    private EventDTO eventDTO;
    private SuperService service;
    private User loggedUser;

    /**
     * Handler for next event, fire EventCursor for load next event.
     * @param mouseEvent mouseEvent
     */
    public void handlerNextEvent(MouseEvent mouseEvent) {
        nextEventImageView.fireEvent(new EventCursor(EventCursor.NEXT_EVENT));
    }

    /**
     * Handler for previous event, fire EventCursor for load previous event.
     * @param mouseEvent mouseEvent
     */
    public void handlerPreviousEvent(MouseEvent mouseEvent) {
        previousEventImageView.fireEvent(new EventCursor(EventCursor.PREVIOUS_EVENT));
    }

    /**
     * load a eventItem, set all labels and check the participation check box if the user is enrolled and
     * check notification and one of the radioButtons if the user want to be notified
     * @param eventDTO EventDTO
     */
    public void load(EventDTO eventDTO) {
        this.eventDTO = eventDTO;
        titleEventLabel.setText(eventDTO.getName());
        numberOfParticipantsLabel.setText(String.valueOf(0));
        timeEventLabel.setText(eventDTO.getDate().toString()+ "\n"+ eventDTO.getTime());
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
        if (eventDTO.getCreator().equals(loggedUser.getId()))
        {
            removeEventBtn.setVisible(true);
        }
    }

    /**
     * Set service
     * @param service SuperService
     */
    public void setService(SuperService service){this.service = service;}

    /**
     * Set logged user.
     * @param loggedUser User
     */
    public void setLoggedUser(User loggedUser) {this.loggedUser = loggedUser;}

    /**
     * handler for participation check box, if the checkBox is selected and the notification checkBox is selected, add
     * participant with a notification time(1,5, 60, 1440 min), if the notification checkBox is not selected add event without
     * notification time, if the participation check box is not selected, remove the participants, if was in the past enrolled
     * @param actionEvent ActionEvent
     */
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

    /**
     * Handler for notification checkBox, if the notification checkBox is not selected disable all radioBtn and updateNotificationEvent, else
     * setDisable(false) to all radioBtn
     * @param actionEvent ActionEvent
     */
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

    /**
     * Handler for 1minRadioBtn, update notification time with 1 (min)
     * @param actionEvent ActionEvent
     */
    public void handler1minRadioBtn(ActionEvent actionEvent) {
        service.updateNotificationEvent(eventDTO.getId(), loggedUser.getId(), "1");
    }

    /**
     *  Handler for 5minRadioBtn, update notification time with 5 (min)
     * @param actionEvent ActionEvent
     */
    public void handler5minRadioBtn(ActionEvent actionEvent) {
        service.updateNotificationEvent(eventDTO.getId(), loggedUser.getId(), "5");
    }

    /**
     *  Handler for 1hourRadioBtn, update notification time with 60 (min)
     * @param actionEvent ActionEvent
     */
    public void handler1hourRadioBtn(ActionEvent actionEvent) {
        service.updateNotificationEvent(eventDTO.getId(), loggedUser.getId(), "60");
    }

    /**
     *  Handler for 1dayRadioBtn, update notification time with 1440 (min)
     * @param actionEvent ActionEvent
     */
    public void handler1dayRadioBtn(ActionEvent actionEvent) {
        service.updateNotificationEvent(eventDTO.getId(), loggedUser.getId(), "1440");
    }

    /**
     * Clear all radioBtn and disable all radioBtn.
     */
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

    /**
     * Handler for removeBtn, remove an event and fire an event for load again all events.
     */
    public void handlerRemoverEvent(MouseEvent mouseEvent) {
        service.removeEvent(eventDTO.getId());
        removeEventBtn.fireEvent(new LoadView(LoadView.LOAD_EVENTS));
    }
}
