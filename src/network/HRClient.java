package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * HRClient - Sends notification messages to the HRServer over TCP/IP. Used
 * throughout the application to log important HR actions in real time. The
 * client sends a message and waits for acknowledgment from the server.
 */
public class HRClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;

    /**
     * Sends a notification message to the HR server. Opens a socket, sends the
     * message, reads the acknowledgment, then closes. Silently handles
     * connection errors (server may not always be running).
     *
     * @param message the notification text to send
     */
    public static void sendNotification(String message) {
        try (
                Socket socket = new Socket(SERVER_HOST, SERVER_PORT); PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))) {
            // Send notification to server
            writer.println(message);

            // Read server acknowledgment
            String ack = reader.readLine();
            if (ack != null) {
                System.out.println("Server response: " + ack);
            }

        } catch (Exception e) {
            // Server may not be running - log silently, don't crash the app
            System.out.println("Notification skipped (server not running): " + e.getMessage());
        }
    }
}
