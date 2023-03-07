package com.example.socialappgui.controller;

import com.example.socialappgui.domain.Request;
import com.example.socialappgui.domain.RequestType;
import com.example.socialappgui.domain.User;
import com.example.socialappgui.service.FriendshipService;
import com.example.socialappgui.service.MessageService;
import com.example.socialappgui.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainMenuController {
    User appUser;
    UserService userService;
    FriendshipService friendshipService;
    MessageService messageService;
    ObservableList<Request> model = FXCollections.observableArrayList();
    TableContent state;

    //-------------------------------TOP------------------------------------------
    @FXML
    private Label userLabel;

    @FXML
    private Button buttonChangeAccount;

    //------------------------------BOTTOM----------------------------------------
    @FXML
    private Button buttonCurrentFriends;

    @FXML
    private Button buttonReceivedRequests;

    @FXML
    private Button buttonSentRequests;

    @FXML
    private Button buttonOtherUsers;

    //--------------------------------RIGHT-----------------------------------------
    @FXML
    private TextField searchbar;

    @FXML
    private Button buttonChangeStatus;

    @FXML
    private Button buttonRemoveDeleteDecline;

    @FXML
    private Button buttonAcceptRequest;

    @FXML
    private Button buttonSendRequest;

    @FXML
    private Button chatButton;
    //-------------------------------MIDDLE-----------------------------------------

    @FXML
    private TableView<Request> tableView;
    @FXML
    private TableColumn<Request, String> tableColumnName;
    @FXML
    private TableColumn<Request, LocalDateTime> tableColumnSince;
    @FXML
    private TableColumn<Request, String> tableColumnDescription;

    //----------------------------actual code---------------------------------------

    /**
     * clase made to initialise the main menu of the app
     * @param appUser - the user who is currently logged in
     * @param userService - the user serivce
     * @param friendshipService - the friendship service
     */
    public void setup(User appUser,UserService userService,FriendshipService friendshipService,MessageService messageService)
    {
        this.appUser = appUser;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.state = TableContent.FRIENDS;


        userLabel.setText("Hello, "+appUser.getName()+"!");

        tableColumnName.setCellValueFactory(new PropertyValueFactory<Request, String>("friendName"));
        tableColumnSince.setCellValueFactory(new PropertyValueFactory<Request, LocalDateTime>("friendsSince"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory<Request, String>("description"));
        searchbar.textProperty().addListener(o -> search());

        refresh();
    }

    /**
     * method used to keep the menu up-to-date and show the content desired by the user
     */
    public void refresh()
    {
        if(Objects.equals(state,TableContent.FRIENDS))
        {
            tableColumnName.setPrefWidth(152);
            tableColumnSince.setVisible(true);
            tableColumnDescription.setVisible(true);
            tableColumnSince.setText("Friends since");
            model.setAll(friendshipService.getFriends(appUser.getID()));
            tableView.setItems(model);

            buttonChangeStatus.setVisible(true);
            buttonRemoveDeleteDecline.setVisible(true);
            buttonRemoveDeleteDecline.setText("Remove Friend");
            buttonAcceptRequest.setVisible(false);
            buttonSendRequest.setVisible(false);
            chatButton.setVisible(true);

            resetSearch();
        }
        if(Objects.equals(state,TableContent.RECEIVED))
        {
            tableColumnName.setPrefWidth(152);
            tableColumnSince.setVisible(true);
            tableColumnDescription.setVisible(true);
            tableColumnSince.setText("Requested since");
            model.setAll(friendshipService.getReceivedRequests(appUser.getID()));
            tableView.setItems(model);

            buttonChangeStatus.setVisible(false);
            buttonRemoveDeleteDecline.setVisible(true);
            buttonRemoveDeleteDecline.setText("Decline Request");
            buttonAcceptRequest.setVisible(true);
            buttonSendRequest.setVisible(false);
            chatButton.setVisible(false);

            resetSearch();
        }
        if(Objects.equals(state,TableContent.SENT))
        {
            tableColumnName.setPrefWidth(152);
            tableColumnSince.setVisible(true);
            tableColumnDescription.setVisible(true);
            tableColumnSince.setText("Requested since");
            model.setAll(friendshipService.getSentRequests(appUser.getID()));
            tableView.setItems(model);

            buttonChangeStatus.setVisible(false);
            buttonRemoveDeleteDecline.setVisible(true);
            buttonRemoveDeleteDecline.setText("Delete Request");
            buttonAcceptRequest.setVisible(false);
            buttonSendRequest.setVisible(false);
            chatButton.setVisible(false);

            resetSearch();
        }
        if(Objects.equals(state,TableContent.OTHERS))
        {
            tableColumnName.setPrefWidth(456);
            tableColumnSince.setVisible(false);
            tableColumnDescription.setVisible(false);
            model.setAll(friendshipService.getOthers(appUser.getID()));
            tableView.setItems(model);

            buttonChangeStatus.setVisible(false);
            buttonRemoveDeleteDecline.setVisible(false);
            buttonAcceptRequest.setVisible(false);
            buttonSendRequest.setVisible(true);
            chatButton.setVisible(false);

            resetSearch();
        }
    }

    /**
     * method activated when the user presses the 'Change Account' button
     * @throws IOException - ..idk
     */
    @FXML
    public void changePressed() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialappgui/login-controller.fxml"));
        AnchorPane root = loader.load();

        LoginController ctrl= loader.getController();
        ctrl.setup();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();

        Stage thisStage = (Stage) buttonChangeAccount.getScene().getWindow();
        thisStage.close();
    }

    /**
     * method used to display an error message if no user was chosen when pressing a button
     */
    public void noneSelectedError()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No user selected");
        alert.setContentText("Please select a user!");
        alert.show();
    }

    /**
     * method activated upon pressing the 'Change Status' button
     * @throws IOException -
     */
    @FXML
    public void changeStatus() throws IOException {
        Request request = tableView.getSelectionModel().getSelectedItem();
        if(request ==null)
            noneSelectedError();
        else
        {
            openView(request,TableContent.FRIENDS,buttonChangeStatus);
            refresh();
        }
    }

    /**
     * method used upon pressing any button related to deleting data stored
     */
    @FXML
    public void removeDeleteDecline()
    {
        Request request = tableView.getSelectionModel().getSelectedItem();
        if(request ==null)
            noneSelectedError();
        else
        {
            friendshipService.delete(request.getFriendshipID());
            refresh();
        }
    }

    /**
     * method used upon pressing the 'Accept Request' button
     */
    @FXML
    public void acceptRequest()
    {
        Request request = tableView.getSelectionModel().getSelectedItem();
        if(request ==null)
            noneSelectedError();
        else
        {
            friendshipService.updateRequest(request.getFriendshipID(), RequestType.ACCEPTED);
            refresh();
        }
    }

    /**
     * method used upon pressing the 'Send Request' button
     * @throws IOException -
     */
    @FXML
    public void sendRequest() throws IOException
    {
        Request request = tableView.getSelectionModel().getSelectedItem();
        if(request ==null)
            noneSelectedError();
        else
        {
            openView(request,TableContent.OTHERS,buttonSendRequest);
            refresh();
        }
    }

    @FXML
    public void chatPressed() throws IOException
    {
        Request request = tableView.getSelectionModel().getSelectedItem();
        if(request ==null)
            noneSelectedError();
        else
        {
            openChat(request.getFriendID(),request.getFriendName());
        }
    }

    /**
     * method used upon pressing the 'View Friends' button
     * @throws IOException -
     */
    @FXML
    public void friendsPressed() throws IOException
    {
        attemptChangeTab(TableContent.FRIENDS);
    }

    /**
     * method used upon pressing the 'View Sent Requests' button
     * @throws IOException -
     */
    @FXML
    public void sentRequestsPressed() throws IOException
    {
        attemptChangeTab(TableContent.SENT);
    }

    /**
     * method used upon pressing the 'View Received Requests' button
     * @throws IOException -
     */
    @FXML
    public void receivedRequestsPressed() throws IOException
    {
        attemptChangeTab(TableContent.RECEIVED);
    }

    /**
     * method used upon pressing the 'View Other Users' button
     * @throws IOException -
     */
    @FXML
    public void othersPressed() throws IOException
    {
        attemptChangeTab(TableContent.OTHERS);
    }

    /**
     * tests if the tab isn't already opened
     * @param desired - the desired type of content
     */
    public void attemptChangeTab(TableContent desired)
    {
        if(state==desired)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notice");
            alert.setContentText("You are already viewing this list!");
            alert.show();
        }
        else
        {
            state= desired;
            refresh();
        }
    }

    /**
     * opens the view details page
     * @param r -
     * @param tableContent -
     * @param button -
     * @throws IOException -
     */
    public void openView(Request r,TableContent tableContent,Button button) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialappgui/viewdetails-controller.fxml"));
        AnchorPane root = loader.load();

        ViewDetailsController ctrl = loader.getController();
        ctrl.setup(appUser,r.getFriendName(),r.getFriendID(),r.getFriendshipID(),r.getDescription(),friendshipService,tableContent,messageService);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("View");
        stage.show();

        Stage thisStage = (Stage) button.getScene().getWindow();
        thisStage.close();
    }

    public void openChat(Long friendID,String friendName) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/socialappgui/chat-controller.fxml"));
        AnchorPane root = loader.load();

        ChatController ctrl = loader.getController();
        ctrl.setup(appUser,friendID,friendName,messageService);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Chat - "+friendName);
        stage.show();

        Stage thisStage = (Stage) chatButton.getScene().getWindow();
        thisStage.close();
    }

    /**
     * searches for usernames similar to the word typed in the searchbar
     */
    private void search()
    {
        Predicate<Request> predicate= n-> n.getFriendName().startsWith(searchbar.getText());
        List<Request> data = new ArrayList<>();
        if(Objects.equals(state,TableContent.FRIENDS))
        {
            data = friendshipService.getFriends(appUser.getID());
        }
        if(Objects.equals(state,TableContent.SENT))
        {
            data = friendshipService.getSentRequests(appUser.getID());
        }
        if(Objects.equals(state,TableContent.RECEIVED))
        {
            data = friendshipService.getReceivedRequests(appUser.getID());
        }
        if(Objects.equals(state,TableContent.OTHERS))
        {
            data = friendshipService.getOthers(appUser.getID());
        }
        model.setAll(data
                .stream()
                .filter(predicate)
                .collect(Collectors.toList()));
    }

    /**
     * resets the searchbar
     */
    @FXML
    public void resetSearch()
    {
        searchbar.setText("");
    }
}
