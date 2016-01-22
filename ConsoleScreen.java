/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_della;

import java.sql.ResultSet;

/**
 *
 * @author Chotu
 */
public class ConsoleScreen {
    public static String name;
    public static String description;
    public static String resolution;
    public static String creationDate;
    public static String duedate;
    public static String assignedMember;
    public static String assignedTeam;
    public static String status;
    
    public static void getDetails(String selectedItem) throws Exception{
        Connector conn = new Connector();
        String sql = "select * from action_item where name='"+selectedItem+"';";
        ResultSet myRes = conn.myStmt.executeQuery(sql);
        while (myRes.next()){
            name = myRes.getString("name");
            description = myRes.getString("description");
            resolution = myRes.getString("resolution");
            creationDate = myRes.getString("creationdate");
            duedate = myRes.getString("duedate");
            assignedMember = myRes.getString("assignedMember");
            assignedTeam = myRes.getString("assignedTeam");
            status = myRes.getString("status");
        }
    }
}
