package com.example.socialappgui.controller;

import com.example.socialappgui.Main;
import com.example.socialappgui.domain.User;
import com.example.socialappgui.repository.FriendshipDBRepo;
import com.example.socialappgui.repository.MessageDBRepo;
import com.example.socialappgui.repository.UserDBRepo;
import com.example.socialappgui.service.FriendshipService;
import com.example.socialappgui.service.MessageService;
import com.example.socialappgui.service.UserService;
import com.example.socialappgui.validator.FriendshipValidator;
import com.example.socialappgui.validator.UserValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * class made for the login screen
 */
public class LoginController {

    User appUser;
    UserService userService;

    MessageService messageService;

    @FXML
    private Button logInButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label passwordLabel;

    /**
     * checks if the entered username is stored in the database and allows entry based on that
     * @throws IOException - ...i have no idea when
     */
    @FXML
    private void loginPressed() throws IOException
    {
        String userName = usernameField.getText();
        appUser = obtainUser(userName);
        if(appUser==null)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
                alert.setHeaderText("Invalid username");
            alert.setContentText("Please try again!");
            alert.show();
            return;
        }

        usernameField.clear();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialappgui/mainmenu-controller.fxml"));
        BorderPane root = loader.load();

        MainMenuController ctrl= loader.getController();
        ctrl.setup(getAppUser(),getUserService(),new FriendshipService(new FriendshipDBRepo(new FriendshipValidator(),"jdbc:postgresql://localhost:5432/SocialApp",
                "postgres","postgres"),new UserDBRepo(new UserValidator(),"jdbc:postgresql://localhost:5432/SocialApp",
                "postgres","postgres")),messageService);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Page");
        stage.show();

        //Stage thisStage = (Stage) logInButton.getScene().getWindow();
        //thisStage.close();
    }

    /**
     * attempts to return the user object from the database
     * @param userName - the name of the desired user
     * @return - the user, if they exist
     *         - null, otherwise
     */
    protected User obtainUser(String userName)
    {
        Iterable<User> users = userService.getUserList();
        for (User user: users)
        {
            if(Objects.equals(user.getName(),userName))
                return user;
        }
        return null;
    }

    /**
     * method used to initialise the login controller
     */
    public void setup()
    {
        this.userService = new UserService(new UserDBRepo(new UserValidator(),"jdbc:postgresql://localhost:5432/SocialApp",
                "postgres","postgres"));
        this.messageService = new MessageService(new MessageDBRepo("jdbc:postgresql://localhost:5432/SocialApp",
                "postgres","postgres"));

        passwordLabel.setVisible(false);
        passwordField.setVisible(false);
        createAccountButton.setVisible(false);
    }

    public User getAppUser() {
        return appUser;
    }

    public UserService getUserService() {
        return userService;
    }
}
