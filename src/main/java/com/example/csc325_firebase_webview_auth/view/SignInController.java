package com.example.csc325_firebase_webview_auth.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SignInController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;


    @FXML
    private Button signInButton;
    @FXML
    private Button goToRegisterButton;


    @FXML
    private void handleSignIn(ActionEvent event) {
        signInLogic();
    }

    @FXML
    private void handleGoToRegister(ActionEvent event) throws IOException {
        App.setRoot("/files/Registration.fxml");
    }


    public void signInLogic(){
        String email = emailField.getText();
        String password = passwordField.getText();





    }
}
