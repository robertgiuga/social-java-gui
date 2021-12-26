package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreateEventWindowController {
    @FXML
    private TextField descriptionEventTextField, locationEventTextField, nameEventTextField;
    @FXML
    private DatePicker dateEventTextField;

    private SuperService service;
    private User loggedUser;

    /**
     * Handler for add button, if one text field is empty throw alert, if the date from date picker is before LocalDate.now()
     * throw alert, else add event and clear all text fields and data picker.
     * @param mouseEvent MouseEvent
     */
    public void handlerAddEventBtn(MouseEvent mouseEvent) {
        if ((descriptionEventTextField.getText().length() == 0) || (locationEventTextField.getText().length() == 0) || (dateEventTextField.getValue() == null) || nameEventTextField.getText().length() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Text fields can not be empty!!! Try again.");
            alert.show();
        }
        else if (dateEventTextField.getValue().isBefore(LocalDate.now())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Date is invalid!!! Try again.");
            alert.show();
        }
        else{
            List<UserDTO> list = new ArrayList<>(); list.add(new UserDTO(loggedUser));
            service.saveEvent(new EventDTO(descriptionEventTextField.getText(), dateEventTextField.getValue(), locationEventTextField.getText(), list, nameEventTextField.getText(), loggedUser.getId()));
            descriptionEventTextField.clear(); locationEventTextField.clear(); nameEventTextField.clear(); dateEventTextField.getEditor().clear();
        }
    }

    /**
     * Set service
     * @param service SuperService
     */
    public void setService(SuperService service) {
        this.service = service;
    }

    /**
     * Set logged user
     * @param loggedUser User
     */
    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
}
