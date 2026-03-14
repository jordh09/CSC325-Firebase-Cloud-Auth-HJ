package com.example.csc325_firebase_webview_auth.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegistrationController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;


    @FXML
    private Button registerButton;
    @FXML
    private Button goToSignInButton;

    @FXML
    private void handleRegister(ActionEvent event) {
        registrationLogic();
    }

    @FXML
    private void handleGoToSignIn(ActionEvent event) throws IOException {
        App.setRoot("/files/SignIn.fxml");
    }


    public void registrationLogic(){
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();





    }
}
