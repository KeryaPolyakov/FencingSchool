package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Trainer;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.TrainerRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class AddTrainerController {
    @FXML
    public TextField textFieldSurname;
    @FXML
    public TextField textFieldName;
    @FXML
    public TextField textFieldPatronymic;
    @FXML
    public TextField textFieldExperience;
    @FXML
    public Button buttonAdd;
    @FXML
    public TextField textFieldEmail;
    @FXML
    public TextField textFieldUserName;
    @FXML
    public TextField textFieldPassword;

    @FXML
    public void buttonAdd(ActionEvent actionEvent) {
        String surname = textFieldSurname.getText();
        String name = textFieldName.getText();
        String patronymic = textFieldPatronymic.getText();
        String experience = textFieldExperience.getText();
        String email = textFieldEmail.getText();
        String userName = textFieldUserName.getText();
        String password = textFieldPassword.getText();
        if (surname.isEmpty() || name.isEmpty() || patronymic.isEmpty() || experience.isEmpty() || email.isEmpty()
                || userName.isEmpty() || password.isEmpty()) {
            App.showInfo("Error", "Все поля должны быть заполнены", Alert.AlertType.ERROR);
        } else if (Util.notLetters(surname) || Util.notLetters(name) || Util.notLetters(patronymic)
                || Util.notDigitsOrLetters(userName)) {
            App.showInfo("Error", "Фамилия имя и отчество должны состоять только из букв, " +
                    "логин только из букв и цифп", Alert.AlertType.ERROR);
        } else {
            try {
                Preferences preferences = Preferences.userNodeForPackage(App.class);
                new TrainerRepository(preferences.get("username", ""), preferences.get("password", ""))
                        .post(new Trainer(userName, surname, name, patronymic, password,
                                Integer.parseInt(experience), email));
                App.showInfo("Info", "Тренер успешно добавлен", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) buttonAdd.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                App.showInfo("Error", "Стаж должен состоять только из цифр", Alert.AlertType.ERROR);
            } catch (IllegalArgumentException e) {
                App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}
