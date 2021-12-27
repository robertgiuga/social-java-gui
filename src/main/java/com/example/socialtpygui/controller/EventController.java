package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.EventCursor;
import com.example.socialtpygui.domainEvent.LoadView;
import com.example.socialtpygui.service.SuperService;
import javafx.event.Event;
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

    /**
     * Load first Event.
     * @throws IOException .
     */
    public void loadExploreEventView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("eventItem.fxml"));
        AnchorPane panel = fxmlLoader.load();
        EventItemController eventItemController = fxmlLoader.getController();
        eventItemController.setService(service);
        eventItemController.setLoggedUser(loggedUser);
        List<EventDTO> list = new ArrayList<>();
        service.findAllEvents().forEach(list::add);
        if (service.sizeEvent() != 0) {eventItemController.load(list.get(0));}
        else {
            loadCreateEvent();}
        Pane view = new Pane(panel);
        borderPaneMainEventWindow.setCenter(view);
    }

    /**
     * Handler for exploreBtn, load all events.
     * @param mouseEvent MouseEvent
     * @throws IOException .
     */
    public void handlerExploreEventBtn(MouseEvent mouseEvent) throws IOException {
        if (service.sizeEvent() != 0) {
            loadCursorEventFilter();
            loadExploreEventView();
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
     * Filter for Event(fireEvent).
     */
    public void loadCursorEventFilter()
    {
        borderPaneMainEventWindow.addEventFilter(EventCursor.ANY, this::handlerForEvent);
        borderPaneMainEventWindow.addEventFilter(LoadView.LOAD_EVENTS, this::handlerForEvent);
    }

    /**
     * Handler for event(fireEvent), if the event is EventCursor.NEXT_EVENT, next event, and if the event is ventCursor.PREVIOUS_EVENT, previous
     * event
     * @param t Event
     */
    private void handlerForEvent(Event t){
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
            else if (service.sizeEvent() == 0){
                try {
                    loadCreateEvent();
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
            else if (service.sizeEvent() == 0){
                try {
                    loadCreateEvent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (t.getEventType().equals(LoadView.LOAD_EVENTS)){
            if (service.sizeEvent() == 0){
                try {
                    loadCreateEvent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    loadExploreEventView();
                    this.currentEventIndex = 0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Load a eventItem.
     * @throws IOException .
     */
    public void loadEventItem() throws IOException {
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

    /**
     * Handler for create event, load create event window.
     * @param mouseEvent MouseEvent
     * @throws IOException .
     */
    public void handlerCreateEventBtn(MouseEvent mouseEvent) throws IOException {
       loadCreateEvent();
    }

    /**
     * Set logged user.
     * @param loggedUser User
     */
    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void loadCreateEvent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("createEventWindow.fxml"));
        AnchorPane panel = fxmlLoader.load();
        CreateEventWindowController controller = fxmlLoader.getController();
        controller.setLoggedUser(loggedUser);
        controller.setService(service);
        Pane view = new Pane(panel);
        borderPaneMainEventWindow.setCenter(view);
    }



}
