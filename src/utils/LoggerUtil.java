package utils;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {

    public static void log(String fileName, String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
            String timeStamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            out.println("[" + timeStamp + "] " + message);

        } catch (Exception e) {
            System.out.println("Logging error: " + e.getMessage());
        }
    }
}
