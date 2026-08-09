package utils;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LoggerUtil - IOStream utility for writing timestamped log entries to files.
 * Demonstrates Java IO Streams: FileWriter (character stream) wrapped with
 * PrintWriter. All log entries are appended to the specified file (append mode
 * = true).
 *
 * Example log files used: - login.txt : records user login/logout events -
 * employees.txt: records employee add/delete events - contract.txt : records
 * contract creation events
 */
public class LoggerUtil {

    private static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Appends a timestamped message to the specified log file. Creates the file
     * if it does not exist; appends if it does.
     *
     * @param fileName the name/path of the log file (e.g., "login.txt")
     * @param message the message to log
     */
    public static void log(String fileName, String message) {
        // Try-with-resources ensures the stream is always closed (IOStream best practice)
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            out.println("[" + timestamp + "] " + message);
        } catch (Exception e) {
            System.out.println("Logging error: " + e.getMessage());
        }
    }
}
