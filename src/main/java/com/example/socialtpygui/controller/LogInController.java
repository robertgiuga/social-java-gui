package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.PageDTO;
import com.example.socialtpygui.utils.socket.TCPClient;
import com.example.socialtpygui.utils.socket.UDPClient;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.domainEvent.LoadView;
import com.example.socialtpygui.service.SuperService;
import com.example.socialtpygui.service.validators.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LogInController {

    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private Button logInBtn, createAcountBtn;
    @FXML
    private BorderPane borderPaneLogWindow;

    SignUpMainWindowController signUpMainWindowController;

    private SuperService service;
    private double yCord;
    private double xCord;



    @FXML
    private void handlerLogIn() throws IOException {
        String username= usernameTxt.getText();
        String password= passwordTxt.getText();
        try {
            PageDTO pageDTO = service.logIn(username, password);

            FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("mainWindow.fxml"));
            Stage manWindowStage= new Stage();
            StackPane panel= fxmlLoader.load();

            MainWindowController mainWindowController= fxmlLoader.getController();

            TCPClient client= new TCPClient();
            UDPClient client1= new UDPClient(service,client.createConnection(pageDTO.getUserDTO().getId()));
            client1.start();

            mainWindowController.load(service,pageDTO.getUserDTO(),client1, pageDTO);

            Scene scene = new Scene(panel, 650, 600);

            panel.setOnMousePressed(event->{
                xCord = event.getSceneX();
                yCord = event.getSceneY();
            });
            panel.setOnMouseDragged(event->{
                manWindowStage.setX(event.getScreenX() - xCord);
                manWindowStage.setY(event.getScreenY() - yCord);
            });

            scene.getStylesheets().add(LogInApplication.class.getResource("mainWindow.css").toExternalForm());
            scene.setFill(Color.TRANSPARENT);
            manWindowStage.setScene(scene);
            manWindowStage.initStyle(StageStyle.TRANSPARENT);
            manWindowStage.initModality(Modality.NONE);
            manWindowStage.show();

            Stage logInStage =(Stage) logInBtn.getScene().getWindow();
            logInStage.close();


        }catch (ValidationException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();

        }
    }

    public void setService(SuperService service) {
        this.service = service;
    }


    public void handlerExitBtn(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    public void handlerMinimizeBtn(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).setIconified(true);
    }

    public void handlerExtindButton(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).setFullScreen(true);
    }

    /**
     * Handler for createAccountBtn, if the text is SignUp then load the SignUp window, if the
     * text is SignIn then load the SignIn window
     * @param mouseEvent MouseEvent
     * @throws IOException
     */
    public void handlerCreateAccountBtn(MouseEvent mouseEvent) throws IOException {
        if (createAcountBtn.getText().equals("SignUp")) {
            loadEventFilter();
            loadSignUpWindow();
        }
        else if (createAcountBtn.getText().equals("SignIn"))
        {
            loadSignInWindow();
        }
    }

    /**
     * Filter for catch the LoadView event.
     */
    public void loadEventFilter()
    {
        borderPaneLogWindow.addEventFilter(LoadView.SIGN_UP_NEXT, this::handlerForLoadView);
        borderPaneLogWindow.addEventFilter(LoadView.FINAL_SIGN_UP, this::handlerForLoadView);
    }

    /**
     * if the flag of LoadView event is SIGN_UP_NEXT then load the verification code window, if the flag is
     * FINAL_SIGN_UP add the user and if the data is invalid throw alert
     * @param t LoadView event
     */
    private void handlerForLoadView(LoadView t) {
        if (t.getEventType().equals(LoadView.SIGN_UP_NEXT)){
            FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("signUpConfirmEmailWindow.fxml"));
            AnchorPane panel = null;
            try {panel = fxmlLoader.load();} catch (IOException e) {e.printStackTrace();}
            SignUpConfirmEmailWindowController controller = fxmlLoader.getController();
            controller.setVerificationCode(t.getValidationCode());
            Pane view = new Pane(panel);
            borderPaneLogWindow.setCenter(view);
        }
        if (t.getEventType().equals(LoadView.FINAL_SIGN_UP))
        {
            try{
                service.addUser(new User(signUpMainWindowController.getTextFieldSignUpFN(), signUpMainWindowController.getTextFieldSignUpLN(),signUpMainWindowController.getTextFieldSignUpEmail(),signUpMainWindowController.getTextFieldPassword()));
                loadSignInWindow();
            }catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid Date!");
                alert.show();
            }
        }
    }

    /**
     * Load SignIn window
     * @throws IOException
     */
    private void loadSignInWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("signInWindow.fxml"));
        AnchorPane panel = fxmlLoader.load();
        LogInController logInController = fxmlLoader.getController();
        logInController.setService(service);
        Pane view = new Pane(panel);
        borderPaneLogWindow.setCenter(view);
        createAcountBtn.setText("SignUp");
    }

    /**
     * Load SignUp window
     * @throws IOException
     */
    private void loadSignUpWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("singUpMainWindow.fxml"));
        AnchorPane panel = fxmlLoader.load();
        this.signUpMainWindowController = fxmlLoader.getController();
        Pane view = new Pane(panel);
        borderPaneLogWindow.setCenter(view);
        createAcountBtn.setText("SignIn");
    }
}
