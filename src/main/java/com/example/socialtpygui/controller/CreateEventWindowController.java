package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreateEventWindowController {
    public Spinner<Integer> hourSpiner;
    public Spinner<Integer> minSpiner;
    @FXML
    private TextField descriptionEventTextField, locationEventTextField, nameEventTextField;
    @FXML
    private DatePicker dateEventTextField;

    private SuperService service;
    private UserDTO loggedUser;

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
            Time time = new Time(hourSpiner.getValue(),minSpiner.getValue(),0);
            List<UserDTO> list = new ArrayList<>(); list.add(loggedUser);
            service.saveEvent(new EventDTO(descriptionEventTextField.getText(), dateEventTextField.getValue(), locationEventTextField.getText(), list, nameEventTextField.getText(), loggedUser.getId(), time));
            descriptionEventTextField.clear(); locationEventTextField.clear(); nameEventTextField.clear(); dateEventTextField.getEditor().clear();
        }
    }

    /**
     * Set service
     * @param service SuperService
     */
    public void setService(SuperService service) {
        SpinnerValueFactory<Integer> hourValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23);
        hourValue.setValue(1);
        SpinnerValueFactory<Integer> minValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59);
        minValue.setValue(0);
        hourSpiner.setValueFactory(hourValue);
        minSpiner.setValueFactory(minValue);
        this.service = service;
    }

    /**
     * Set logged user
     * @param loggedUser User
     */
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }
}
