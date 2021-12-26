package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.utils.socket.TCPClient;
import com.example.socialtpygui.utils.socket.UDPClient;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.service.SuperService;
import com.example.socialtpygui.service.validators.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;

public class MainWindowController {

    public AnchorPane leftPane;
    public Button meniuBtn;
    @FXML
    private ImageView friendsViewBtn;

    @FXML
    private TextField searchbar;

    @FXML
    private ImageView requestsViewBtn;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ImageView messageViewBtn;

    @FXML
    private Button exitBtnMW;

    @FXML
    private Button minimizeBtnMW;

    @FXML
    private Button extindBtnMW;

    @FXML
    private Button statisticsBtn;

    Pane friendsView=null;

    private SuperService service;

    private User loggedUser;
    private UDPClient udpThread;

    public void load(SuperService service, User loggedUser, UDPClient udpThread){
        leftPane.setVisible(false);
        this.service=service;
        this.loggedUser=loggedUser;
        this.udpThread= udpThread;
    }


    @FXML
    private void handlerFriendsButton() throws IOException {
        slidebarDisbale();
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("friends-view.fxml"));
        Pane pane = loader.load();
        FriendsController friendsController= loader.getController();
        friendsController.load(service,loggedUser);
        borderPane.setCenter(pane);
    }

    @FXML
    private void handlerKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
        {
            FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("search-view.fxml"));
            Pane view = fxmlLoader.load();
            SearchController searchController = fxmlLoader.getController();
            searchController.setService(service);
            searchController.setLoggedUser(this.loggedUser);
            try{searchController.load(searchbar.getText());} catch (ValidationException ignored){}
            borderPane.setCenter(view);
        }
    }

    @FXML
    private void handlerRequestsButton() throws IOException{
        FXMLLoader loader= new FXMLLoader(LogInApplication.class.getResource("request-view.fxml"));
        AnchorPane panel= loader.load();

        RequestController requestController= loader.getController();
        requestController.setService(service);
        requestController.setLoggedUser(loggedUser);
        requestController.load();

        Pane view = new Pane(panel);

        borderPane.setCenter(view);
    }

    @FXML
    private void handlerMessageBtn() throws IOException {
        FXMLLoader loader= new FXMLLoader(LogInApplication.class.getResource("messageWindow-view.fxml"));
        Pane panel= loader.load();
        MessageController messageController=loader.getController();
        messageController.load(service,loggedUser);
        borderPane.setCenter(panel);
    }

    public void handlerExitBtnMW(ActionEvent actionEvent) throws IOException {
        TCPClient closeThread = new TCPClient();
        closeThread.closeConnection(loggedUser.getId());

        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    public void handlerMinimizeBtnMW(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).setIconified(true);
    }

    public void handlerExtindButtonMW(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).setFullScreen(true);
    }

    @FXML
    private void handlerLogOutBtn() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("logIn-view.fxml"));

        AnchorPane panel= fxmlLoader.load();

        LogInController logInController= fxmlLoader.getController();
        logInController.setService(service);

        Stage stage= new Stage();
        Scene scene = new Scene(panel, 580, 460);
        scene.getStylesheets().add(LogInApplication.class.getResource("log.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

        Stage mainWnd =(Stage) borderPane.getScene().getWindow();
        mainWnd.close();

    }


    public void handelerStatisticsbtn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("statistics-view.fxml"));

        AnchorPane pane = fxmlLoader.load();
        StatisticsController controller = fxmlLoader.getController();
        controller.load(service, loggedUser);

        borderPane.setCenter(pane);
    }

    private void slidebarDisbale(){
        leftPane.setVisible(false);
        borderPane.setDisable(false);
    }

    public void handlerMenuShow() {
        if(leftPane.isVisible()) {
            slidebarDisbale();
        }
        else {
            leftPane.setVisible(true);
            borderPane.setDisable(true);
        }

    }

    public void handlerForBgClick(MouseEvent event) {
    }
}
