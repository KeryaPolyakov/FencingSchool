package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Trainer;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.TrainerScheduleRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.util.prefs.Preferences;

public class AddScheduleController implements ControllerData<Trainer> {
    @FXML
    public ComboBox<String> comboBoxDay;
    @FXML
    public ComboBox<String> comboBoxStart;
    @FXML
    public ComboBox<String> comboBoxEnd;

    @FXML
    public Button buttonAdd;

    private Trainer trainer;

    @FXML
    @Override
    public void initData(Trainer data) {
        trainer = data;
        comboBoxDay.getItems().addAll(Constants.SORTED_DAYS);
        comboBoxStart.getItems().addAll(Constants.TIME_START);
        comboBoxEnd.getItems().addAll(Constants.TIME_END);
    }

    @FXML
    public void buttonAdd(ActionEvent actionEvent) {
        try {
            String day = comboBoxDay.getSelectionModel().getSelectedItem();
            LocalTime start = LocalTime.parse(comboBoxStart.getSelectionModel().getSelectedItem());
            LocalTime end = LocalTime.parse(comboBoxEnd.getSelectionModel().getSelectedItem());
            if (start.isAfter(end)) {
                App.showInfo("Error", "Время начала работы не может быть позже времени окончания работы",
                        Alert.AlertType.ERROR);
            } else if (start.plusMinutes(Constants.MINUTES).isAfter(end)) {
                App.showInfo("Error", "Недостаточно времени для одной тренировки (90 минут)",
                        Alert.AlertType.ERROR);
            } else {
                try {
                    Preferences preferences = Preferences.userNodeForPackage(App.class);
                    new TrainerScheduleRepository(preferences.get("username", ""),
                            preferences.get("password", ""))
                            .post(trainer.getId(), Constants.DAYS.get(day), start, end);
                    App.showInfo("Информация", "Расписание успешно добавлено",
                            Alert.AlertType.INFORMATION);
                    Stage stage = (Stage) buttonAdd.getScene().getWindow();
                    stage.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (NullPointerException e) {
            App.showInfo("Error", "Поля должны быть заполнены", Alert.AlertType.ERROR);
        }
    }
}
