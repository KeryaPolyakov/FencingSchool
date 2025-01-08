package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.User;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class MainAdminController implements ControllerData<User> {

    @FXML
    public Button buttonTrainers;
    @FXML
    public Button buttonApprentice;
    @FXML
    public Button buttonExit;
    @FXML
    public Button buttonAdministrators;
    @FXML
    public Button buttonAccount;

    private User user;

    @Override
    public void initData(User data) {
        user = data;
    }

    @FXML
    public void buttonTrainers(ActionEvent actionEvent) {
        try {
            App.openWindow("trainers.fxml", "Тренеры", user);
            Stage stage = (Stage) buttonTrainers.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void buttonApprentice(ActionEvent actionEvent) {
        try {
            App.openWindow("apprentices.fxml", "Ученики", user);
            Stage stage = (Stage) buttonApprentice.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void buttonAccount(ActionEvent actionEvent) {
        try {
            App.openWindowAndWaitModal("admin.fxml", "аккаунт", user);
            Preferences preferences = Preferences.userNodeForPackage(App.class);
            if (preferences.getLong("id", -1) == -1) {
                Stage stage = (Stage) buttonAccount.getScene().getWindow();
                stage.close();
                App.openWindow("authorisation.fxml", "Authorisation", null);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void buttonExit(ActionEvent actionEvent) {
        try {
            Preferences preferences = Preferences.userNodeForPackage(App.class);
            preferences.remove(Constants.PREFERENCE_KEY_ID);
            preferences.remove(Constants.PREFERENCE_KEY_USERNAME);
            preferences.remove(Constants.PREFERENCE_KEY_PASSWORD);
            App.openWindow("authorisation.fxml", "Authorisation", null);
            Stage stage = (Stage) buttonExit.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void buttonAdministrators(ActionEvent actionEvent) {
        try {
            App.openWindow("admins.fxml", "Admins", user);
            Stage stage = (Stage) buttonAdministrators.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
