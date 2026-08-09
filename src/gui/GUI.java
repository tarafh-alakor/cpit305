/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gui;

import database.DBSetup;

/**
 * GUI - Application entry point. Initializes the database schema via DBSetup,
 * then launches the Login screen on the Swing Event Dispatch Thread (EDT) for
 * thread-safe GUI startup.
 */
public class GUI {

    public static void main(String[] args) {
        // TODO code application logic here
        //Database setup
        DBSetup.setupDatabase();
        //run GUI
        java.awt.EventQueue.invokeLater(() -> new Login().setVisible(true));
    }
}
