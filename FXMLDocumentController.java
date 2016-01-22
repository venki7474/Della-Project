/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_della;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import static java.lang.System.exit;
import java.net.URL;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
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
    
    ////////// actionItems ////////////////
    
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
    private ComboBox itemStatus, assgToMember, assgToTeam;
    
    ObservableList<String> statusList = FXCollections.observableArrayList("open","closed");
                  /////////////// available members and teams ///////////////////
    ObservableList<String> avMembsList_combo = FXCollections.observableArrayList();
    ObservableList<String> avTeamsList_combo = FXCollections.observableArrayList();
    
    ///////////// online offline indicators //////////
    @FXML
    public Circle onlineLabel1, onlineLabel2, onlineLabel3, onlineLabel4;
    @FXML
    public Circle offlineLabel1, offlineLabel2, offlineLabel3, offlineLabel4;
    public boolean ConnectionStatus;
    
    
    
    ///////// all buttons ////////////
    @FXML
    ToolBar actionButtons;
    @FXML
    Button addMemToListButton, remvMemButton, addTeamToListButton, remvTeamButton,addAffMem, remvAffMem, addAsstnTeam, remvAsstnTeam,
            updateButton,deleteButton;
    
    Hashtable<String, ArrayList<String>> disp_hash = new Hashtable<String, ArrayList<String>>();
    boolean flag_f;
    @FXML
    private void updateAction(ActionEvent event) throws Exception {
            
            String name = itemName.getText();
            String desc = itemDesc.getText();
            String resolution = itemResolution.getText();
            String duedate = dueDate.getText();
            String creationDate = currentDate.getText();
            String actionStatus = (String) itemStatus.getSelectionModel().getSelectedItem();
            String memAssigned = (String) assgToMember.getSelectionModel().getSelectedItem();
            String teamAssigned = (String) assgToTeam.getSelectionModel().getSelectedItem();
            String selectedItem = (String) actionItemCombo.getSelectionModel().getSelectedItem();
            ActionItems.updateActionItem(selectedItem, name, desc, resolution, duedate, creationDate, actionStatus, memAssigned, teamAssigned);
            refresh();
            if (flag_f){
                Connector conn = new Connector();
                conn.myStmt.executeUpdate("Truncate flag;");
            }
    }
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
    private void createActionItem(ActionEvent event) throws Exception {
            String name = itemName.getText();
            String desc = itemDesc.getText();
            String resolution = itemResolution.getText();
            String duedate = dueDate.getText();
            String creationDate = currentDate.getText();
            String actionStatus = (String) itemStatus.getSelectionModel().getSelectedItem();
            String memAssigned = (String) assgToMember.getSelectionModel().getSelectedItem();
            String teamAssigned = (String) assgToTeam.getSelectionModel().getSelectedItem();
            String selectedItem = (String) actionItemCombo.getSelectionModel().getSelectedItem();
            ActionItems.createItem(name, desc, resolution, duedate, creationDate, actionStatus, memAssigned, teamAssigned);
            System.out.println("Item created");
            refresh();
    }

    @FXML
    private void deleteActionItem(ActionEvent event) throws Exception{
            String selectedItem = (String) actionItemCombo.getSelectionModel().getSelectedItem();
            ActionItems.deleteActionItem(selectedItem);
            refresh();
            itemName.setText("");
            itemDesc.setText("");
            itemResolution.setText("");
            dueDate.setText("");
            assgToMember.setValue("");
            assgToTeam.setValue("");
            itemStatus.setValue("open");
            if (flag_f){
                Connector conn = new Connector();
                conn.myStmt.executeUpdate("Truncate flag;");
            }
            
    }
    
    @FXML
    private void onActionCombo(ActionEvent event) throws Exception{
        String selectedItem = (String) actionItemCombo.getSelectionModel().getSelectedItem();
        if (ConnectionStatus){
            ActionItems.OnActionCombo(selectedItem);
            itemName.setText(ActionItems.name1);
            itemDesc.setText(ActionItems.desc1);
            itemResolution.setText(ActionItems.resolution1);
            dueDate.setText(ActionItems.duedate1);
            currentDate.setText(ActionItems.creationDate1);
            assgToMember.setValue(ActionItems.memAssigned1);
            assgToTeam.setValue(ActionItems.teamAssigned1);
            itemStatus.setValue(ActionItems.actionStatus1);
            flag_f = checkFlag(selectedItem);
            if ( flag_f ) {
                updateButton.setDisable(flag_f);
                deleteButton.setDisable(flag_f);
            } else {
                Connector conn = new Connector();
                conn.myStmt.executeUpdate("insert into flag values=('"+selectedItem+"');");
            }
        } else{
            disp_hash = readFromFile();
            ArrayList<String> sfl = disp_hash.get(selectedItem);
            itemName.setText(selectedItem);
            itemDesc.setText(sfl.get(0));
            itemResolution.setText(sfl.get(1));
            dueDate.setText(sfl.get(3));
            currentDate.setText(sfl.get(2));
            assgToMember.setValue(sfl.get(4));
            assgToTeam.setValue(sfl.get(5));
            itemStatus.setValue(sfl.get(6));
        }
    }
    public boolean checkFlag(String str) throws Exception {
        String sql = "select * from itemflag where itemflag='"+str+"';";
        Connector conn = new Connector();
        ResultSet myRes = conn.myStmt.executeQuery(sql);
        while(myRes.next()){
            return true;
        }
        return false;
    }
    public void displayingMemToTeam() throws Exception {
        String selected_mem = (String) assgToMember.getSelectionModel().getSelectedItem();
        String selected_team = (String) assgToTeam.getSelectionModel().getSelectedItem();
        ArrayList<String> teamListTemp = new ArrayList<>();
        Connector conn = new Connector();
        //System.out.println("In 1) " + selected_mem);
        if (selected_mem == null || selected_mem.equals("")) {
            // error code
            //System.out.println("1) U are not allowed! Sorry");
        } else if(selected_mem.equals("None")){
            showTeamsList();
        } else {
            if (selected_team == null || selected_team.equals("") || selected_team.equals("None")) {
                assgToTeam.setValue("");
            }
            String query1 = "select * from teams_members where members = '" + selected_mem +"'";
            ResultSet myRs = conn.myStmt.executeQuery(query1);
            while(myRs.next()){
                teamListTemp.add(myRs.getString("teams"));
            }
            teamListTemp.add("None");
            ObservableList<String> teamListFinal = FXCollections.observableArrayList(teamListTemp);
            assgToTeam.setItems(teamListFinal);
        }
    }
    public void displayingTeamToMem() throws Exception {
        String selected_team = (String) assgToTeam.getSelectionModel().getSelectedItem();
        String selected_mem = (String) assgToMember.getSelectionModel().getSelectedItem();
        Connector conn = new Connector();
        ArrayList<String> memberListTemp = new ArrayList<>();
        if (selected_team == null || selected_team.equals("")) {
 
        } else if(selected_team.equals("None")){
            showMembersList();
        } else {
            if (selected_mem == null || selected_mem.equals("") || selected_mem.equals("None")) {
                assgToMember.setValue("");
            }
            String query1 = "select * from teams_members where teams = '" + selected_team +"'"; 
            ResultSet rs = conn.myStmt.executeQuery(query1);
            while(rs.next()){
                memberListTemp.add(rs.getString("members"));
            }
            memberListTemp.add("None");
            ObservableList<String> memberListFinal = FXCollections.observableArrayList(memberListTemp);
            assgToMember.setItems(memberListFinal);
        }
    }
    public void showTeamsList() throws Exception{
        Connector conn = new Connector();
        ResultSet myRs2 = conn.myStmt.executeQuery("Select * from teams");
        avTeamsList_combo = FXCollections.observableArrayList();
        while(myRs2.next()){
            avTeamsList_combo.add(myRs2.getString("team"));
        }
        assgToTeam.setItems(avTeamsList_combo);
    }
    public void showMembersList() throws Exception{
        Connector conn = new Connector();
        ResultSet myRs1 = conn.myStmt.executeQuery("Select * from members");
        avMembsList_combo = FXCollections.observableArrayList();
        while(myRs1.next()){
            avMembsList_combo.add(myRs1.getString("member"));
        }
        assgToMember.setItems(avMembsList_combo);
    }
    public void assigned_mem_team() throws Exception{
        Connector conn = new Connector();
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
    public void refresh() throws Exception{
            consoleItemsView.setItems(ActionItems.getItemlist());
            actionItemCombo.setItems(ActionItems.getItemlist());
    }
    /**
     *  this is quit action button
     * @param event 
     */
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
                //connection.myStmt.executeUpdate("truncate flag");
                //connection.myStmt.executeUpdate("insert into flag values('1')");
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            Check_Connection();
            addMemToListButton.setDisable(true);
            remvMemButton.setDisable(true);
            addTeamToListButton.setDisable(true);
            remvTeamButton.setDisable(true);
            addAffMem.setDisable(true);
            remvAffMem.setDisable(true);
            addAsstnTeam.setDisable(true);
            remvAsstnTeam.setDisable(true);
            
            
            
            currentDate.setText(format.format(dateobj));
            getMembers();
            getTeams();
            assigned_mem_team();
            itemStatus.setItems(statusList);
            itemStatus.setValue("open");
        } catch (Exception e){
            System.out.println(e);
        }
    }
    Hashtable<String, ArrayList<String>> myHash = new Hashtable<String, ArrayList<String>>();
    public void Check_Connection(){
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
//                    actionButtons.setDisable(false);
                    try{
                        Connector conn = new Connector();

                        
                        ResultSet myRs = conn.myStmt.executeQuery("Select * from item_sample ;");
                        while (myRs.next()) {
                            ArrayList<String> arrList = new ArrayList<String>();
                            arrList.add(myRs.getString("description"));
                            arrList.add(myRs.getString("resolution"));
                            arrList.add(myRs.getString("creationdate"));
                            arrList.add(myRs.getString("duedate"));
                            arrList.add(myRs.getString("assignedMember"));
                            arrList.add(myRs.getString("assignedteam"));
                            arrList.add(myRs.getString("status"));
                            myHash.put(myRs.getString("name"),arrList);
                        }
                        writeIntoFile(myHash);
                        
                        disp_hash = new Hashtable<String, ArrayList<String>>();
                        actionItemCombo.setItems(ActionItems.getItemlist());
                        consoleItemsView.setItems(ActionItems.getItemlist());
                        //checkOnlineUser();
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
                   
//                    myHash = readFromFile();
//                    ObservableList<String> hash_itemsList = FXCollections.observableArrayList();
//                      for (String key : myHash.keySet()){
//                          hash_itemsList.add(key);
//                      }
//                      consoleItemsView.setItems(hash_itemsList);
                   }

            }
            }, 0, 1000);
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
            if (statusFlag.equals("2")){
                disableButtons();
            } else {
                conn.myStmt.executeUpdate("truncate flag");
                conn.myStmt.executeUpdate("insert into flag values('1')");
            }
    }
    
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
        if (ConnectionStatus){
            if( member.getText().equals("")) {
                addMemToListButton.setDisable(true);
            } else {
                addMemToListButton.setDisable(false);
            }
        }
    }
   /*To enable remove from list button in members screen*/
    public void memRemove_enable() {
        if (ConnectionStatus)
            remvMemButton.setDisable(false);
    }
   /*To enable Add_Affiliation button in members screen */
    public void addAffil_enable() {
        if (ConnectionStatus)
           addAffMem.setDisable(false);
    }
    /*To enable remove_affiliation button in members screen */
    public void removeAffil_enable() {
        if (ConnectionStatus)
            remvAffMem.setDisable(false);
    }
   
    public void teamAdd_enable() {
        if (ConnectionStatus){
            if(team.getText().equals("")) {
                addTeamToListButton.setDisable(true);
            } else {
                addTeamToListButton.setDisable(false);
            }
        }
   }
   /*To enable remove from list button in teams screen*/
   public void teamRemove_enable() {
        if (ConnectionStatus)
            remvTeamButton.setDisable(false);
   }
   /*To enable Add_Association button in teams screen */
   public void addAss_enable() {
        if(ConnectionStatus)
            addAsstnTeam.setDisable(false);
   }
   public void remvAss_enable() {
        if (ConnectionStatus)
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
    public void addToList_mem(ActionEvent event) throws Exception{
        String memb = member.getText();
        Members.addMemberToList(memb);
        getMembers();
        member.setText("");
    }

    @FXML
    public void removeMember(ActionEvent event) throws Exception{
        String selectedMember = (String) membersView.getSelectionModel().getSelectedItem();
        Members.removeMember(selectedMember);
        getMembers();
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
        availTeams.clear();
        availTeams = Members.available_Teams(selectedMem);
        availableTeams.setItems(availTeams);
        currTeams.clear();
        currTeams = Members.current_teams(selectedMem);
        currentTeams.setItems(currTeams);
    }

    @FXML
    private void addAffiliation_mem(ActionEvent event) throws Exception {
        selectedMem = (String) membersView.getSelectionModel().getSelectedItem();
        String selectedTeam = (String) availableTeams.getSelectionModel().getSelectedItem();
        Members.AddAfffiliation(selectedMem, selectedTeam);
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
        Members.removeAffiliation(selectedMem, selectedTeam);
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
        if (ConnectionStatus){
            ConsoleScreen.getDetails(selectedConsoleItem);
            name_Console.setText(ConsoleScreen.name);
            desc_Console.setText(ConsoleScreen.description);
            res_Console.setText(ConsoleScreen.resolution);
            duedate_Console.setText(ConsoleScreen.duedate);
            creationDate_Console.setText(ConsoleScreen.creationDate);
            status_Console.setText(ConsoleScreen.status);
            assgnMem_Console.setText(ConsoleScreen.assignedMember);
            assgnTeam_Console.setText(ConsoleScreen.assignedTeam);
        } else {
            myHash = readFromFile();
            System.out.println(myHash);
            ArrayList<String> sfl = new ArrayList<String>();
            sfl = myHash.get(selectedConsoleItem);
//            System.out.println("arrr-->"+sfl+"," +selectedConsoleItem);
            name_Console.setText(selectedConsoleItem);
            desc_Console.setText(sfl.get(0));
            res_Console.setText(sfl.get(1));
            duedate_Console.setText(sfl.get(3));
            creationDate_Console.setText(sfl.get(2));
            status_Console.setText(sfl.get(6));
            assgnMem_Console.setText(sfl.get(4));
            assgnTeam_Console.setText(sfl.get(5));
        }
    }
    
    public void writeIntoFile(Hashtable<String, ArrayList<String>> list) throws IOException{    
       OutputStream ops = new FileOutputStream("actionItems.xml");;
       ObjectOutputStream objOps = new ObjectOutputStream(ops);;
       try {
//           ops = new FileOutputStream("actionItems.xml");
//           objOps = new ObjectOutputStream(ops);
           objOps.writeObject(list);
           objOps.flush();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       } finally{
           try{
               if(objOps != null) objOps.close();
           } catch (Exception ex){
                
           }
       }
    }
    
    public Hashtable<String, ArrayList<String>> readFromFile() {
       Hashtable<String, ArrayList<String>> list = new Hashtable<>();
       try {
          FileInputStream fileIn = new FileInputStream("actionItems.xml");
          ObjectInputStream in = new ObjectInputStream(fileIn);
          list = (Hashtable) in.readObject();
          in.close();
          fileIn.close();
       } catch(IOException i) {
          i.printStackTrace();
       } catch(ClassNotFoundException c) {
          System.out.println(c);
          c.printStackTrace();
       }
       
       return list;
   }
    
}
