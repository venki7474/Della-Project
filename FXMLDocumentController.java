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
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Chotu
 */
public class FXMLDocumentController implements Initializable {
    @FXML
    private TextArea itemDesc, itemResolution;
    
    @FXML
    private TextField itemName, dueDate;
    
    @FXML
    private Label label;
    
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
    }
    
    @FXML
    private void quitAction(ActionEvent event){
        exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
