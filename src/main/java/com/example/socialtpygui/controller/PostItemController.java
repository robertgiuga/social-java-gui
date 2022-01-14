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
    private Label fromPostLabel, numberOfLikesLabel, datePostLabel;

    @FXML
    private ImageView likePostClickImageView, likePostUnclickImageView;

    @FXML
    private Text textPost;

    private Post post;

    /**
     * hide the unlike label and imageView
     */
    public void hideUnlike(){
        likePostUnclickImageView.setVisible(false);
    }

    /**
     * hide the like label and imageView
     */
    public void hideLike(){
        likePostClickImageView.setVisible(false);
    }

    /**
     * show the unlike label and imageView
     */
    public void showUnlike(){
        likePostUnclickImageView.setVisible(true);
    }

    /**
     * show the like label and imageView
     */
    public void showLike(){
        likePostClickImageView.setVisible(true);
    }

    /**
     * Set the text from postLabel
     * @param fromPostLabel String
     */
    public void setFromPostLabel(String fromPostLabel) {
        this.fromPostLabel.setText(fromPostLabel);
    }

    /**
     * Set the text from datePostLabel
     *
     */
    public void setDatePostLabel(String datePostLabel) {this.datePostLabel.setText(datePostLabel);}

    /**
     * Set the text from post text label
     * @param textPost String
     */
    public void setTextPost(String textPost) {
        this.textPost.setText(textPost);
    }

    /**
     * Handler for like button(imageView), hide unlike label and imageView, show like label and imageView, increment
     * number of likes and fire LikeEvent with LIKE_POST flag for like a post.
     * @param mouseEvent MouseEvent
     */
    public void handlerLike(MouseEvent mouseEvent) {
        hideUnlike();
        showLike();
        incrementNoLikes();
        likePostUnclickImageView.fireEvent(new LikeEvent(LikeEvent.LIKE_POST, post.getId()));
    }

    /**
     * Handler for unlike button(imageView), hide like label and imageView, show unlike label and imageView, decrement
     * number of likes and fire LikeEvent with UNLIKE_POST flag for unlike a post.
     * @param mouseEvent MouseEvent
     */
    public void handlerUnlike(MouseEvent mouseEvent) {
        hideLike();
        showUnlike();
        decrementNoLikes();
        likePostClickImageView.fireEvent(new LikeEvent(LikeEvent.UNLIKE_POST, post.getId()));
    }

    /**
     * Set the post
     * @param post Post
     */
    public void setPost(Post post){
        this.post = post;
    }

    /**
     * Set number of likes
     * @param numberOfLikesLabel String
     */
    public void setNumberOfLikesLabel(String numberOfLikesLabel) {
        this.numberOfLikesLabel.setText(numberOfLikesLabel);
    }

    /**
     * Decrement number of likes
     */
    private void decrementNoLikes(){
        numberOfLikesLabel.setText(String.valueOf((Integer.parseInt(numberOfLikesLabel.getText()) - 1)));
    }

    /**
     * Increment numbers of likes
     */
    private void incrementNoLikes(){
        numberOfLikesLabel.setText(String.valueOf((Integer.parseInt(numberOfLikesLabel.getText()) + 1)));
    }
}
