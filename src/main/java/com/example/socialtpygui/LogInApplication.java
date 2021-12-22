package com.example.socialtpygui;

import com.example.socialtpygui.controller.LogInController;
import com.example.socialtpygui.repository.db.FriendshipDb;
import com.example.socialtpygui.repository.db.FriendshipRequestDb;
import com.example.socialtpygui.repository.db.MessageDb;
import com.example.socialtpygui.repository.db.UserDb;
import com.example.socialtpygui.service.SuperService;
import com.example.socialtpygui.service.entityservice.FriendshipService;
import com.example.socialtpygui.service.entityservice.MessageService;
import com.example.socialtpygui.service.entityservice.NetworkService;
import com.example.socialtpygui.service.entityservice.UserService;
import com.example.socialtpygui.service.validators.MessageValidator;
import com.example.socialtpygui.service.validators.UserValidator;
import com.example.socialtpygui.tests.Tests;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class LogInApplication extends Application {
    private double yCord;
    private double xCord;

    @Override
    public void start(Stage stage) throws IOException {
       //Tests.RunALL();
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("logIn-view.fxml"));

        AnchorPane panel= fxmlLoader.load();
        panel.setOnMousePressed(event->{
            xCord = event.getSceneX();
            yCord = event.getSceneY();
        });
        panel.setOnMouseDragged(event->{
            stage.setX(event.getScreenX() - xCord);
            stage.setY(event.getScreenY() - yCord);
        });
        LogInController logInController= fxmlLoader.getController();
        logInController.setService(createSuperService());


        Scene scene = new Scene(panel, 580, 460);
        scene.getStylesheets().add(LogInApplication.class.getResource("log.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }


    private SuperService createSuperService(){
        UserValidator userValidator= new UserValidator();
        UserDb userDb= new UserDb("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","postgres");
        FriendshipDb friendshipDb= new FriendshipDb("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","postgres");
        MessageDb messageDb = new MessageDb("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","postgres");
        FriendshipRequestDb friendshipRequestDb = new FriendshipRequestDb("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres");
        UserService userService = new UserService(userDb, userValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipRequestDb, friendshipDb);
        NetworkService networkService = new NetworkService(userDb, friendshipDb);
        MessageService messageService = new MessageService(messageDb);
        MessageValidator messageValidator= new MessageValidator(userValidator);
        SuperService service= new SuperService(messageService, networkService, friendshipService, userService,userValidator,messageValidator);
        return service;
    }

    public static void main(String[] args) {
        launch();
    }
}
