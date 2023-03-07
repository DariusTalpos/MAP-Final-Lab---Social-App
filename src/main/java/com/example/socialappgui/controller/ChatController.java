package com.example.socialappgui.controller;

import com.example.socialappgui.domain.Message;
import com.example.socialappgui.domain.User;
import com.example.socialappgui.observer.Observer;
import com.example.socialappgui.repository.FriendshipDBRepo;
import com.example.socialappgui.repository.MessageDBRepo;
import com.example.socialappgui.repository.UserDBRepo;
import com.example.socialappgui.service.FriendshipService;
import com.example.socialappgui.service.MessageService;
import com.example.socialappgui.service.UserService;
import com.example.socialappgui.validator.FriendshipValidator;
import com.example.socialappgui.validator.UserValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Observable;
import java.util.Set;

public class ChatController implements Observer {
    private User appUser;
    private Long friendID;

    private String friendName;

    private MessageService service;

    ObservableList<String> model = FXCollections.observableArrayList();

    @FXML
    private TextField text;

    @FXML
    private Button exitButton;

    @FXML
    private ListView<String> messageList;

    public void setup(User appUser,Long friend, String friendName,MessageService messageService)
    {
        this.service = messageService;
        service.addObserver(this);
        this.appUser = appUser;
        this.friendID = friend;
        this.friendName = friendName;
        model.setAll(service.findExchange(appUser.getID(), friendID));
        messageList.setItems(model);
    }

    public void clearText()
    {
        text.clear();
    }

    public void exit() throws IOException
    {
        service.removeObserver(this);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialappgui/mainmenu-controller.fxml"));
        BorderPane root = loader.load();

        MainMenuController ctrl= loader.getController();
        ctrl.setup(appUser,new UserService(new UserDBRepo(new UserValidator(),"jdbc:postgresql://localhost:5432/SocialApp",
                "postgres","postgres")),new FriendshipService(new FriendshipDBRepo(new FriendshipValidator(),"jdbc:postgresql://localhost:5432/SocialApp",
                "postgres","postgres"),new UserDBRepo(new UserValidator(),"jdbc:postgresql://localhost:5432/SocialApp",
                "postgres","postgres")),service);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Page");
        stage.show();

        Stage thisStage = (Stage) exitButton.getScene().getWindow();
        thisStage.close();
    }

    public void sendPressed()
    {
        if(Objects.equals(text.getText(), ""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notice");
            alert.setContentText("You cannot send a blank message");
            alert.show();
        }
        else
        {
            String message = text.getText();
            service.addMessage(appUser.getID(),friendID, appUser.getName()+": "+message);
            text.clear();
            update();
        }

    }

    @Override
    public void update() {
        model.setAll(service.findExchange(appUser.getID(), friendID));
        messageList.setItems(model);
    }
}
