package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.*;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.TrainerRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.TrainerScheduleRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.TrainingRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.UserRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Constants;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.prefs.Preferences;


public class AddTrainingController implements ControllerData<Apprentice> {
    @FXML
    public ComboBox<Trainer> comboBoxTrainer;
    @FXML
    public DatePicker datePickerData;
    @FXML
    public ComboBox<String> comboBoxTime;
    @FXML
    public TableView<TrainerScheduleItem> tableViewSchedule;
    @FXML
    public Button buttonAddTraining;
    @FXML
    public ComboBox<Integer> comboBoxNumberGym;

    private List<String> trainerWorkDay;

    private List<LocalDate> trainingsDates;

    private Apprentice apprentice;

    private Trainer trainer;

    private TrainerScheduleRepository trainerScheduleRepository;

    private TrainingRepository trainingRepository;

    private TrainerScheduleItem trainerScheduleItem;

    private final Preferences preferences = Preferences.userNodeForPackage(App.class);

    @Override
    public void initData(Apprentice data) {
        apprentice = data;
        try {
            User user = new UserRepository(preferences.get("username", ""),
                    preferences.get("password", "")).authenticate();
            comboBoxNumberGym.getItems().setAll(Constants.NUMBERS_GYM);
            comboBoxNumberGym.setDisable(true);
            datePickerData.setDisable(true);
            comboBoxTime.setDisable(true);
            trainerScheduleRepository = new TrainerScheduleRepository(preferences.get("username", ""),
                    preferences.get("password", ""));
            trainingRepository = new TrainingRepository(preferences.get("username", ""),
                    preferences.get("password", ""));
            TrainerRepository trainerRepository = new TrainerRepository(preferences.get("username", ""),
                    preferences.get("password", ""));

            if (user.getClass().getSimpleName().equals("Trainer")) {
                comboBoxTrainer.getItems().add((Trainer) user);
            } else {
                List<Long> trainerSchedules = trainerScheduleRepository.get()
                        .stream().map(TrainerSchedule::getId).toList();
                comboBoxTrainer.getItems().addAll(trainerRepository.get()
                        .stream().filter(x -> trainerSchedules.contains(x.getId())).toList());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        datePickerData.setOnAction(actionEvent ->
        {
            comboBoxNumberGym.setDisable(false);
            comboBoxNumberGym.setValue(null);
            comboBoxTime.setDisable(true);
            comboBoxTime.setValue(null);
        });

        comboBoxNumberGym.setOnAction(actionEvent ->
        {
            if (datePickerData.getValue() != null) {
                String[] split = datePickerData.getValue().toString().split("-");
                StringJoiner stringJoiner = new StringJoiner(".");
                for (int i = split.length - 1; i >= 0; i--) {
                    stringJoiner.add(split[i]);
                }
                comboBoxTime.setDisable(false);
                try {
                    if (comboBoxNumberGym.getSelectionModel().getSelectedItem() != null) {
                        comboBoxTime.getItems().setAll(
                                trainingRepository.getFreeTime(trainer.getId(),
                                        stringJoiner.toString(),
                                        comboBoxNumberGym.getSelectionModel().getSelectedItem()));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        datePickerData.setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        try {
                            if (item != null && trainingsDates.contains(item)) {
                                this.setDisable(true);
                                this.setStyle("-fx-background-color: lightgrey;");
                            } else if (item != null && item.isBefore(LocalDate.now())
                                    || !trainerWorkDay.contains(Objects.requireNonNull(item)
                                    .getDayOfWeek().toString().toLowerCase()) || LocalDate.now().equals(item)
                                    && LocalTime.now().isAfter(Objects.requireNonNull(trainerScheduleItem).getTimeEnd())
                            ) {
                                this.setDisable(true);
                                this.setStyle("-fx-background-color: fac7c3;");
                            }
                        } catch (NullPointerException ignored) {
                        }
                    }
                };
            }
        });

        TableColumn<TrainerScheduleItem, String> day = new TableColumn<>("День недели");
        TableColumn<TrainerScheduleItem, LocalTime> startTime = new TableColumn<>("Время начала работы");
        TableColumn<TrainerScheduleItem, LocalTime> endTime = new TableColumn<>("Время окончания работы");
        day.setCellValueFactory(new PropertyValueFactory<>("rusDay"));
        day.setPrefWidth(115);
        startTime.setCellValueFactory(new PropertyValueFactory<>("timeStart"));
        startTime.setPrefWidth(140);
        endTime.setCellValueFactory(new PropertyValueFactory<>("timeEnd"));
        endTime.setPrefWidth(160);
        tableViewSchedule.getColumns().addAll(day, startTime, endTime);

        comboBoxTrainer.setOnAction(actionEvent -> {
            try {
                datePickerData.setDisable(false);
                datePickerData.setValue(null);
                comboBoxNumberGym.setValue(null);
                comboBoxNumberGym.setDisable(true);
                comboBoxTime.setValue(null);
                comboBoxTime.setDisable(true);
                trainer = comboBoxTrainer.getSelectionModel().getSelectedItem();
                tableViewSchedule.setItems(FXCollections.observableList(trainerScheduleRepository
                        .get(trainer.getId()).get()));
                trainerScheduleItem = tableViewSchedule.getItems().stream()
                        .filter(x -> x.engDay.equals(LocalDate.now().getDayOfWeek().toString().toLowerCase()))
                        .findFirst()
                        .orElse(null);
                trainingsDates = trainingRepository.getByApprenticeId(apprentice.getId())
                        .stream().map(Training::getDate).toList();
                trainerWorkDay = trainerScheduleRepository
                        .get(comboBoxTrainer.getSelectionModel().getSelectedItem().getId()).get()
                        .stream().map(TrainerScheduleItem::getEngDay).toList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                tableViewSchedule.getItems().clear();
            }
        });
    }

    public void buttonAddTraining(ActionEvent actionEvent) {
        Trainer trainer = comboBoxTrainer.getSelectionModel().getSelectedItem();
        LocalDate date = datePickerData.getValue();
        if (trainer == null) {
            App.showInfo("Error", "Не выбран тренер", Alert.AlertType.ERROR);
        } else if (date == null) {
            App.showInfo("Error", "Не выбрана дата", Alert.AlertType.ERROR);
        } else if (comboBoxNumberGym.getSelectionModel().getSelectedItem() == null) {
            App.showInfo("Error", "Не выбран номер зала", Alert.AlertType.ERROR);
        } else {
            try {
                int numberGym = comboBoxNumberGym.getSelectionModel().getSelectedItem();
                LocalTime time = LocalTime.parse(comboBoxTime.getSelectionModel().getSelectedItem());
                new TrainingRepository(preferences.get("username", ""),
                        preferences.get("password", ""))
                        .post(new Training(numberGym, trainer, apprentice, date, time), apprentice.getId(),
                                trainer.getId());
                App.showInfo("Информация", "Тренировка успешно добавлена", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) buttonAddTraining.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                App.showInfo("Error", "Номер зала должен состоять из цифр", Alert.AlertType.ERROR);
            } catch (IllegalArgumentException e) {
                App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
            } catch (NullPointerException e) {
                App.showInfo("Error", "Не выбрано время тренировки", Alert.AlertType.ERROR);
            }
        }
    }
}
