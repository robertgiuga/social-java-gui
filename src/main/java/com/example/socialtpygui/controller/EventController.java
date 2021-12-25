package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.EventCursor;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventController {
    @FXML
    private BorderPane borderPaneMainEventWindow;
    @FXML
    private Button exploreEventBtn, createEventBtn;

    private SuperService service;
    private int currentEventIndex = 0;
    private User loggedUser;


    public void handlerExploreEventBtn(MouseEvent mouseEvent) throws IOException {
        loadEventFilter();
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("eventItem.fxml"));
        AnchorPane panel = fxmlLoader.load();
        EventItemController eventItemController = fxmlLoader.getController();
        eventItemController.setService(service);
        eventItemController.setLoggedUser(loggedUser);
        List<EventDTO> list = new ArrayList<>();
        service.findAllEvents().forEach(list::add);
        eventItemController.load(list.get(0));
        Pane view = new Pane(panel);
        borderPaneMainEventWindow.setCenter(view);
    }

    public void setService(SuperService service) {
        this.service = service;
    }

    public void loadEventFilter()
    {
        borderPaneMainEventWindow.addEventFilter(EventCursor.NEXT_EVENT, this::handlerForEventCursor);
        borderPaneMainEventWindow.addEventFilter(EventCursor.PREVIOUS_EVENT, this::handlerForEventCursor);
    }

    private void handlerForEventCursor(EventCursor t){
        if (t.getEventType().equals(EventCursor.NEXT_EVENT))
        {
            if ((service.sizeEvent()!= 0) && (this.currentEventIndex >= 0) && (this.currentEventIndex < service.sizeEvent()-1))
            {
                this.currentEventIndex++;
                try {
                    loadEventItem();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (t.getEventType().equals(EventCursor.PREVIOUS_EVENT)){
            if ((service.sizeEvent()!= 0) && (this.currentEventIndex > 0) && (this.currentEventIndex <= service.sizeEvent()-1))
            {
                this.currentEventIndex--;
                try {
                    loadEventItem();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void loadEventItem() throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("eventItem.fxml"));
            AnchorPane panel = fxmlLoader.load();
            EventItemController eventItemController = fxmlLoader.getController();
            eventItemController.setService(this.service);
            eventItemController.setLoggedUser(loggedUser);
            List<EventDTO> list = new ArrayList<>();
            service.findAllEvents().forEach(list::add);
            eventItemController.load(list.get(this.currentEventIndex));
            Pane view = new Pane(panel);
            borderPaneMainEventWindow.setCenter(view);
    }

    public void handlerCreateEventBtn(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("createEventWindow.fxml"));
        AnchorPane panel = fxmlLoader.load();
        CreateEventWindowController controller = fxmlLoader.getController();
        controller.setLoggedUser(loggedUser);
        controller.setService(service);
        Pane view = new Pane(panel);
        borderPaneMainEventWindow.setCenter(view);
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
}
