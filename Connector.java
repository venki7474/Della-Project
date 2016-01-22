/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_della;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;

/**
 *
 * @author Venkatesh Ejjagiri
 */
public class Connector {
    public Connection myConn;
    public Statement myStmt;
    public Connector(){
        try {
            //1. Getting a connection to database
            myConn = DriverManager.getConnection("jdbc:mysql://10.10.10.167:3306/azra", "root", "123");
            //2.creating the  statement
            myStmt = myConn.createStatement();
        } catch(Exception e){
            System.err.println("");
        }
    }
}

