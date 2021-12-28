package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.Post;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import com.example.socialtpygui.domain.User;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class PostViewController {

    @FXML
    private ScrollPane scrollPanePostView;

    @FXML
    private AnchorPane anchorPanePostView;

    @FXML
    private GridPane gridPanePostView;

    private User loggedUser;
    private SuperService service;


    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setService(SuperService service) {
        this.service = service;
    }

    private Pane createItem(Post post) throws IOException {
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("post-item.fxml"));
        Pane item = loader.load();
        PostItemController controller = loader.getController();
        controller.setFromPostLabel(post.getEmailUser());
        controller.setTextPost(post.getDescription());
        controller.hide();
        return item;


    }

    public void load()
    {
        service.getAllPostFromFriends(this.loggedUser.getId()).forEach(post->{
            Pane item = null;
            try {
                item = createItem(post);
                gridPanePostView.addRow(gridPanePostView.getRowCount(), item);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
