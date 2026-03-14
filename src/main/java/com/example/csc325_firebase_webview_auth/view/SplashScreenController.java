package com.example.csc325_firebase_webview_auth.view;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;


public class SplashScreenController {
    @FXML
    private Button registerButton;
    @FXML
    private Button signInButton;
    @FXML
    private Button continueToApp;


    @FXML
    private void handleAppChange() throws IOException {
        App.setRoot("/files/AccessFBView.fxml");
    }

    @FXML
    private void handleRegister(ActionEvent event) throws IOException {
        App.setRoot("/files/Registration.fxml");
    }

    @FXML
    private void handleSignIn(ActionEvent event) throws IOException {
        App.setRoot("/files/SignIn.fxml");
    }
}
