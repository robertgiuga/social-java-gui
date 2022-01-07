package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.*;
import com.example.socialtpygui.service.SuperService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class StatisticsController {


    public DatePicker dateStart;
    public DatePicker dateStop;
    public ProgressBar progressBar;
    public GridPane gridPane;
    private SuperService service;
    private UserDTO loggedUser;
    ToggleGroup toggleGroup;

    public void load(SuperService service, UserDTO loggedUser){
        this.service=service;
        this.loggedUser=loggedUser;
        toggleGroup= new ToggleGroup();
        service.getFriends(loggedUser.getId()).forEach(friendShipDTO -> {
            try {
                Pane item= createItem(friendShipDTO.getUser2());
                gridPane.addRow(gridPane.getRowCount(), item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private Pane createItem(UserDTO user) throws IOException {
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("friendReport-viewItem.fxml"));
        Pane item = loader.load();
        FriendReportController controller= loader.getController();
        controller.setName(user.getLastName()+" "+ user.getFirstName());
        controller.setToggleGroup(toggleGroup);
        controller.setId(user.getId());
        return item;
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
            List<Tuple<User, Integer>> userMessages= service.getMessagesInDate(loggedUser.getId(),startDate,stopDate);
            if(userMessages.size()==0){
                stream.beginText();
                stream.moveTextPositionByAmount(x,y);
                stream.drawString("You got no userMessages in this date period.");
                stream.endText();
            }
            else {
                float size=1/(float)userMessages.size();
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
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select dates");
            alert.show();
        }
    }



    /**
     * saves a pdf Document (must not be close) and close the doc
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

    public void handlerRaport2Btn(ActionEvent event) throws IOException {
        LocalDate startDate= dateStart.getValue();
        LocalDate stopDate= dateStop.getValue();

        if(startDate!=null&&stopDate!=null) {
            String friendEmail = null;
            for (Node node : gridPane.getChildren()) {
                Pane item = (Pane) node;
                for (Node node1 : item.getChildren()) {
                    if (node1 instanceof RadioButton)
                        if (((RadioButton) node1).isSelected())
                            friendEmail = node1.getId();
                }
            }
            if (friendEmail != null) {
                User friend = service.findOneUser(friendEmail);
                PDDocument doc = new PDDocument();
                PDPage page = new PDPage();
                doc.addPage(page);
                PDPageContentStream stream = new PDPageContentStream(doc, page);
                stream.beginText();
                stream.setFont(PDType1Font.HELVETICA, 26);
                stream.moveTextPositionByAmount(100, 750);
                stream.drawString("Your Messages with " + friend.getLastName() + " " + friend.getFirstName() + "are:");
                stream.setFont(PDType1Font.TIMES_ROMAN, 16);
                stream.endText();
                int x = 20, y = 700;
                String logUserName=loggedUser.getLastName() + " " + loggedUser.getFirstName();
                String friendName=friend.getLastName() + " " + friend.getFirstName();

                for (ReplyMessage replyMessage : service.getMessagesBetween2UsersInDate(loggedUser.getId(), friendEmail, startDate, stopDate)) {
                    stream.beginText();
                    if (replyMessage.getFrom().equals(loggedUser.getId())) {
                        x += 300;
                        stream.moveTextPositionByAmount(x,y);
                        stream.drawString(logUserName+" "+replyMessage.getMessage());
                        x-=300;
                    }
                    else {
                        stream.moveTextPositionByAmount(x,y);
                        stream.drawString(friendName+" "+replyMessage.getMessage());
                    }
                    stream.endText();
                    y-=20;
                    if(y<=20){
                        stream.close();
                        y=700;
                        page = new PDPage();
                        doc.addPage(page);
                        stream = new PDPageContentStream(doc, page);
                        stream.setFont(PDType1Font.TIMES_ROMAN, 16);
                    }

                }
                stream.close();
                savePDFFile(doc);

            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please select a friend");
                alert.show();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select dates");
            alert.show();
        }

    }
}
