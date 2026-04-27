/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;

/**
 *
 * @author mawad
 */

import java.io.PrintWriter;
import java.net.Socket;

public class HRClient {

    public static void sendNotification(String message) {
        try {
            Socket socket = new Socket("localhost", 5000);

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);

            socket.close();

        } catch (Exception e) {
            System.out.println("Notification error: " + e.getMessage());
        }
    }
}