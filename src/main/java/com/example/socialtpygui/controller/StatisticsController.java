package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.FriendShipDTO;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.service.SuperService;
import javafx.scene.control.DatePicker;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class StatisticsController {


    public DatePicker dateStart;
    public DatePicker dateStop;
    private SuperService service;
    private User loggedUser;

    public void load(SuperService service, User loggedUser){
        this.service=service;
        this.loggedUser=loggedUser;
    }


    public void handlerRaport1Btn() {
        LocalDate startDate= dateStart.getValue();
        LocalDate stopDate= dateStop.getValue();


        List<FriendShipDTO> friendShipDTOList = service.getUserFriendshipsInDate(loggedUser.getId(),startDate,stopDate);

        PDDocument doc = new PDDocument();

    }
}
