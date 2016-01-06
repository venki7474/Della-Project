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
import javafx.scene.text.TextFlow;

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
    private void updateAction(ActionEvent event){
        try {
            Connector conn = new Connector();
            conn.myStmt = conn.myConn.createStatement();
            String name = itemName.getText();
            String desc = itemDesc.getText();
            String resolution = itemResolution.getText();
            String duedate = dueDate.getText();
            String selectedItem = (String)actionItemCombo.getSelectionModel().getSelectedItem();
            conn.myStmt.executeUpdate("delete from item_sample where name = '"+selectedItem+"';");
            String sql = "insert into item_sample"
                    + "(name, description, resolution, duedate)" 
                    + "values('" +name+ "','" +desc+ "','" + resolution + "','" + duedate + "')";
            conn.myStmt.executeUpdate(sql);
            refresh();
        }catch(Exception e){
            System.out.println(e);
        }
        
    }
    @FXML
    private void clearFormAction(ActionEvent event){
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
                    + "values('" +name+ "','" +desc+ "','" + resolution + "','" + duedate + "')";
            conn.myStmt.executeUpdate(sql);
            System.out.println("Item created");
        }catch(Exception e) {
            System.out.println(e);
        }
//        actionItemCombo.setItems(itemsList);
        refresh();
    }
    
    @FXML
    private void deleteActionItem(ActionEvent event){
        try{
            Connector conn = new Connector();
            conn.myStmt = conn.myConn.createStatement();
            String selectedItem = (String)actionItemCombo.getSelectionModel().getSelectedItem();
            conn.myStmt.executeUpdate("delete from item_sample where name = '"+selectedItem+"';");
            refresh();
            itemName.setText("");
            itemDesc.setText("");
            itemResolution.setText("");
            dueDate.setText("");
        }catch(Exception e){
               System.out.println(e);
           }
    }
    
    @FXML
    private void onActionCombo(ActionEvent event){
         try {
            Connector conn = new Connector();
            conn.myStmt = conn.myConn.createStatement();
            
            ResultSet myRs = conn.myStmt.executeQuery("Select * from item_sample");
            String selectedItem = (String)actionItemCombo.getSelectionModel().getSelectedItem();
            if (selectedItem.equals("")){
                    itemName.setText("");
                    itemDesc.setText("");
                    itemResolution.setText("");
                    dueDate.setText("");
            }else {
                while (myRs.next()) {
//                itemsList.add(myRs.getString("name"));
                    if (selectedItem.equals(myRs.getString("name"))){
                        itemName.setText(myRs.getString("name"));
                        itemDesc.setText(myRs.getString("description"));
                        itemResolution.setText(myRs.getString("resolution"));
                        dueDate.setText(myRs.getString("duedate"));
                    } 
                }
            }
//            actionItemCombo.setItems(itemsList);
//            System.out.println("inserted");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void refresh(){
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
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    @FXML
    private void quitAction(ActionEvent event){
       Alert alert = new Alert(AlertType.CONFIRMATION);
       alert.setTitle("Confirmation");
       String s = "A Quit has been requested and there are updated Action Items that have not been saved!";
       alert.setHeaderText(s);
       alert.setContentText("Do you want to save these Action Items? Click Ok to save or Click Cancel to ignore Changes");
       Optional<ButtonType> result = alert.showAndWait();
       try{
        Connector connection = new Connector();
        connection.myStmt = connection.myConn.createStatement();
       if (result.get() == ButtonType.OK){     
            connection.myStmt.executeUpdate("truncate items");
            connection.myStmt.executeUpdate("insert into items select * from item_sample;");
            connection.myStmt.executeUpdate("truncate item_sample");
           exit(0);
       } 
       else {
           connection.myStmt.executeUpdate("truncate item_sample");
           exit(0);
        }
    }catch(Exception e){
               System.out.println(e);
           }
    }
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
            
        }catch(Exception e){
            System.out.println(e);
        }
    }
/*
    the below code is for cosole
    */  
        @FXML
        private TextFlow consoleItems;
        
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
        public void addToList(ActionEvent event ){
            String memb = member.getText();
            try{
                Connector conn = new Connector();
                String sql = "insert into members"
                        + "(membname)" 
                        + "values('" + memb + "');";   
                conn.myStmt.executeUpdate(sql);
                getMembers();
            } catch(Exception e){
                System.err.println(e);
            }
        }
        @FXML
        public void removeMember(ActionEvent event){
            String selectedMember = (String)membersView.getSelectionModel().getSelectedItem();
            try {
                Connector conn = new Connector();
                String sql = "delete from members where membname= '"+selectedMember+"';";
                conn.myStmt.executeUpdate(sql);
                getMembers();
            }catch(Exception e){
                System.out.println(e);
            }

        }
//        String selecMem = (String)membersView.getSelectionModel().getSelectedItem();
        @FXML 
        private Label selectedMember;
        private Label selectedMember2;

        
//        String selecMem = (String)membersView.getSelectionModel().getSelectedItem();
//        @FXML
//        public void onActionMembers(ActionEvent event){
//            String selectedMem = (String)membersView.getSelectionModel().getSelectedItem();
//            selectedMember.setText(selectedMem);
//            selectedMember2.setText(selectedMem);

//        }
        public void getMembers(){
            try {
                 Connector conn = new Connector();
                 String sql = "select membname from members; ";
                 ResultSet myRes = conn.myStmt.executeQuery(sql);
                 membersList = FXCollections.observableArrayList();
                 while(myRes.next()){
                     membersList.add(myRes.getString("membname"));
                 }
                 membersView.setItems(membersList);
            }catch(Exception e){
                System.err.println(e);
            }
        }

}
