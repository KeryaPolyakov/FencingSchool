package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.dto.ResponseResult;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Trainer;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.User;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.TrainerRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.UserRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.util.prefs.Preferences;

public class TrainersController implements ControllerData<User> {
    @FXML
    public TableView<Trainer> tableViewTrainers;
    @FXML
    public Button buttonMainMenu;
    @FXML
    public Button buttonAdd;
    @FXML
    public Button buttonApprentices;

    private User user;

    private ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.registerModule(new JavaTimeModule());
    }

    private TrainerRepository trainerRepository;

    @FXML
    @Override
    public void initData(User data) {
        user = data;
        Preferences preferences = Preferences.userNodeForPackage(App.class);
        trainerRepository = new TrainerRepository(preferences.get("username", ""),
                preferences.get("password", ""));
        TableColumn<Trainer, String> surname = new TableColumn<>("Фамилия");
        TableColumn<Trainer, LocalTime> name = new TableColumn<>("Имя");
        TableColumn<Trainer, LocalTime> patronymic = new TableColumn<>("Отчество");
        TableColumn<Trainer, LocalTime> experience = new TableColumn<>("Стаж");
        TableColumn<Trainer, LocalTime> email = new TableColumn<>("eMail");
        TableColumn<Trainer, LocalTime> regData = new TableColumn<>("Дата регистрации");
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        patronymic.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        experience.setCellValueFactory(new PropertyValueFactory<>("experience"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        regData.setCellValueFactory(new PropertyValueFactory<>("regData"));
        surname.setPrefWidth(135);
        name.setPrefWidth(130);
        patronymic.setPrefWidth(155);
        experience.setPrefWidth(50);
        tableViewTrainers.getColumns().addAll(surname, name, patronymic, experience, email, regData);
        try {
            tableViewTrainers.setItems(FXCollections.observableList(trainerRepository.get()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        tableViewTrainers.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Trainer trainer = tableViewTrainers.getSelectionModel().getSelectedItem();
                try {
                    App.openWindowAndWaitModal("trainer.fxml", "Тренер", trainer);
                    tableViewTrainers.getItems().setAll(trainerRepository.get());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @FXML
    public void buttonMainMenu(ActionEvent actionEvent) {
        try {
            App.openWindow("mainAdmin.fxml", "Главное иеню", user);
            Stage stage = (Stage) buttonMainMenu.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void buttonAdd(ActionEvent actionEvent) {
        try {
            App.openWindowAndWaitModal("addTrainer.fxml", "Добавить тренера", null);
            tableViewTrainers.getItems().setAll(trainerRepository.get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
