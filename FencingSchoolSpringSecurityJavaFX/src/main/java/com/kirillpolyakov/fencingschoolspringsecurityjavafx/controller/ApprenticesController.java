package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Apprentice;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.User;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.ApprenticeRepository;
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

public class ApprenticesController implements ControllerData<User> {
    @FXML
    public TableView<Apprentice> tableViewApprentice;
    @FXML
    public Button buttonAdd;
    @FXML
    public Button buttonMainMenu;

    private ApprenticeRepository apprenticeRepository;

    private User user;

    private Stage stage;

    @FXML
    @Override
    public void initData(User data) {
        user = data;
        stage = (Stage) tableViewApprentice.getScene().getWindow();
        Preferences preferences = Preferences.userNodeForPackage(App.class);
        apprenticeRepository = new ApprenticeRepository(preferences.get("username", ""),
                preferences.get("password", ""));
        TableColumn<Apprentice, String> surname = new TableColumn<>("Фамилия");
        TableColumn<Apprentice, LocalTime> name = new TableColumn<>("Имя");
        TableColumn<Apprentice, LocalTime> patronymic = new TableColumn<>("Отчество");
        TableColumn<Apprentice, LocalTime> phoneNumber = new TableColumn<>("Номер телефона");
        TableColumn<Apprentice, LocalTime> regData = new TableColumn<>("Дата регистрации");
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        patronymic.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        regData.setCellValueFactory(new PropertyValueFactory<>("regData"));
        surname.setPrefWidth(120);
        name.setPrefWidth(115);
        patronymic.setPrefWidth(115);
        phoneNumber.setPrefWidth(120);
        tableViewApprentice.getColumns().addAll(surname, name, patronymic, phoneNumber, regData);
        try {
            tableViewApprentice.setItems(FXCollections.observableList(apprenticeRepository.get()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tableViewApprentice.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Apprentice apprentice = tableViewApprentice.getSelectionModel().getSelectedItem();
                try {
                    App.openWindowAndWaitModal("apprentice.fxml", "Ученик", apprentice);
                    if (preferences.getLong("id", -1) != -1) {
                        tableViewApprentice.getItems().setAll(apprenticeRepository.get());
                    } else {
                        stage.close();
                    }
                    tableViewApprentice.getItems().setAll(apprenticeRepository.get());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @FXML
    public void buttonAdd(ActionEvent actionEvent) {
        try {
            App.openWindowAndWaitModal("addApprentice.fxml", "Добавить ученика", null);
            tableViewApprentice.getItems().setAll(apprenticeRepository.get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void buttonMainMenu(ActionEvent actionEvent) {
        try {
            if (user.getClass().getSimpleName().equals("Admin")) {
                App.openWindow("mainAdmin.fxml", "Главное меню", user);
            } else if (user.getClass().getSimpleName().equals("Trainer")) {
                App.openWindow("mainTrainer.fxml", "Главное меню", user);
            } else {
                App.openWindow("mainApprentice.fxml", "Главное меню", user);
            }
            Stage stage = (Stage) buttonMainMenu.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
