/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_della;

import static java.lang.System.exit;
import java.net.URL;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Chotu
 */
public class FXMLDocumentController implements Initializable {

    ObservableList<String> itemsList = FXCollections.observableArrayList();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();
    Date dateobj = cal.getTime();

    @FXML
    private TextArea itemDesc, itemResolution;

    @FXML
    private TextField itemName, dueDate;

    @FXML
    private Label currentDate;

    @FXML
    private ComboBox<String> actionItemCombo = new ComboBox<String>(itemsList);

    @FXML
    private void updateAction(ActionEvent event) {
        try {
            Connector conn = new Connector();
            conn.myStmt = conn.myConn.createStatement();
            String name = itemName.getText();
            String desc = itemDesc.getText();
            String resolution = itemResolution.getText();
            String duedate = dueDate.getText();
            String selectedItem = (String) actionItemCombo.getSelectionModel().getSelectedItem();
            conn.myStmt.executeUpdate("delete from item_sample where name = '" + selectedItem + "';");
            String sql = "insert into item_sample"
                    + "(name, description, resolution, duedate)"
                    + "values('" + name + "','" + desc + "','" + resolution + "','" + duedate + "')";
            conn.myStmt.executeUpdate(sql);
            refresh();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @FXML
    private void clearFormAction(ActionEvent event) {
        itemName.setText("");
        itemDesc.setText("");
        itemResolution.setText("");
        dueDate.setText("");
    }

    @FXML
    private void createActionItem(ActionEvent event) {
        try {
            String name = itemName.getText();
            String desc = itemDesc.getText();
            String resolution = itemResolution.getText();
            String duedate = dueDate.getText();
            Connector conn = new Connector();
            conn.myStmt = conn.myConn.createStatement();
            String sql = "insert into item_sample"
                    + "(name, description, resolution, duedate)"
                    + "values('" + name + "','" + desc + "','" + resolution + "','" + duedate + "')";
            conn.myStmt.executeUpdate(sql);
            System.out.println("Item created");
        } catch (Exception e) {
            System.out.println(e);
        }
//        actionItemCombo.setItems(itemsList);
        refresh();
    }

    @FXML
    private void deleteActionItem(ActionEvent event) {
        try {
            Connector conn = new Connector();
            conn.myStmt = conn.myConn.createStatement();
            String selectedItem = (String) actionItemCombo.getSelectionModel().getSelectedItem();
            conn.myStmt.executeUpdate("delete from item_sample where name = '" + selectedItem + "';");
            refresh();
            itemName.setText("");
            itemDesc.setText("");
            itemResolution.setText("");
            dueDate.setText("");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void onActionCombo(ActionEvent event) {
        try {
            Connector conn = new Connector();
            conn.myStmt = conn.myConn.createStatement();

            ResultSet myRs = conn.myStmt.executeQuery("Select * from item_sample");
            String selectedItem = (String) actionItemCombo.getSelectionModel().getSelectedItem();
            if (selectedItem.equals("")) {
                itemName.setText("");
                itemDesc.setText("");
                itemResolution.setText("");
                dueDate.setText("");
            } else {
                while (myRs.next()) {
//                itemsList.add(myRs.getString("name"));
                    if (selectedItem.equals(myRs.getString("name"))) {
                        itemName.setText(myRs.getString("name"));
                        itemDesc.setText(myRs.getString("description"));
                        itemResolution.setText(myRs.getString("resolution"));
                        dueDate.setText(myRs.getString("duedate"));
                    }
                }
            }
//            actionItemCombo.setItems(itemsList);
//            System.out.println("inserted");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void refresh() {
        try {
            Connector conn = new Connector();
            conn.myStmt = conn.myConn.createStatement();
            ResultSet myRs = conn.myStmt.executeQuery("Select * from item_sample");
            itemsList = FXCollections.observableArrayList();
            itemsList.add("");
            while (myRs.next()) {
                itemsList.add(myRs.getString("name"));
            }
            actionItemCombo.setItems(itemsList);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void quitAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        String s = "A Quit has been requested and there are updated Action Items that have not been saved!";
        alert.setHeaderText(s);
        alert.setContentText("Do you want to save these Action Items? Click Ok to save or Click Cancel to ignore Changes");
        Optional<ButtonType> result = alert.showAndWait();
        try {
            Connector connection = new Connector();
            connection.myStmt = connection.myConn.createStatement();
            if (result.get() == ButtonType.OK) {
                connection.myStmt.executeUpdate("truncate items");
                connection.myStmt.executeUpdate("insert into items select * from item_sample;");
                connection.myStmt.executeUpdate("truncate item_sample");
                exit(0);
            } else {
                connection.myStmt.executeUpdate("truncate item_sample");
                exit(0);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @FXML
    public Label onlineLabel1, onlineLabel2, onlineLabel3, onlineLabel4;
    public Label offlineLabel1, offlineLabel2, offlineLabel3, offlineLabel4;
//    offlineLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            Connector conn = new Connector();
//            conn.myStmt = conn.myConn.createStatement();
            conn.myStmt.executeUpdate("insert into item_sample select * from items;");
            ResultSet myRs = conn.myStmt.executeQuery("Select * from item_sample");
            itemsList = FXCollections.observableArrayList();
            while (myRs.next()) {
                itemsList.add(myRs.getString("name"));
                consoleList.add(myRs.getString("name"));
            }
            actionItemCombo.setItems(itemsList);
//            for (int i = 0; i < consoleList.size(); i ++ ){
//                consoleItems.setText(consoleList.get(i));
//            }
            currentDate.setText(format.format(dateobj));
            getMembers();
            getTeams();
            boolean status = OnlineStatus.checkStatus();
            if (status == true) {
                offlineLabel1.setText("");
                offlineLabel2.setText("");
                offlineLabel3.setText("");
                offlineLabel4.setText("");
            } else if (status == false) {
                onlineLabel1.setText("");
                onlineLabel2.setText("");
                onlineLabel3.setText("");
                onlineLabel4.setText("");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /*
    the below code is for console
     */
    ObservableList<String> consoleList = FXCollections.observableArrayList();
    /*
    this is for members    
     */
    @FXML
    private TextField member;
    @FXML
    private ListView membersView;

    ObservableList<String> membersList = FXCollections.observableArrayList();

    @FXML
    public void addToList_mem(ActionEvent event) {
        String memb = member.getText();
        try {
            Connector conn = new Connector();
            String sql = "insert into members"
                    + "(member)"
                    + "values('" + memb + "');";
            conn.myStmt.executeUpdate(sql);
            getMembers();
            member.setText("");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @FXML
    public void removeMember(ActionEvent event) {
        String selectedMember = (String) membersView.getSelectionModel().getSelectedItem();
        try {
            Connector conn = new Connector();
            String sql = "delete from members where member= '" + selectedMember + "';";
            conn.myStmt.executeUpdate(sql);
            getMembers();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    ObservableList<String> availTeams = FXCollections.observableArrayList();
    ObservableList<String> currTeams = FXCollections.observableArrayList();
    String selectedMem = "";

    @FXML
    private ListView availableTeams;
    @FXML
    private ListView currentTeams;

    @FXML
    public void onActionMember() throws Exception {
        selectedMem = (String) membersView.getSelectionModel().getSelectedItem();
        Connector conn = new Connector();
        String sql = "SELECT team FROM teams where team  not in (select teams from teams_members where members ='" + selectedMem + "');";
        ResultSet myRes = conn.myStmt.executeQuery(sql);
        availTeams.clear();
        while (myRes.next()) {
            availTeams.add(myRes.getString("team"));
        }
        availableTeams.setItems(availTeams);
        currTeams.clear();
        sql = "SELECT team FROM teams where team in (select teams from teams_members where members ='" + selectedMem + "');";
        myRes = conn.myStmt.executeQuery(sql);
        while (myRes.next()) {
            currTeams.add(myRes.getString("team"));
        }
        currentTeams.setItems(currTeams);
    }

    @FXML
    private void addAffiliation_mem(ActionEvent event) throws Exception {
        selectedMem = (String) membersView.getSelectionModel().getSelectedItem();
        String selectedTeam = (String) availableTeams.getSelectionModel().getSelectedItem();
        Connector conn = new Connector();
        String sql = "insert into teams_members values('" + selectedTeam + "', '" + selectedMem + "');";
        conn.myStmt.executeUpdate(sql);
        currTeams.add(selectedTeam);
        availTeams.remove(selectedTeam);
        availableTeams.setItems(availTeams);
        currentTeams.setItems(currTeams);
        refresh();

    }

    @FXML
    private void removeAffiliation_mem(ActionEvent event) throws Exception {
        selectedMem = (String) membersView.getSelectionModel().getSelectedItem();
        String selectedTeam = (String) currentTeams.getSelectionModel().getSelectedItem();
        Connector conn = new Connector();
        String sql = "delete from teams_members where members = '" + selectedMem + "' and teams = '" + selectedTeam + "';";
        conn.myStmt.executeUpdate(sql);
        availTeams.add(selectedTeam);
        availableTeams.setItems(availTeams);
        currTeams.remove(selectedTeam);
        currentTeams.setItems(currTeams);
        refresh();
    }

    public void getMembers() throws Exception {
        Connector conn = new Connector();
        String sql = "select member from members;";
        ResultSet myRes = conn.myStmt.executeQuery(sql);
        membersList = FXCollections.observableArrayList();
        while (myRes.next()) {
            membersList.add(myRes.getString("member"));
        }
        membersView.setItems(membersList);
    }

    // =================== teams ==========
    @FXML
    private TextField team;

    @FXML
    private ListView teamsView, availableMembers, currentMembers;

    ObservableList<String> teamsList = FXCollections.observableArrayList();
    ObservableList<String> availMembersList = FXCollections.observableArrayList();
    ObservableList<String> currMembersList = FXCollections.observableArrayList();
    String teamSelected = "";

    @FXML
    public void addToList_team(ActionEvent event) throws Exception {
        String teamStr = team.getText();
        Connector conn = new Connector();
        String sql = "insert into teams values('" + teamStr + "');";
        conn.myStmt.executeUpdate(sql);
        getTeams();
        team.setText("");
    }

    public void getTeams() throws Exception {
        Connector conn = new Connector();
        String sql = "select * from teams;";
        ResultSet myRes = conn.myStmt.executeQuery(sql);
        teamsList = FXCollections.observableArrayList();
        while (myRes.next()) {
            teamsList.add(myRes.getString("team"));
        }
        teamsView.setItems(teamsList);
    }

    @FXML
    public void removeFromList_team(ActionEvent event) throws Exception {
        teamSelected = (String) teamsView.getSelectionModel().getSelectedItem();
        Connector conn = new Connector();
        String sql = "delete from teams where team= '" + teamSelected + "';";
        conn.myStmt.executeUpdate(sql);
        getTeams();
        team.setText(teamSelected);
    }
    
    @FXML
    public void onClickActionTeam() throws Exception{
        
        teamSelected = (String) teamsView.getSelectionModel().getSelectedItem();
        Connector conn = new Connector();
        String sql = "SELECT member FROM members where member  not in (select members from teams_members where teams ='" + teamSelected + "');";
        ResultSet myRes = conn.myStmt.executeQuery(sql);
        availMembersList.clear();
        while (myRes.next()) {
            
            availMembersList.add(myRes.getString("member"));
        }
        availableMembers.setItems(availMembersList);
        currMembersList.clear();
        myRes.close();
        sql = "SELECT member FROM members where member in (select members from teams_members where teams ='" + teamSelected + "');";
        System.out.println("test1");
        myRes = conn.myStmt.executeQuery(sql);
        while (myRes.next()) {
            currMembersList.add(myRes.getString("member"));
        } 
        currentMembers.setItems(currMembersList);
    }
    
    @FXML
    public void addAssociation_team(ActionEvent event) throws Exception {
        teamSelected = (String) teamsView.getSelectionModel().getSelectedItem();
        String selectedMember = (String) availableMembers.getSelectionModel().getSelectedItem();
        Connector conn = new Connector();
        String sql = "insert into teams_members values('" + teamSelected + "', '" + selectedMember + "');";
        conn.myStmt.executeUpdate(sql);
        currMembersList.add(selectedMember);
        availMembersList.remove(selectedMember);
        availableMembers.setItems(availMembersList);
        currentMembers.setItems(currMembersList);
        getTeams();
    }
    
    @FXML
    public void removeAssociation_team(ActionEvent event) throws Exception{
        String selectedMember = (String) currentMembers.getSelectionModel().getSelectedItem();
        teamSelected = (String) teamsView.getSelectionModel().getSelectedItem();
        Connector conn = new Connector();
        String sql = "delete from teams_members where members = '" + selectedMember + "' and teams = '" + teamSelected + "';";
        conn.myStmt.executeUpdate(sql);
        availMembersList.add(selectedMember);
        availableMembers.setItems(availMembersList);
        currMembersList.remove(selectedMember);
        currentMembers.setItems(currMembersList);
        refresh();
    }
}
