package com.example.socialtpygui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class PostItemController {
    @FXML
    private Label fromPostLabel, labelLike, unlikeLabel;

    @FXML
    private ImageView likePostClickImageView, likePostUnclickImageView;

    @FXML
    private Text textPost;

    public void hide(){
        unlikeLabel.setVisible(false);
        likePostUnclickImageView.setVisible(false);
    }

    public void setFromPostLabel(String fromPostLabel) {
        this.fromPostLabel.setText(fromPostLabel);
    }

    public void setTextPost(String textPost) {
        this.textPost.setText(textPost);
    }
}
