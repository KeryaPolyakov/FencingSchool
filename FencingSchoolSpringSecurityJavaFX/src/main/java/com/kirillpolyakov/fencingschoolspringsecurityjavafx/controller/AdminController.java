package com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller;

import com.kirillpolyakov.fencingschoolspringsecurityjavafx.App;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.model.Admin;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.retrofit.AdminRepository;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class AdminController implements ControllerData<Admin> {

    @FXML
    public Button buttonDelete;
    @FXML
    public TextField textFieldSurname;
    @FXML
    public TextField textFieldName;
    @FXML
    public TextField textFieldPatronymic;
    @FXML
    public TextField textFieldEmail;
    @FXML
    public TextField textFieldSalary;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Button buttonUpdate;
    @FXML
    public TextField textFieldUserName;
    @FXML
    public CheckBox checkBoxPassword;
    @FXML
    public TextField textFieldPassword;

    private AdminRepository adminRepository;

    private final Preferences preferences = Preferences.userNodeForPackage(App.class);

    private Admin admin;

    @FXML
    @Override
    public void initData(Admin data) {
        admin = data;
        anchorPane.requestFocus();
        adminRepository = new AdminRepository(preferences.get("username", ""),
                preferences.get("password", ""));
        textFieldName.setText(admin.getName());
        textFieldSurname.setText(admin.getSurname());
        textFieldPatronymic.setText(admin.getPatronymic());
        textFieldEmail.setText(admin.getEmail());
        textFieldSalary.setText(String.valueOf(admin.getSalary()));
        textFieldUserName.setText(admin.getUserName());
        textFieldPassword.setVisible(false);
        checkBoxPassword.setSelected(false);
        checkBoxPassword.setOnAction(action -> textFieldPassword.setVisible(checkBoxPassword.isSelected()));
    }

    @FXML
    public void buttonUpdate(ActionEvent actionEvent) {
        String surname = textFieldSurname.getText();
        String name = textFieldName.getText();
        String patronymic = textFieldPatronymic.getText();
        String salary = textFieldSalary.getText();
        String email = textFieldEmail.getText();
        String userName = textFieldUserName.getText();
        if (surname.isEmpty() || name.isEmpty() || patronymic.isEmpty() || salary.isEmpty() || email.isEmpty()
                || userName.isEmpty()) {
            App.showInfo("Error", "Все поля должны быть заполнены", Alert.AlertType.ERROR);
        } else if (Util.notLetters(surname) || Util.notLetters(name) || Util.notLetters(patronymic)
                || Util.notDigitsOrLetters(userName)) {
            App.showInfo("Error", "Фамилия имя и отчество должны состоять только из букв, " +
                    "логин только из букв и цифр", Alert.AlertType.ERROR);
        } else {
            try {
                Admin newAdmin;
                if (!checkBoxPassword.isSelected()) {
                    newAdmin = new Admin(admin.getId(), userName, surname, name, patronymic,
                            email, Integer.parseInt(salary));
                    adminRepository.update(newAdmin);
                    App.showInfo("Info", "Администратор успешно обновлен", Alert.AlertType.INFORMATION);
                    if (preferences.getLong("id", -1) == admin.getId()) {
                        preferences.put("username", userName);
                        adminRepository = new AdminRepository(preferences.get("username", ""),
                                preferences.get("password", ""));
                    } else {
                        Stage stage = (Stage) buttonUpdate.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    String password = textFieldPassword.getText();
                    if (password.isEmpty()) {
                        App.showInfo("Error", "Если вы хотите изменить пароль поле должно быть заполнено",
                                Alert.AlertType.ERROR);
                    } else {
                        newAdmin = new Admin(admin.getId(), userName, surname, name, patronymic, password,
                                email, Integer.parseInt(salary));
                        adminRepository.updateWithPassword(newAdmin);
                        App.showInfo("Info", "Администратор успешно обновлен", Alert.AlertType.INFORMATION);
                        if (preferences.getLong("id", -1) == admin.getId()) {
                            preferences.put("password", password);
                            preferences.put("username", userName);
                            adminRepository = new AdminRepository(preferences.get("username", ""),
                                    preferences.get("password", ""));
                        } else {
                            Stage stage = (Stage) buttonUpdate.getScene().getWindow();
                            stage.close();
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                App.showInfo("Error", "Зарплата должна состоять только из цифр", Alert.AlertType.ERROR);
            } catch (IllegalArgumentException e) {
                App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void buttonDelete(ActionEvent actionEvent) {
        try {
            adminRepository.delete(admin.getId());
            if (preferences.getLong("id", -1) == admin.getId()) {
                preferences.remove("id");
                preferences.remove("username");
                preferences.remove("password");
                App.showInfo("Info", "Администратор успешно удален", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) buttonDelete.getScene().getWindow();
                stage.close();
            } else {
                App.showInfo("Info", "Администратор успешно удален", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) buttonDelete.getScene().getWindow();
                stage.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
