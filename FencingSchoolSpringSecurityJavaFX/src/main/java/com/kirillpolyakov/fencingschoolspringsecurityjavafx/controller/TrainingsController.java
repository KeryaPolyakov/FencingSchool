package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Training;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.User;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.TrainingRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class TrainingsController implements ControllerData<User> {
    @FXML
    public ListView<Training> listViewTrainings;
    @FXML
    public Button buttonMainMenu;

    private User user;

    @Override
    public void initData(User data) {
        user = data;
        Preferences preferences = Preferences.userNodeForPackage(App.class);
        try {
            listViewTrainings.setItems(FXCollections.observableList(
                    new TrainingRepository(preferences.get("username", ""),
                    preferences.get("password", "")).getByTrainerId(user.getId())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void buttonMainMenu(ActionEvent actionEvent) {
        try {
            App.openWindow("mainTrainer.fxml", "Главное иеню", user);
            Stage stage = (Stage) buttonMainMenu.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
