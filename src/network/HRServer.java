package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HRServer - A multi-threaded TCP server that receives HR notifications. Each
 * client connection is handled in a separate thread (ClientHandler),
 * demonstrating concurrent programming with shared state management.
 *
 * Threading concept demonstrated: - Each client gets its own ClientHandler
 * thread (extends Thread) - AtomicInteger used for thread-safe connection
 * counting - Synchronized logging to avoid interleaved output
 */
public class HRServer {

    // Thread-safe counter tracking how many clients have connected
    private static final AtomicInteger connectionCount = new AtomicInteger(0);

    /**
     * ClientHandler - Inner class extending Thread. Each instance handles one
     * client connection independently.
     */
    static class ClientHandler extends Thread {

        private final Socket socket;
        private final int clientId;

        /**
         * Creates a new handler for the given client socket.
         *
         * @param socket the connected client socket
         * @param clientId unique ID for this connection
         */
        public ClientHandler(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
            // Name the thread for easier debugging
            this.setName("HR-Client-" + clientId);
        }

        /**
         * Thread entry point: reads the notification message, logs it, sends
         * acknowledgment back, then closes the connection.
         */
        @Override
        public void run() {
            try (
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream())); PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
                String message = reader.readLine();

                if (message != null) {
                    String timestamp = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    // Synchronized to prevent interleaved console output from multiple threads
                    synchronized (HRServer.class) {
                        System.out.println("[" + timestamp + "] [Thread: " + getName()
                                + "] Notification: " + message);
                    }

                    // Send acknowledgment back to client
                    writer.println("ACK: Message received by server");
                }

            } catch (Exception e) {
                System.out.println("[Thread: " + getName() + "] Error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * Starts the HR notification server on port 5000. Listens indefinitely,
     * spawning a new ClientHandler thread per connection.
     */
    public static void main(String[] args) {
        System.out.println("HR Notification Server starting on port 5000...");

        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("HR Server is running. Waiting for connections...");

            while (true) {
                Socket clientSocket = server.accept();
                int id = connectionCount.incrementAndGet();

                // Each client handled by its own thread - demonstrates multi-threading
                ClientHandler handler = new ClientHandler(clientSocket, id);
                handler.start(); // Non-blocking: main thread immediately returns to accept()

                System.out.println("Client #" + id + " connected. Active threads: "
                        + Thread.activeCount());
            }

        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
