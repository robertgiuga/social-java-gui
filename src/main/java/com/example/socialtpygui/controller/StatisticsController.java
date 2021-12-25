package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.FriendShipDTO;
import com.example.socialtpygui.domain.ReplyMessage;
import com.example.socialtpygui.domain.Tuple;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.service.SuperService;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
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


    public void handlerRaport1Btn() throws IOException {
        LocalDate startDate= dateStart.getValue();
        LocalDate stopDate= dateStop.getValue();

        if(startDate!=null&&stopDate!=null) {
            List<FriendShipDTO> friendShipDTOList = service.getUserFriendshipsInDate(loggedUser.getId(), startDate, stopDate);

            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            PDPageContentStream stream= new PDPageContentStream(doc,page);

            stream.beginText();
            stream.setFont(PDType1Font.HELVETICA,26);
            stream.moveTextPositionByAmount(100,750);
            stream.drawString("Friendships and userMessages report");
            stream.endText();

            stream.beginText();
            stream.setFont(PDType1Font.TIMES_ROMAN,16);
            int y= 700;
            int x= 50;
            stream.moveTextPositionByAmount(x,y);
            stream.drawString("Date interval: "+ startDate.toString()+" "+ stopDate.toString());
            stream.endText();
            if(friendShipDTOList.size()==0){
                stream.beginText();
                y-=20;
                stream.moveTextPositionByAmount(x,y);
                stream.drawString("You got no new friends in this date interval.");
                stream.endText();
            }
            else {
                stream.beginText();
                y-=20;
                stream.moveTextPositionByAmount(x,y);
                stream.drawString("Your new friendship in this date interval are with: ");
                stream.endText();
                x+=20;
                for (FriendShipDTO friendShipDTO : friendShipDTOList) {
                    stream.beginText();
                    y-=20;
                    stream.moveTextPositionByAmount(x,y);
                    stream.drawString("* "+friendShipDTO.getUser2().getFirstName() + " " + friendShipDTO.getUser2().getLastName());
                    stream.endText();
                }
            }

            x-=20;
            y-=40;
            //TODO about userMessages
            List<Tuple<User, Integer>> userMessages= service.getMessagesInDate(loggedUser.getId(),startDate,stopDate);
            if(userMessages.size()==0){
                stream.beginText();
                stream.moveTextPositionByAmount(x,y);
                stream.drawString("You got no userMessages in this date period.");
                stream.endText();
            }
            else {
                stream.beginText();
                stream.moveTextPositionByAmount(x,y);
                stream.drawString("You have been sending and receive messages with:");
                stream.endText();
                x+=20;
                for (Tuple<User, Integer> userMessage:userMessages){
                   stream.beginText();
                   y-=20;
                   stream.moveTextPositionByAmount(x,y);
                   stream.drawString("* "+userMessage.getLeft().getLastName()+" "+userMessage.getLeft().getFirstName()+" in number of "+userMessage.getRight());
                   stream.endText();
                }
            }
            stream.close();
            savePDFFile(doc);
        }

    }

    /**
     * saves a pdf Document (must not be close)
     * @param doc the document to pe save
     * @throws IOException .
     */
    private void savePDFFile(PDDocument doc) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save pdf file...");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));

        File file = fc.showSaveDialog(dateStart.getScene().getWindow());
        if(file!=null){
            doc.save(file);
        }
        doc.close();
    }
}
