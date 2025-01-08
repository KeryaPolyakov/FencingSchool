package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Admin;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Trainer;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.User;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.AdminRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.prefs.Preferences;

public class AdminsController implements ControllerData<User> {

    @FXML
    public TableView<Admin> tableViewAdmins;
    @FXML
    public Button buttonMainMenu;
    @FXML
    public AnchorPane anchorPane;

    private AdminRepository adminRepository;

    private final Preferences preferences = Preferences.userNodeForPackage(App.class);

    private User user;

    @FXML
    @Override
    public void initData(User data) {
        user = data;
        adminRepository = new AdminRepository(preferences.get("username", ""),
                preferences.get("password", ""));
        TableColumn<Admin, String> surname = new TableColumn<>("Фамилия");
        TableColumn<Admin, LocalTime> name = new TableColumn<>("Имя");
        TableColumn<Admin, LocalTime> patronymic = new TableColumn<>("Отчество");
        TableColumn<Admin, LocalTime> salary = new TableColumn<>("Стаж");
        TableColumn<Admin, LocalTime> email = new TableColumn<>("eMail");
        TableColumn<Admin, LocalTime> regData = new TableColumn<>("Дата регистрации");
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        patronymic.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        regData.setCellValueFactory(new PropertyValueFactory<>("regData"));
        tableViewAdmins.getColumns().addAll(surname, name, patronymic, salary, email, regData);
        try {
            setListOfAdmins();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tableViewAdmins.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Admin admin = tableViewAdmins.getSelectionModel().getSelectedItem();
                try {
                    App.openWindowAndWaitModal("admin.fxml", "Администратор", admin);
                    setListOfAdmins();
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
            App.openWindowAndWaitModal("addAdmin.fxml", "Добавить администратора", null);
            setListOfAdmins();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setListOfAdmins() throws IOException {
        List<Admin> admins = adminRepository.get();
        admins = admins.stream().filter(x -> x.getId() != preferences.getLong("id", -1)).toList();
        tableViewAdmins.setItems(FXCollections.observableList(admins));
    }
}
