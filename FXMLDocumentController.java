/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_della;

import static java.lang.System.exit;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Chotu
 */
public class FXMLDocumentController implements Initializable {
    ObservableList<String> itemsList = FXCollections.observableArrayList();
    @FXML
    private TextArea itemDesc, itemResolution;
    
    @FXML
    private TextField itemName, dueDate;
    
    @FXML
    private ComboBox<String> actionItemCombo = new ComboBox<String>(itemsList);
    
    @FXML
    private void createActionItem(ActionEvent event) {
        try {
            String name = itemName.getText();
            String desc = itemDesc.getText();
            String resolution = itemResolution.getText();
            String duedate = dueDate.getText();
            //1. Getting a connection to database
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample", "root", "venki_7474");
            //2.creating the  statement
            Statement myStmt = myConn.createStatement();
            //3.Executing the SQL query

            String sql = "insert into item_sample"
                    + "(name, description, resolution, duedate)" 
                    + "values('" +name+ "','" +desc+ "','" + resolution + "','" + duedate + "')";
            myStmt.executeUpdate(sql);
            System.out.println("Item created");
        }catch(Exception e) {
            System.out.println(e);
        }
        actionItemCombo.setItems(itemsList);
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
        Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample", "root", "venki_7474");
        Statement myStmt = myConn.createStatement();
       if (result.get() == ButtonType.OK){     
            myStmt.executeUpdate("truncate items");
            myStmt.executeUpdate("insert into items select * from item_sample;");
            myStmt.executeUpdate("truncate item_sample");
           exit(0);
       } 
       else {
           myStmt.executeUpdate("truncate item_sample");
           exit(0);
        }
    }catch(Exception e){
               System.out.println(e);
           }
    }
    @FXML
    private void onActionCombo(ActionEvent event){
         try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample", "root", "venki_7474");
            Statement myStmt = myConn.createStatement();
            
            ResultSet myRs = myStmt.executeQuery("Select * from item_sample");
            String selectedItem = (String)actionItemCombo.getSelectionModel().getSelectedItem();
            while (myRs.next()) {
//                itemsList.add(myRs.getString("name"));
                if (selectedItem.equals(myRs.getString("name"))){
                    itemName.setText(myRs.getString("name"));
                    itemDesc.setText(myRs.getString("description"));
                    itemResolution.setText(myRs.getString("resolution"));
                    dueDate.setText(myRs.getString("duedate"));
                }
                    
            }
//            actionItemCombo.setItems(itemsList);
//            System.out.println("inserted");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            //1. Get a connection to database
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample", "root", "venki_7474");
            //2.create statement
            Statement myStmt = myConn.createStatement();
            //3.Execute SQL query
            myStmt.executeUpdate("insert into item_sample select * from items;");
            ResultSet myRs = myStmt.executeQuery("Select * from item_sample");

            //4.Process the result set
            while (myRs.next()) {
                itemsList.add(myRs.getString("name"));
//                System.out.println(myRs.getString("name") );
            }
            actionItemCombo.setItems(itemsList);
//            System.out.println("inserted");
        }catch(Exception e){
            System.out.println(e);
        }
        
    }    
    
}
