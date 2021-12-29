package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.Post;
import com.example.socialtpygui.domainEvent.LikeEvent;
import com.example.socialtpygui.service.SuperService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import com.example.socialtpygui.domain.User;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostViewController {

    @FXML
    private ScrollPane scrollPanePostView;

    @FXML
    private AnchorPane anchorPanePostView;

    @FXML
    private GridPane gridPanePostView;

    @FXML
    private TextField postTextField;

    private User loggedUser;
    private SuperService service;
    private List<Post> list = new ArrayList<>();


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
        controller.setPost(post);
        controller.setNumberOfLikesLabel(String.valueOf(service.numberOfLikes(post.getId())));
        if (service.isPostLike(post.getId(), loggedUser.getId())) {controller.hideUnlike();}
        else {controller.hideLike();}
        return item;


    }

    public void load()
    {
        loadLikeEventFilter();
        service.getAllPostFromFriends(this.loggedUser.getId()).forEach(post->{
            Pane item = null;
            list.add(post);
            try {
                item = createItem(post);
                gridPanePostView.addRow(gridPanePostView.getRowCount(), item);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadLikeEventFilter()
    {
        anchorPanePostView.addEventFilter(LikeEvent.ANY, this::handlerForEvent);
    }

    private void handlerForEvent(LikeEvent t) {
        if (t.getEventType().equals(LikeEvent.LIKE_POST)){
            service.likeAPost(t.getIdPost(), loggedUser.getId());
        }
        else if (t.getEventType().equals(LikeEvent.UNLIKE_POST)){
            service.unlikeAPost(t.getIdPost(), loggedUser.getId());
        }
    }

    public void handlerForPost(MouseEvent mouseEvent) {
        Post post = service.savePost(new Post(postTextField.getText(), loggedUser.getId(), LocalDate.now()));
        list.add(post);
        try {gridPanePostView.addRow(gridPanePostView.getRowCount(), createItem(post));} catch (IOException e) {e.printStackTrace();}

    }
}
