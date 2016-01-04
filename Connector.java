/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_della;

import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author Venkatesh Ejjagiri
 */
public class Connector {
    public java.sql.Connection myConn;
    public Statement myStmt;
    public Connector(){
        try {
            //1. Getting a connection to database
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample", "root", "venki_7474");
            //2.creating the  statement
            myStmt = myConn.createStatement();
        } catch(Exception e){
            System.err.println("");
        }
    }
}

