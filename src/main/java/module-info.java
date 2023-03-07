module com.example.socialappgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.socialappgui to javafx.fxml;
    exports com.example.socialappgui;

    opens com.example.socialappgui.controller to javafx.fxml;
    exports com.example.socialappgui.controller;

    opens com.example.socialappgui.domain to javafx.fxml;
    exports com.example.socialappgui.domain;

    opens com.example.socialappgui.service to javafx.fxml;
    exports com.example.socialappgui.service;
    exports com.example.socialappgui.repository;
    opens com.example.socialappgui.repository to javafx.fxml;
}