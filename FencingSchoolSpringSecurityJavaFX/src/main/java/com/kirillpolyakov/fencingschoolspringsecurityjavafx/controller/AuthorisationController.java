package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.User;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.UserRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class AuthorisationController {
    @FXML
    public Button buttonSignIn;

    @FXML
    public TextField textFieldLogin;

    @FXML
    public TextField textFieldPassword;

    @FXML
    public Button buttonSignUp;

    private final ObjectMapper mapper = new ObjectMapper();

    {
        mapper.registerModule(new JavaTimeModule());
    }

    @FXML
    public void buttonSignIn(ActionEvent actionEvent) {
        String username = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        if (username.isEmpty() || password.isEmpty()) {
            App.showInfo("Error", "Все поля должны быть заполнены", Alert.AlertType.ERROR);
        } else {
            try {
                User user = new UserRepository(username, password).authenticate();
                Preferences preferences = Preferences.userNodeForPackage(App.class);
                long id = user.getId();
                preferences.putLong(Constants.PREFERENCE_KEY_ID, id);
                preferences.put(Constants.PREFERENCE_KEY_USERNAME, username);
                preferences.put(Constants.PREFERENCE_KEY_PASSWORD, password);
                if (user.getClass().getSimpleName().equals("Admin")) {
                    App.openWindow("mainAdmin.fxml", "Admin", user);
                } else if (user.getClass().getSimpleName().equals("Trainer")) {
                    App.openWindow("mainTrainer.fxml", "Trainer", user);
                } else {
                    App.openWindow("apprentice.fxml", "Apprentice", user);
                }
                Stage stage = (Stage) buttonSignIn.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                App.showInfo("Error", "No connection", Alert.AlertType.ERROR);
            } catch (IllegalArgumentException e) {
                App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void buttonSignUp(ActionEvent actionEvent) {
        try {
            App.openWindow("registration.fxml", "Registration", null);
            Stage stage = (Stage) buttonSignUp.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
