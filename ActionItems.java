/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_della;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Venkatesh Ejjagiri
 */
public class ActionItems {
    
    static Connector conn = new Connector();
    static String name1="", desc1="", resolution1="", duedate1="", creationDate1="", actionStatus1="", memAssigned1="", teamAssigned1="";
    static ObservableList<String> itemsList = FXCollections.observableArrayList();
    public static void updateActionItem(String selectedItem, String name, String desc,
            String resolution,String duedate,String creationDate,
            String actionStatus,String memAssigned,String teamAssigned ) throws SQLException {
        conn.myStmt = conn.myConn.createStatement();
        conn.myStmt.executeUpdate("delete from item_sample where name = '" + selectedItem + "';");
            String sql = "insert into item_sample"
                    + "(name, description, resolution, creationdate, duedate, status, assignedMember, assignedTeam)"
                    + "values('" + name + "','" + desc + "','" + resolution + "','" + creationDate + "','"+duedate+"', '"
                    + actionStatus+"', '"+memAssigned+ "', '"+teamAssigned+"')";
            conn.myStmt.executeUpdate(sql);
    }
    public static void createItem(String name, String desc,
            String resolution,String duedate,String creationDate,
            String actionStatus,String memAssigned,String teamAssigned ) throws SQLException{
            conn.myStmt = conn.myConn.createStatement();
            String sql = "insert into item_sample"
                    + "(name, description, resolution, creationdate, duedate, status, assignedMember, assignedTeam)"
                    + "values('" + name + "','" + desc + "','" + resolution + "','" + creationDate + "','"+duedate+"', '"
                    + actionStatus+"', '"+memAssigned+ "', '"+teamAssigned+"')";
            conn.myStmt.executeUpdate(sql);
    }
    public static void deleteActionItem(String selectedItem) throws SQLException{
        conn.myStmt = conn.myConn.createStatement();
        conn.myStmt.executeUpdate("delete from item_sample where name = '" + selectedItem + "';");
    }
    public static void OnActionCombo(String selectedItem) throws SQLException{
        ResultSet myRs = conn.myStmt.executeQuery("Select * from item_sample where name='"+selectedItem+"';");
        while(myRs.next()) {
            name1 = myRs.getString("name");
            desc1 = myRs.getString("description");
            resolution1 = myRs.getString("resolution");
            creationDate1 = myRs.getString("creationdate");
            duedate1 = myRs.getString("duedate");
            memAssigned1 = myRs.getString("assignedMember");
            teamAssigned1 = myRs.getString("assignedTeam");
            actionStatus1 = myRs.getString("status");
        }
    }
    public static ObservableList<String> getItemlist() throws SQLException{
        ResultSet myRs = conn.myStmt.executeQuery("Select * from item_sample");
        itemsList = FXCollections.observableArrayList();
        while (myRs.next()) {
            itemsList.add(myRs.getString("name"));
        }     
        return itemsList;
    }
 
    
}
