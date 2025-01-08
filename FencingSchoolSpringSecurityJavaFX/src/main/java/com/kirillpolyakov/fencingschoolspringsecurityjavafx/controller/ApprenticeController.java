package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Apprentice;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Training;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.User;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.ApprenticeRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.TrainingRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.UserRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Util;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class ApprenticeController implements ControllerData<Apprentice> {
    @FXML
    public TextField textFieldSurname;
    @FXML
    public TextField textFieldName;
    @FXML
    public TextField textFieldPatronymic;
    @FXML
    public TextField textFieldPhoneNumber;
    @FXML
    public ListView<Training> listViewTrainings;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Button buttonDeleteApprentice;
    @FXML
    public CheckBox checkBoxPassword;
    @FXML
    public TextField textFiledUserName;
    @FXML
    public TextField textFieldPassword;
    @FXML
    public Button buttonExit;
    @FXML
    public Button buttonUpdateApprentice;

    private Apprentice apprentice;

    private TrainingRepository trainingRepository;

    private ApprenticeRepository apprenticeRepository;

    private final Preferences preferences = Preferences.userNodeForPackage(App.class);

    @Override
    public void initData(Apprentice data) {
        anchorPane.requestFocus();
        apprentice = data;
        User user;
        try {
            user = new UserRepository(preferences.get("username", ""),
                    preferences.get("password", "")).authenticate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!user.getClass().getSimpleName().equals("Apprentice")) {
            buttonExit.setVisible(false);
        }
        if (user.getClass().getSimpleName().equals("Trainer")) {
            buttonDeleteApprentice.setVisible(false);
            buttonUpdateApprentice.setVisible(false);
            checkBoxPassword.setVisible(false);
        }
        trainingRepository = new TrainingRepository(preferences.get("username", ""),
                preferences.get("password", ""));
        apprenticeRepository = new ApprenticeRepository(preferences.get("username", ""),
                preferences.get("password", ""));
        try {
            listViewTrainings.setItems(FXCollections.observableList(
                    trainingRepository.getByApprenticeId(apprentice.getId())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        textFieldName.setText(apprentice.getName());
        textFieldSurname.setText(apprentice.getSurname());
        textFieldPatronymic.setText(apprentice.getPatronymic());
        textFieldPhoneNumber.setText(apprentice.getPhoneNumber());
        textFiledUserName.setText(apprentice.getUserName());
        checkBoxPassword.setSelected(false);
        textFieldPassword.setVisible(false);
        checkBoxPassword.setOnAction(actionEvent -> textFieldPassword.setVisible(checkBoxPassword.isSelected()));
    }

    @FXML
    public void buttonAddTraining(ActionEvent actionEvent) {
        try {
            App.openWindowAndWaitModal("addTraining.fxml", "Тренировка", apprentice);
            listViewTrainings.setItems(FXCollections.observableList(
                    trainingRepository.getByApprenticeId(apprentice.getId())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void buttonDeleteTraining(ActionEvent actionEvent) {
        Training training = listViewTrainings.getSelectionModel().getSelectedItem();
        try {
            trainingRepository.delete(training.getId());
            App.showInfo("Информация", "Тренировка успешка удалена", Alert.AlertType.INFORMATION);
            listViewTrainings.setItems(FXCollections.observableList(
                    trainingRepository.getByApprenticeId(apprentice.getId())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            App.showInfo("Информация", "Для удаления необходимо выбрать тренировку из списка",
                    Alert.AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            App.showInfo("Информация", e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void buttonUpdateApprentice(ActionEvent actionEvent) {
        String surname = textFieldSurname.getText();
        String name = textFieldName.getText();
        String patronymic = textFieldPatronymic.getText();
        String phoneNumber = textFieldPhoneNumber.getText();
        String username = textFiledUserName.getText();
        if (surname.isEmpty() || name.isEmpty() || patronymic.isEmpty() || phoneNumber.isEmpty()
                || username.isEmpty()) {
            App.showInfo("Error", "Все поля должны быть заполнены", Alert.AlertType.ERROR);
        } else if (Util.notLetters(surname) || Util.notLetters(name) || Util.notLetters(patronymic) ||
                Util.notDigitsOrLetters(username)) {
            App.showInfo("Error", "Фамилия имя и отчество должны состоять только из букв, " +
                    "логин только из букв или цифр", Alert.AlertType.ERROR);
        } else if (Util.notDigits(phoneNumber)) {
            App.showInfo("Error", "Номер телефона должен состоять только из цифр", Alert.AlertType.ERROR);
        } else {
            Apprentice newApprentice;
            try {
                if (!checkBoxPassword.isSelected()) {
                    newApprentice = new Apprentice(apprentice.getId(), username, surname, name, patronymic, phoneNumber);
                    apprenticeRepository.update(newApprentice);
                    App.showInfo("Info", "Ученик успешно обновлен", Alert.AlertType.INFORMATION);
                    if (preferences.getLong("id", -1) == apprentice.getId()) {
                        preferences.put("username", username);
                        apprenticeRepository = new ApprenticeRepository(preferences.get("username", ""),
                                preferences.get("password", ""));
                        trainingRepository = new TrainingRepository(preferences.get("username", ""),
                                preferences.get("password", ""));
                    }
                } else {
                    String password = textFieldPassword.getText();
                    if (password.isEmpty()) {
                        App.showInfo("Error", "Если вы хотите изменить пароль, " +
                                "поле пароль должно быть заполнено", Alert.AlertType.ERROR);
                    } else {
                        newApprentice = new Apprentice(apprentice.getId(), username, surname, name, patronymic, password,
                                phoneNumber);
                        apprenticeRepository.updateWithPassword(newApprentice);
                        if (preferences.getLong("id", -1) == apprentice.getId()) {
                            preferences.put("password", password);
                            preferences.put("username", username);
                            apprenticeRepository = new ApprenticeRepository(preferences.get("username", ""),
                                    preferences.get("password", ""));
                            trainingRepository = new TrainingRepository(preferences.get("username", ""),
                                    preferences.get("password", ""));
                        }
                        App.showInfo("Info", "Ученик успешно обновлен", Alert.AlertType.INFORMATION);
                    }
                }
            } catch (IllegalArgumentException e) {
                App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void buttonDeleteApprentice(ActionEvent actionEvent) {
        try {
            apprenticeRepository.delete(apprentice.getId());
            if (preferences.getLong("id", -1) == apprentice.getId()) {
                preferences.remove("id");
                preferences.remove("username");
                preferences.remove("password");
                App.showInfo("Info", "Ученик успешно удален", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) buttonDeleteApprentice.getScene().getWindow();
                stage.close();
                App.openWindow("authorisation.fxml", "Authorisation", null);
            } else {
                App.showInfo("Info", "Ученик успешно удален", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) buttonDeleteApprentice.getScene().getWindow();
                stage.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            App.showInfo("Info", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void buttonExit(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) buttonDeleteApprentice.getScene().getWindow();
            stage.close();
            App.openWindow("authorisation.fxml", "Authorisation", null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
