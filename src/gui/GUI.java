/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gui;

import database.DBSetup;

/**
 *
 * @author mawad
 */
public class GUI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Database setup
        DBSetup.setupDatabase();
        //run GUI
        java.awt.EventQueue.invokeLater(() -> new Login().setVisible(true));
    }
}
