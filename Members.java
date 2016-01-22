/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_della;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Venkatesh
 */
public class Members {
    public static Connector conn = new Connector();
    
    public static void addMemberToList(String member) throws Exception{
        String sql = "insert into members"
                    + "(member)"
                    + "values('" + member + "');";
            conn.myStmt.executeUpdate(sql);
    }
    public static void removeMember(String member) throws Exception{
        String sql = "delete from members where member= '" + member + "';";
        conn.myStmt.executeUpdate(sql);
    }
    
    public static ObservableList<String> available_Teams(String member) throws Exception{
        ObservableList<String> availTeams = FXCollections.observableArrayList();
        String sql = "SELECT team FROM teams where team  not in (select teams from teams_members where members ='" + member + "');";
        ResultSet myRes = conn.myStmt.executeQuery(sql);
        availTeams.clear();
        while (myRes.next()) {
            availTeams.add(myRes.getString("team"));
        }
        return availTeams;
    }
    public static ObservableList<String> current_teams(String member) throws Exception{
        ObservableList<String> currTeams = FXCollections.observableArrayList();
        String sql = "SELECT team FROM teams where team in (select teams from teams_members where members ='" + member + "');";
        ResultSet myRes = conn.myStmt.executeQuery(sql);
        while (myRes.next()) {
            currTeams.add(myRes.getString("team"));
        }
        return currTeams;
    }
    public static void AddAfffiliation(String member, String team) throws Exception {
        String sql = "insert into teams_members values('" + team + "', '" + member + "');";
        conn.myStmt.executeUpdate(sql);
    }
    public static void removeAffiliation(String member, String team) throws SQLException{
        String sql = "delete from teams_members where members = '" + member + "' and teams = '" + team + "';";
        conn.myStmt.executeUpdate(sql);
    }
}
