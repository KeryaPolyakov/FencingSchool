package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Trainer;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.TrainerScheduleItem;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.User;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.TrainerRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.TrainerScheduleRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.UserRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Util;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.util.prefs.Preferences;

public class TrainerController implements ControllerData<Trainer> {

    @FXML
    public Button buttonDeleteTrainer;
    @FXML
    public TextField textFieldSurname;
    @FXML
    public TextField textFieldName;
    @FXML
    public TextField textFieldPatronymic;
    @FXML
    public TextField textFieldExperience;
    @FXML
    public TableView<TrainerScheduleItem> tableViewTrainerSchedule;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public TextField textFieldEmail;
    @FXML
    public CheckBox checkBoxPassword;
    @FXML
    public TextField textFieldUsername;
    @FXML
    public TextField textFieldPassword;

    private TrainerScheduleRepository trainerScheduleRepository;

    private TrainerRepository trainerRepository;

    private final Preferences preferences = Preferences.userNodeForPackage(App.class);

    private Trainer trainer;

    @FXML
    @Override
    public void initData(Trainer data) {
        anchorPane.requestFocus();
        trainer = data;
        trainerScheduleRepository = new TrainerScheduleRepository(preferences.get("username", ""),
                preferences.get("password", ""));
        trainerRepository = new TrainerRepository(preferences.get("username", ""),
                preferences.get("password", ""));
        textFieldName.setText(trainer.getName());
        textFieldSurname.setText(trainer.getSurname());
        textFieldPatronymic.setText(trainer.getPatronymic());
        textFieldExperience.setText(String.valueOf(trainer.getExperience()));
        textFieldEmail.setText(trainer.getEmail());
        textFieldUsername.setText(trainer.getUserName());
        textFieldPassword.setVisible(false);
        checkBoxPassword.setSelected(false);
        checkBoxPassword.setOnAction(action -> textFieldPassword.setVisible(checkBoxPassword.isSelected()));
        TableColumn<TrainerScheduleItem, String> day = new TableColumn<>("День недели");
        TableColumn<TrainerScheduleItem, LocalTime> startTime = new TableColumn<>("Время начала работы");
        TableColumn<TrainerScheduleItem, LocalTime> endTime = new TableColumn<>("Время окончания работы");
        day.setCellValueFactory(new PropertyValueFactory<>("rusDay"));
        day.setPrefWidth(122);
        startTime.setCellValueFactory(new PropertyValueFactory<>("timeStart"));
        startTime.setPrefWidth(140);
        endTime.setCellValueFactory(new PropertyValueFactory<>("timeEnd"));
        endTime.setPrefWidth(160);
        tableViewTrainerSchedule.getColumns().addAll(day, startTime, endTime);
        try {
            tableViewTrainerSchedule.setItems(FXCollections.observableList(trainerScheduleRepository
                    .get(trainer.getId()).get()).sorted());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException ignored) {
        }
    }

    public void buttonUpdateTrainer(ActionEvent actionEvent) {
        String surname = textFieldSurname.getText();
        String name = textFieldName.getText();
        String patronymic = textFieldPatronymic.getText();
        String experience = textFieldExperience.getText();
        String email = textFieldEmail.getText();
        String username = textFieldUsername.getText();
        if (surname.isEmpty() || name.isEmpty() || patronymic.isEmpty() || experience.isEmpty() || email.isEmpty()
                || username.isEmpty()) {
            App.showInfo("Error", "Все поля должны быть заполнены", Alert.AlertType.ERROR);
        } else if (Util.notLetters(surname) || Util.notLetters(name) || Util.notLetters(patronymic)
                || Util.notDigitsOrLetters(username)) {
            App.showInfo("Error", "Фамилия имя и отчество должны состоять только из букв, " +
                    "логин только из букв и цифр", Alert.AlertType.ERROR);
        } else {
            Trainer newTrainer;
            try {
                if (!checkBoxPassword.isSelected()) {
                    newTrainer = new Trainer(trainer.getId(), username, surname, name, patronymic,
                            Integer.parseInt(experience), email);
                    trainerRepository.update(newTrainer);
                    if (preferences.getLong("id", -1) == trainer.getId()) {
                        preferences.put("username", username);
                        trainerScheduleRepository = new TrainerScheduleRepository(preferences.get("username", ""),
                                preferences.get("password", ""));
                        trainerRepository = new TrainerRepository(preferences.get("username", ""),
                                preferences.get("password", ""));
                    }
                    App.showInfo("Info", "Тренер успешно обновлен", Alert.AlertType.INFORMATION);
                } else {
                    String password = textFieldPassword.getText();
                    if (password.isEmpty()) {
                        App.showInfo("Error", "Если вы хотите изменить пароль, " +
                                "поле пароль должно быть заполнено", Alert.AlertType.ERROR);
                    } else {
                        newTrainer = new Trainer(trainer.getId(), username, surname, name, patronymic, password,
                                Integer.parseInt(experience), email);
                        trainerRepository.updateWithPassword(newTrainer);
                        if (preferences.getLong("id", -1) == trainer.getId()) {
                            preferences.put("password", password);
                            preferences.put("username", username);
                            trainerScheduleRepository = new TrainerScheduleRepository(preferences.get("username", ""),
                                    preferences.get("password", ""));
                            trainerRepository = new TrainerRepository(preferences.get("username", ""),
                                    preferences.get("password", ""));
                        }
                        App.showInfo("Info", "Тренер успешно обновлен", Alert.AlertType.INFORMATION);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                App.showInfo("Error", "Стаж должен состоять только из цифр", Alert.AlertType.ERROR);
            } catch (IllegalArgumentException e) {
                App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void buttonDeleteTrainer(ActionEvent actionEvent) {
        try {
            trainerRepository.delete(trainer.getId());
            if (preferences.getLong("id", -1) == trainer.getId()) {
                preferences.remove("id");
                preferences.remove("username");
                preferences.remove("password");
                App.showInfo("Info", "Тренер успешно удален", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) buttonDeleteTrainer.getScene().getWindow();
                stage.close();
            } else {
                App.showInfo("Info", "Тренер успешно удален", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) buttonDeleteTrainer.getScene().getWindow();
                stage.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void buttonAddSchedule(ActionEvent actionEvent) {
        try {
            App.openWindowAndWaitModal("addSchedule.fxml", "Расписание", trainer);
            tableViewTrainerSchedule.setItems(FXCollections.observableList(trainerScheduleRepository
                    .get(trainer.getId()).get()).sorted());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void buttonDeleteSchedule(ActionEvent actionEvent) {
        try {
            TrainerScheduleItem trainerScheduleItem = tableViewTrainerSchedule.getSelectionModel().getSelectedItem();
            trainerScheduleRepository.delete(trainer.getId(), trainerScheduleItem.engDay);
            tableViewTrainerSchedule.setItems(FXCollections.observableList(trainerScheduleRepository
                    .get(trainer.getId()).get()).sorted());
            App.showInfo("Информация", "Расписание успешно изменено", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            App.showInfo("Информация", "Для удаления нужно выбрать строку расписания",
                    Alert.AlertType.INFORMATION);
        }
    }
}
