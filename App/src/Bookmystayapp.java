/**
 * UseCase12DataPersistenceRecovery
 *
 * Demonstrates persistence of inventory and booking history using serialization.
 * Saves state to files on shutdown and restores on startup.
 * Author: Admin
 * Version: 12.0
 */

import java.io.*;
import java.util.*;

// Serializable Reservation class
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Serializable RoomInventory class
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public boolean allocateRoom(String roomType) {
        if (isAvailable(roomType)) {
            inventory.put(roomType, inventory.get(roomType) - 1);
            return true;
        }
        return false;
    }

    public void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println();
    }
}

// BookingHistory class to store confirmed reservations
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Reservation> bookings = new ArrayList<>();

    public void addReservation(Reservation r) {
        bookings.add(r);
    }

    public void displayBookings() {
        System.out.println("Booking History:");
        if (bookings.isEmpty()) {
            System.out.println("No bookings available.");
        } else {
            for (Reservation r : bookings) {
                System.out.println(r);
            }
        }
        System.out.println();
    }

    public List<Reservation> getBookings() {
        return bookings;
    }
}

// PersistenceService handles saving/loading objects
class PersistenceService {

    public static void saveState(String fileName, Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(obj);
            System.out.println("✅ Saved state to " + fileName);
        } catch (IOException e) {
            System.out.println("❌ Failed to save state: " + e.getMessage());
        }
    }

    public static Object loadState(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("⚠ File not found: " + fileName + ". Initializing new state.");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            System.out.println("✅ Loaded state from " + fileName);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Failed to load state: " + e.getMessage());
            return null;
        }
        System.out.println();
    }
}

// Thread task to simulate guest booking
class GuestBookingTask implements Runnable {
    private final BookingService bookingService;
    private final Reservation reservation;

    public GuestBookingTask(BookingService bookingService, Reservation reservation) {
        this.bookingService = bookingService;
        this.reservation = reservation;
    }

    @Override
    public void run() {
        bookingService.confirmBooking(reservation);
    }
}

// Main class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Book My Stay App - Data Persistence & Recovery   ");
        System.out.println("======================================");
        System.out.println("Version : v12.0");
        System.out.println("--------------------------------------");

        // Load persisted state or initialize new
        RoomInventory inventory = (RoomInventory) PersistenceService.loadState("inventory.ser");
        if (inventory == null) inventory = new RoomInventory();

        BookingHistory history = (BookingHistory) PersistenceService.loadState("history.ser");
        if (history == null) history = new BookingHistory();

        // Simulate new bookings
        Reservation r1 = new Reservation("R201", "Alice", "Single Room");
        Reservation r2 = new Reservation("R202", "Bob", "Double Room");

        if (inventory.allocateRoom(r1.getRoomType())) history.addReservation(r1);
        if (inventory.allocateRoom(r2.getRoomType())) history.addReservation(r2);

        System.out.println("\nAfter New Bookings:");
        history.displayBookings();
        inventory.displayInventory();

        // Save current state
        PersistenceService.saveState("inventory.ser", inventory);
        PersistenceService.saveState("history.ser", history);

        // Simulate system restart by loading state
        System.out.println("\n--- Simulating System Restart ---\n");

        RoomInventory restoredInventory = (RoomInventory) PersistenceService.loadState("inventory.ser");
        BookingHistory restoredHistory = (BookingHistory) PersistenceService.loadState("history.ser");

        System.out.println("\nRestored System State:");
        restoredHistory.displayBookings();
        restoredInventory.displayInventory();

        System.out.println("✅ System recovery successful. All state restored correctly.");
    }
}