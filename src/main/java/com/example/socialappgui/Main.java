package com.example.socialappgui;


import com.example.socialappgui.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException
    {

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("login-controller.fxml"));
        AnchorPane root=loader.load();

        LoginController ctrl = loader.getController();
        ctrl.setup();


        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    public static void main(String[] args) {launch();}
}
