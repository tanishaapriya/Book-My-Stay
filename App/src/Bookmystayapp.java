/**
 * UseCase5BookingRequestQueue
 *
 * This program demonstrates handling booking requests using
 * a Queue (FIFO - First Come First Served).
 *
 * Booking requests are stored and processed in arrival order.
 * No inventory updates or room allocation happens at this stage.
 *
 * @author Admin
 * @version 5.0
 */

import java.util.LinkedList;
import java.util.Queue;

// Reservation class (represents a booking request)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest Name : " + guestName);
        System.out.println("Room Type  : " + roomType);
    }
}

// Booking Request Queue class
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View all queued requests
    public void displayRequests() {
        System.out.println("\n--- Booking Requests Queue (FIFO Order) ---\n");

        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        for (Reservation r : requestQueue) {
            r.displayReservation();
            System.out.println("----------------------------------");
        }
    }
}

// Main class
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     Welcome to Book My Stay App      ");
        System.out.println("======================================");
        System.out.println("Version : v5.0");
        System.out.println("--------------------------------------");

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulating booking requests (arrival order matters)
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Diana", "Single Room"));

        // Display queued requests
        bookingQueue.displayRequests();

        System.out.println("All requests are stored in arrival order.");
        System.out.println("No allocation done yet (FIFO will be applied in next use case).");
    }
}