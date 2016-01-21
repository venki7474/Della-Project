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
import java.time.Clock;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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
            String creationDate = currentDate.getText();
            String actionStatus = (String) itemStatus.getSelectionModel().getSelectedItem();
            String memAssigned = (String) assgToMember.getSelectionModel().getSelectedItem();
            String teamAssigned = (String) assgToTeam.getSelectionModel().getSelectedItem();
            String selectedItem = (String) actionItemCombo.getSelectionModel().getSelectedItem();
            conn.myStmt.executeUpdate("delete from item_sample where name = '" + selectedItem + "';");
            String sql = "insert into item_sample"
                    + "(name, description, resolution, creationdate, duedate, status, assignedMember, assignedTeam)"
                    + "values('" + name + "','" + desc + "','" + resolution + "','" + creationDate + "','"+duedate+"', '"
                    + actionStatus+"', '"+memAssigned+ "', '"+teamAssigned+"')";
            conn.myStmt.executeUpdate(sql);
            System.out.println("venki's test");
            refresh();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @FXML
    private ComboBox itemStatus, assgToMember, assgToTeam;


    @FXML
    private void clearFormAction(ActionEvent event) {
        itemName.setText("");
        itemDesc.setText("");
        itemResolution.setText("");
        dueDate.setText("");
        assgToMember.setValue("");
        assgToTeam.setValue("");
        itemStatus.setValue("open");
    }

    @FXML
    private void createActionItem(ActionEvent event) {
        try {
            String name = itemName.getText();
            String desc = itemDesc.getText();
            String resolution = itemResolution.getText();
            String duedate = dueDate.getText();
            String creationDate = currentDate.getText();
            String actionStatus = (String) itemStatus.getSelectionModel().getSelectedItem();
            String memAssigned = (String) assgToMember.getSelectionModel().getSelectedItem();
            String teamAssigned = (String) assgToTeam.getSelectionModel().getSelectedItem();
            String selectedItem = (String) actionItemCombo.getSelectionModel().getSelectedItem();
            Connector conn = new Connector();
            conn.myStmt = conn.myConn.createStatement();
            String sql = "insert into item_sample"
                    + "(name, description, resolution, creationdate, duedate, status, assignedMember, assignedTeam)"
                    + "values('" + name + "','" + desc + "','" + resolution + "','" + creationDate + "','"+duedate+"', '"
                    + actionStatus+"', '"+memAssigned+ "', '"+teamAssigned+"')";
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
            assgToMember.setValue("");
            assgToTeam.setValue("");
            itemStatus.setValue("open");
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
                        currentDate.setText(myRs.getString("creationdate"));
                        assgToMember.setValue(myRs.getString("assignedMember"));
                        assgToTeam.setValue(myRs.getString("assignedTeam"));
                        itemStatus.setValue(myRs.getString("status"));
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
//            itemsList.add("");
            while (myRs.next()) {
                itemsList.add(myRs.getString("name"));
            }
            consoleItemsView.setItems(itemsList);
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
                connection.myStmt.executeUpdate("truncate flag");
                connection.myStmt.executeUpdate("insert into flag values('0')");
//                connection.myStmt.executeUpdate("truncate item_sample");
                exit(0);
            } else {
//                connection.myStmt.executeUpdate("truncate item_sample");
                exit(0);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @FXML
    public Circle onlineLabel1, onlineLabel2, onlineLabel3, onlineLabel4;
    @FXML
    public Circle offlineLabel1, offlineLabel2, offlineLabel3, offlineLabel4;
//    offlineLabel;
    
    ObservableList<String> statusList = FXCollections.observableArrayList("open","closed");
    ObservableList<String> avMembsList_combo = FXCollections.observableArrayList();
    ObservableList<String> avTeamsList_combo = FXCollections.observableArrayList();
    boolean ConnectionStatus;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        check_online_offline();
        new Timer().schedule(
            new TimerTask() {
            @Override
            public void run() {
                ConnectionStatus = OnlineStatus.checkStatus();
                System.out.println("Status:" + ConnectionStatus );
                if(ConnectionStatus) {
                   onlineLabel1.setFill(Color.GREEN);
                   onlineLabel2.setFill(Color.GREEN);
                   onlineLabel3.setFill(Color.GREEN);
                   onlineLabel4.setFill(Color.GREEN);
                   offlineLabel1.setFill(Color.WHITE);
                   offlineLabel2.setFill(Color.WHITE);
                   offlineLabel3.setFill(Color.WHITE);
                   offlineLabel4.setFill(Color.WHITE);
                   try{
                        checkOnlineUser();
                   }catch(Exception e){
                       
                   }
                } else {
                   onlineLabel1.setFill(Color.WHITE);
                   onlineLabel2.setFill(Color.WHITE);
                   onlineLabel3.setFill(Color.WHITE);
                   onlineLabel4.setFill(Color.WHITE);
                   offlineLabel1.setFill(Color.RED);
                   offlineLabel2.setFill(Color.RED);
                   offlineLabel3.setFill(Color.RED);
                   offlineLabel4.setFill(Color.RED);
                   disableButtons();
                } 
            }
            }, 0, 10000);
        addMemToListButton.setDisable(true);
        remvMemButton.setDisable(true);
        addTeamToListButton.setDisable(true);
        remvTeamButton.setDisable(true);
        addAffMem.setDisable(true);
        remvAffMem.setDisable(true);
        addAsstnTeam.setDisable(true);
        remvAsstnTeam.setDisable(true);
        
        try {
            Connector conn = new Connector();
            ResultSet myRs = conn.myStmt.executeQuery("Select * from item_sample");
            
            itemsList = FXCollections.observableArrayList();
            while (myRs.next()) {
                itemsList.add(myRs.getString("name"));
//                consoleList.add(myRs.getString("name"));
            }
             
            actionItemCombo.setItems(itemsList);
            consoleItemsView.setItems(itemsList);
            currentDate.setText(format.format(dateobj));
            getMembers();
            getTeams();
            assigned_mem_team();
            itemStatus.setItems(statusList);
            itemStatus.setValue("open");
            
        
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void checkOnlineUser() throws Exception{
        Connector conn = new Connector();
            String statusFlag ="";
//            conn.myStmt = conn.myConn.createStatement();
//            conn.myStmt.executeUpdate("insert into item_sample select * from items;");
            ResultSet myRs1 = conn.myStmt.executeQuery("Select flag_id from flag");
            while(myRs1.next()){
                statusFlag = myRs1.getString("flag_id");
            }
            if (statusFlag.equals("1")){
                disableButtons();
            } else {
                conn.myStmt.executeUpdate("truncate flag");
                conn.myStmt.executeUpdate("insert into flag values('1')");
            }
    }
    
    @FXML
    ToolBar actionButtons;
    @FXML
    Button addMemToListButton, remvMemButton, addTeamToListButton, remvTeamButton,addAffMem, remvAffMem, addAsstnTeam, remvAsstnTeam;
    public void disableButtons(){
        actionButtons.setDisable(true);
        addMemToListButton.setDisable(true);
        remvMemButton.setDisable(true);
        addTeamToListButton.setDisable(true);
        remvTeamButton.setDisable(true);
        addAffMem.setDisable(true);
        remvAffMem.setDisable(true);
        addAsstnTeam.setDisable(true);
        remvAsstnTeam.setDisable(true);
    }
    
    public void memAdd_enable() {
       if(member.getText().equals("")) {
           addMemToListButton.setDisable(true);
       }
       else {
           addMemToListButton.setDisable(false);
       }   
    }
   /*To enable remove from list button in members screen*/
    public void memRemove_enable() {
       remvMemButton.setDisable(false);
    }
   /*To enable Add_Affiliation button in members screen */
    public void addAffil_enable() {
       addAffMem.setDisable(false);
    }
    /*To enable remove_affiliation button in members screen */
    public void removeAffil_enable() {
       remvAffMem.setDisable(false);
    }
   
    public void teamAdd_enable() {
       if(team.getText().equals("")) {
           addTeamToListButton.setDisable(true);
       }
       else {
           addTeamToListButton.setDisable(false);
       }   
   }
   /*To enable remove from list button in teams screen*/
   public void teamRemove_enable() {
       remvTeamButton.setDisable(false);
   }
   /*To enable Add_Association button in teams screen */
   public void addAss_enable() {
       addAsstnTeam.setDisable(false);
   }
   public void remvAss_enable() {
       remvAsstnTeam.setDisable(false);
   }
   public void checkNull_curr_mem(){
       String str = (String) currentMembers.getSelectionModel().getSelectedItem();
       if ( str == null){
           remvAsstnTeam.setDisable(true);
       }
   }
   public void checkNull_avl_mem(){
       String str = (String) availableMembers.getSelectionModel().getSelectedItem();
       if ( str == null){
           addAsstnTeam.setDisable(true);
       }
   }
   public void checkNull_curr_team(){
       String str = (String) currentTeams.getSelectionModel().getSelectedItem();
       if ( str == null){
           remvAffMem.setDisable(true);
       }
   }
   public void checkNull_avl_team(){
       String str = (String) availableTeams.getSelectionModel().getSelectedItem();
       if ( str == null){
           addAffMem.setDisable(true);
       }
   }
    public void assigned_mem_team() throws Exception{
        Connector conn = new Connector();
        
        itemStatus.setItems(statusList);
            ResultSet myRs1 = conn.myStmt.executeQuery("Select * from members");
            while(myRs1.next()){
                avMembsList_combo.add(myRs1.getString("member"));
            }
            assgToMember.setItems(avMembsList_combo);
            ResultSet myRs2 = conn.myStmt.executeQuery("Select * from teams");
            while(myRs2.next()){
                avTeamsList_combo.add(myRs2.getString("team"));
            }
            assgToTeam.setItems(avTeamsList_combo);
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
    
  ///////////  console ////////////
    @FXML
    public ListView consoleItemsView;
    @FXML
    public TextField name_Console, duedate_Console;
    @FXML
    public TextArea desc_Console, res_Console;
    @FXML
    public Label creationDate_Console, assgnMem_Console, assgnTeam_Console, status_Console;
    
    @FXML
    private void onClickConsoleItems() throws Exception{
        String selectedConsoleItem = (String) consoleItemsView.getSelectionModel().getSelectedItem();
        ConsoleScreen.getDetails(selectedConsoleItem);
        name_Console.setText(ConsoleScreen.name);
        desc_Console.setText(ConsoleScreen.description);
        res_Console.setText(ConsoleScreen.resolution);
        duedate_Console.setText(ConsoleScreen.duedate);
        creationDate_Console.setText(ConsoleScreen.creationDate);
        status_Console.setText(ConsoleScreen.status);
        assgnMem_Console.setText(ConsoleScreen.assignedMember);
        assgnTeam_Console.setText(ConsoleScreen.assignedTeam);
    }
}
