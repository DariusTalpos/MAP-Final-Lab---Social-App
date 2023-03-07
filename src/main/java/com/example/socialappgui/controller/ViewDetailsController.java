package com.example.socialappgui.controller;

import com.example.socialappgui.domain.RequestType;
import com.example.socialappgui.domain.User;
import com.example.socialappgui.repository.UserDBRepo;
import com.example.socialappgui.service.FriendshipService;
import com.example.socialappgui.service.MessageService;
import com.example.socialappgui.service.UserService;
import com.example.socialappgui.validator.UserValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class ViewDetailsController {

    private Long friendshipID;
    private User appUser;

    private Long friendID;
    private TableContent type;
    private FriendshipService friendshipService;
    private MessageService messageService;

    @FXML
    private Button goodButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField fromTextField;
    @FXML
    private TextField toTextField;
    @FXML
    private TextField statusTextField;

    /**
     * initialises the page
     * @param appUser -
     * @param friendName -
     * @param friendID -
     * @param friendshipID -
     * @param status -
     * @param friendshipService -
     * @param tableContent -
     */
    public void setup(User appUser,String friendName,Long friendID,Long friendshipID, String status, FriendshipService friendshipService,TableContent tableContent,MessageService messageService)
    {

        this.appUser = appUser;
        this.friendID=friendID;
        this.friendshipID = friendshipID;
        this.friendshipService = friendshipService;
        this.type = tableContent;
        this.messageService = messageService;

        fromTextField.setText(appUser.getName());
        fromTextField.setEditable(false);
        toTextField.setText(friendName);
        toTextField.setEditable(false);
        statusTextField.setText(status);
        if(Objects.equals(tableContent,TableContent.FRIENDS))
            goodButton.setText("Change");
        else
            goodButton.setText("Send");
    }

    public User getAppUser() {
        return appUser;
    }

    public FriendshipService getFriendshipService() {
        return friendshipService;
    }

    /**
     * method used upon pressing the button on the bottom left
     * @throws IOException -
     */
    @FXML
    public void savePressed() throws IOException
    {
            if(Objects.equals(type,TableContent.OTHERS))
                friendshipService.save(appUser.getID(),friendID,LocalDateTime.now(),statusTextField.getText(), RequestType.PENDING);
            else
                friendshipService.updateStatus(friendshipID,statusTextField.getText());
            cancelPressed();
    }

    /**
     * method used after pressing the bottom right button and after saving/sending
     * @throws IOException -
     */
    @FXML
    public void cancelPressed() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialappgui/mainmenu-controller.fxml"));
        BorderPane root = loader.load();

        MainMenuController ctrl= loader.getController();
        ctrl.setup(getAppUser(),new UserService(new UserDBRepo(new UserValidator(),"jdbc:postgresql://localhost:5432/SocialApp",
                "postgres","postgres")),getFriendshipService(),messageService);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Page");
        stage.show();

        Stage thisStage = (Stage) cancelButton.getScene().getWindow();
        thisStage.close();
    }
}
