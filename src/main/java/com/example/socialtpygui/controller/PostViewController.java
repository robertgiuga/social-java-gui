package com.example.socialtpygui.controller;

import com.example.socialtpygui.LogInApplication;
import com.example.socialtpygui.domain.Post;
import com.example.socialtpygui.domain.UserDTO;
import com.example.socialtpygui.domainEvent.LikeEvent;
import com.example.socialtpygui.service.SuperService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDate;
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

    private UserDTO loggedUser;
    private SuperService service;
    private int pageId=0;

    /**
     * Set the logged user
     * @param loggedUser User
     */
    public void setLoggedUser(UserDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * Set the service
     * @param service SuperService
     */
    public void setService(SuperService service) {
        this.service = service;
    }

    /**
     * Create a post-item and set post, number of likes and text from labels for this item
     * @param post Post
     * @return the post-item
     * @throws IOException .
     */
    private Pane createItem(Post post) throws IOException {
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("post-item.fxml"));
        Pane item = loader.load();
        PostItemController controller = loader.getController();
        controller.setFromPostLabel(post.getEmailUser());
        controller.setTextPost(post.getDescription());
        controller.setPost(post);
        controller.setDatePostLabel(post.getDate().toString());
        if (service.isPostLike(post.getId(), loggedUser.getId())) {controller.hideUnlike();}
        else {controller.hideLike();}
        return item;
    }

    /**
     * load the post and filter for fire events
     */
    public void load()
    {
        loadLikeEventFilter(); nextPage();
    }

    /**
     * Filter for fire events(LikeEvent).
     */
    public void loadLikeEventFilter()
    {
        anchorPanePostView.addEventFilter(LikeEvent.ANY, this::handlerForEvent);
    }

    /**
     * Handler for events, if the event flag is LIKE_POST use service method to like a post, if the flag is UNLIKE_POST
     * use service method to unlike a post
     * @param t LikeEvent
     */
    private void handlerForEvent(LikeEvent t) {
        if (t.getEventType().equals(LikeEvent.LIKE_POST)){
            service.likeAPost(t.getIdPost(), loggedUser.getId());
        }
        else if (t.getEventType().equals(LikeEvent.UNLIKE_POST)){
            service.unlikeAPost(t.getIdPost(), loggedUser.getId());
        }
    }

    /**
     * Handler for post button, public a post.
     * @param mouseEvent MouseEvent
     */
    public void handlerForPost(MouseEvent mouseEvent) {
        insertRows(1);
        Post post = service.savePost(new Post(postTextField.getText(), loggedUser.getId(), LocalDate.now(), 0));

        try {gridPanePostView.addRow(0, createItem(post));} catch (IOException e) {e.printStackTrace();}

    }

    /**
     * insert rows in GridShowMessage
     * @param count
     */
    private void insertRows(int count) {
        for (Node child : gridPanePostView.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(child);
            GridPane.setRowIndex(child, rowIndex == null ? count : count + rowIndex);
        }
    }

    /**
     * loads the next page with data in the ui
     */
    private void nextPage(){
        List<Post> nextPost= service.getAllPostFromFriends(this.loggedUser.getId(),pageId++);
        if(nextPost.size()>0){
            nextPost.forEach(post->{
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
    public void scrollHandler(ScrollEvent scrollEvent) {
        System.out.println(scrollPanePostView.getVvalue());
        if(scrollPanePostView.getVvalue()>0.45&&scrollPanePostView.getVvalue()<0.55){
            nextPage();
        }
    }
}
