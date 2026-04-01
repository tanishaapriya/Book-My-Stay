/**
 * UseCase8BookingHistoryReport
 *
 * This program demonstrates storing confirmed bookings
 * and generating reports from booking history.
 *
 * It uses List to maintain insertion order (chronological history)
 * and separates storage from reporting logic.
 *
 * @author Admin
 * @version 8.0
 */

import java.util.*;

// Reservation class (confirmed booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Room Type      : " + roomType);
        System.out.println("----------------------------------");
    }
}

// Booking History (Data Storage)
class BookingHistory {

    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Reservation stored: " + reservation.getReservationId());
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return history; // read-only usage expected
    }
}

// Reporting Service
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all bookings
    public void displayAllBookings() {

        System.out.println("\n=== Booking History (Chronological) ===\n");

        List<Reservation> reservations = history.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummary() {

        System.out.println("\n=== Booking Summary Report ===\n");

        List<Reservation> reservations = history.getAllReservations();

        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomCount.put(
                    r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        for (Map.Entry<String, Integer> entry : roomCount.entrySet()) {
            System.out.println("Room Type : " + entry.getKey() +
                    " | Bookings : " + entry.getValue());
        }

        System.out.println("----------------------------------");
        System.out.println("Total Bookings : " + reservations.size());
    }
}

// Main Class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     Welcome to Book My Stay App      ");
        System.out.println("======================================");
        System.out.println("Version : v8.0");
        System.out.println("--------------------------------------");

        // Initialize history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from UC6)
        history.addReservation(new Reservation("SR1", "Alice", "Single Room"));
        history.addReservation(new Reservation("SR2", "Bob", "Single Room"));
        history.addReservation(new Reservation("DR1", "Charlie", "Double Room"));
        history.addReservation(new Reservation("SU1", "Diana", "Suite Room"));

        // Initialize reporting service
        BookingReportService reportService = new BookingReportService(history);

        // Display booking history
        reportService.displayAllBookings();

        // Generate summary report
        reportService.generateSummary();

        System.out.println("\nReporting completed successfully.");
        System.out.println("Booking history remains unchanged (read-only reporting).");
    }
}