package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.EventDTO;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.domainEvent.EventCursor;
import com.example.socialtpygui.domainEvent.LoadView;
import com.example.socialtpygui.service.SuperService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
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
    private ImageView exploreEventBtn, createEventBtn;

    private SuperService service;
    private int pageId = 0;
    private UserDTO loggedUser;

    /**
     * Load first Event.
     * @throws IOException .
     */
    public void resetEventToFirst() throws IOException {
        pageId=0;
        nextPage();

    }

    /**
     * Handler for exploreBtn, load all events.
     * @param mouseEvent MouseEvent
     * @throws IOException .
     */
    public void handlerExploreEventBtn(MouseEvent mouseEvent) throws IOException {
        resetEventToFirst();
    }


    /**
     * Filter for Event(fireEvent).
     */
    public void addEventFilter()
    {
        borderPaneMainEventWindow.addEventFilter(EventCursor.ANY, this::handlerForEvent);
        borderPaneMainEventWindow.addEventFilter(LoadView.LOAD_EVENTS, this::handlerForEvent);
    }

    private void nextPage() throws IOException {
        List<EventDTO> eventsDTO =service.findAllEvents(pageId);
        if ( eventsDTO.size()>0) {
            loadEventItem(eventsDTO.get(0));
            pageId++;
        }
    }

    private void previousPage() throws IOException {
        if(pageId>0) {
            List<EventDTO> eventsDTO = service.findAllEvents(--pageId);
            if ( eventsDTO.size()>0)
                loadEventItem(eventsDTO.get(0));

        }
    }

    /**
     * Handler for event(fireEvent), if the event is EventCursor.NEXT_EVENT, next event, and if the event is ventCursor.PREVIOUS_EVENT, previous
     * event
     * @param t Event
     */
    private void handlerForEvent(Event t){
        try {
           if (t.getEventType().equals(EventCursor.NEXT_EVENT))
               nextPage();
           else if (t.getEventType().equals(EventCursor.PREVIOUS_EVENT))
               previousPage();
           else if (t.getEventType().equals(LoadView.LOAD_EVENTS))
               resetEventToFirst();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load a eventItem.
     * @throws IOException .
     */
    public void loadEventItem(EventDTO eventDTO) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("eventItem.fxml"));
            AnchorPane panel = fxmlLoader.load();
            EventItemController eventItemController = fxmlLoader.getController();
            eventItemController.setService(this.service);
            eventItemController.setLoggedUser(loggedUser);

            eventItemController.load(eventDTO);
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



    public void loadCreateEvent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("createEventWindow.fxml"));
        AnchorPane panel = fxmlLoader.load();
        CreateEventWindowController controller = fxmlLoader.getController();
        controller.setLoggedUser(loggedUser);
        controller.setService(service);
        Pane view = new Pane(panel);
        borderPaneMainEventWindow.setCenter(view);
    }


    public void load(SuperService service, UserDTO loggedUser) {
        this.service=service;
        this.loggedUser=loggedUser;
        addEventFilter();
        try {
            resetEventToFirst();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
