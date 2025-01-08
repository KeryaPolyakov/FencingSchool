module com.kirillpolyakov.fencingschoolspringsecurityjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires okhttp3;
    requires retrofit2;
    requires retrofit2.converter.jackson;
    requires java.prefs;


    opens com.kirillpolyakov.fencingschoolspringsecurityjavafx to javafx.fxml;
    exports com.kirillpolyakov.fencingschoolspringsecurityjavafx;
    exports com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller to javafx.fxml;
    opens com.kirillpolyakov.fencingschoolspringsecurityjavafx.controller to javafx.fxml;
    exports com.kirillpolyakov.fencingschoolspringsecurityjavafx.dto to com.fasterxml.jackson.databind;
    exports com.kirillpolyakov.fencingschoolspringsecurityjavafx.model to com.fasterxml.jackson.databind;
    opens com.kirillpolyakov.fencingschoolspringsecurityjavafx.model to javafx.base;
}