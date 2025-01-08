package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Apprentice;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.User;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.ApprenticeRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.UserRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {
    @FXML
    public Button buttonSignUp;
    @FXML
    public Button buttonSignIn;
    @FXML
    public TextField textFieldPassword;
    @FXML
    public TextField textFieldName;
    @FXML
    public TextField textFieldSurname;
    @FXML
    public TextField textFieldPatronymic;
    @FXML
    public TextField textFieldPhoneNumber;
    @FXML
    public TextField textFieldUserName;

    @FXML
    public void buttonSignUp(ActionEvent actionEvent) {
        String surname = textFieldSurname.getText();
        String name = textFieldName.getText();
        String patronymic = textFieldPatronymic.getText();
        String phoneNumber = textFieldPhoneNumber.getText();
        String userName = textFieldUserName.getText();
        String password = textFieldPassword.getText();
        if (surname.isEmpty() || name.isEmpty() || patronymic.isEmpty() || phoneNumber.isEmpty() || userName.isEmpty()
                || password.isEmpty()) {
            App.showInfo("Error", "Все поля должны быть заполнены", Alert.AlertType.ERROR);
        } else if (Util.notLetters(surname) || Util.notLetters(name) || Util.notLetters(patronymic)
                || Util.notDigitsOrLetters(userName)){
            App.showInfo("Error", "Фамилия имя и отчество должны состоять только из букв, " +
                    "логин только из букв и цифр", Alert.AlertType.ERROR);
        } else if (Util.notDigits(phoneNumber)) {
            App.showInfo("Error", "Номер телефона должен состоять только из цифр", Alert.AlertType.ERROR);
        } else {
            try {
                new ApprenticeRepository().post(new Apprentice(userName, surname, name, patronymic, password,phoneNumber));
                App.showInfo("Info", "Ученик успешно добавлен", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) buttonSignUp.getScene().getWindow();
                stage.close();
                App.openWindow("authorisation.fxml", "Authorisation", null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void buttonSignIn(ActionEvent actionEvent) {
        try {
            App.openWindow("authorisation.fxml", "Authorisation", null);
            Stage stage = (Stage) buttonSignIn.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
