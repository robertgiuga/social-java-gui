package com.example.socialtpygui.controller;

import com.example.socialtpygui.domain.Post;
import com.example.socialtpygui.domainEvent.LikeEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class PostItemController {
    @FXML
    private Label fromPostLabel, labelLike, unlikeLabel, numberOfLikesLabel;

    @FXML
    private ImageView likePostClickImageView, likePostUnclickImageView;

    @FXML
    private Text textPost;

    private Post post;

    public void hideUnlike(){
        unlikeLabel.setVisible(false);
        likePostUnclickImageView.setVisible(false);
    }

    public void hideLike(){
        labelLike.setVisible(false);
        likePostClickImageView.setVisible(false);
    }

    public void showUnlike(){
        unlikeLabel.setVisible(true);
        likePostUnclickImageView.setVisible(true);
    }

    public void showLike(){
        labelLike.setVisible(true);
        likePostClickImageView.setVisible(true);
    }

    public void setFromPostLabel(String fromPostLabel) {
        this.fromPostLabel.setText(fromPostLabel);
    }

    public void setTextPost(String textPost) {
        this.textPost.setText(textPost);
    }

    public void handlerLike(MouseEvent mouseEvent) {
        hideUnlike();
        showLike();
        incrementNoLikes();
        likePostUnclickImageView.fireEvent(new LikeEvent(LikeEvent.LIKE_POST, post.getId()));
    }

    public void handlerUnlike(MouseEvent mouseEvent) {
        hideLike();
        showUnlike();
        decrementNoLikes();
        likePostClickImageView.fireEvent(new LikeEvent(LikeEvent.UNLIKE_POST, post.getId()));
    }

    public void setPost(Post post){
        this.post = post;
    }

    public void setNumberOfLikesLabel(String numberOfLikesLabel) {
        this.numberOfLikesLabel.setText(numberOfLikesLabel);
    }

    private void decrementNoLikes(){
        numberOfLikesLabel.setText(String.valueOf((Integer.parseInt(numberOfLikesLabel.getText()) - 1)));
    }

    private void incrementNoLikes(){
        numberOfLikesLabel.setText(String.valueOf((Integer.parseInt(numberOfLikesLabel.getText()) + 1)));
    }
}
