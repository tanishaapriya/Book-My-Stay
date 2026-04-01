/**
 * UseCase11ConcurrentBookingSimulation
 *
 * Simulates multiple guests submitting booking requests concurrently.
 * Demonstrates thread safety, synchronized access to inventory and booking allocation.
 *
 * Author: Admin
 * Version: 11.0
 */

import java.util.*;

// Reservation class
class Reservation {
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
}

// Thread-safe Room Inventory
class RoomInventory {
    private final Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public synchronized boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public synchronized boolean allocateRoom(String roomType) {
        if (isAvailable(roomType)) {
            inventory.put(roomType, inventory.get(roomType) - 1);
            return true;
        }
        return false;
    }

    public synchronized void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public synchronized void displayInventory() {
        System.out.println("Current Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println();
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }
}

    public void displayInventory() {
        System.out.println("Current Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println();
    }
}

// Booking Service
class BookingService {
    private final RoomInventory inventory;
    private final Map<String, Reservation> confirmedBookings = new HashMap<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Synchronized booking to ensure thread safety
    public synchronized boolean confirmBooking(Reservation reservation) {
        if (!inventory.isAvailable(reservation.getRoomType())) {
            System.out.println("❌ " + reservation.getGuestName() + " failed to book " + reservation.getRoomType() + " (not available)");
            return false;
        }

        if (confirmedBookings.containsKey(reservation.getReservationId())) {
            System.out.println("❌ Reservation ID already exists: " + reservation.getReservationId());
            return false;
        }

        // Allocate room
        inventory.allocateRoom(reservation.getRoomType());
        confirmedBookings.put(reservation.getReservationId(), reservation);
        System.out.println("✅ Booking Confirmed for " + reservation.getGuestName() + " (" + reservation.getRoomType() + ")");
        return true;
    }

    public synchronized void displayConfirmedBookings() {
        System.out.println("Confirmed Bookings:");
        if (confirmedBookings.isEmpty()) {
            System.out.println("None");
        } else {
            for (Reservation r : confirmedBookings.values()) {
                System.out.println(r.getReservationId() + " | " + r.getGuestName() + " | " + r.getRoomType());
            }
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
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Book My Stay App - Concurrent Booking Simulation   ");
        System.out.println("======================================");
        System.out.println("Version : v11.0");
        System.out.println("--------------------------------------");

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Create reservations
        Reservation r1 = new Reservation("R101", "Alice", "Single Room");
        Reservation r2 = new Reservation("R102", "Bob", "Single Room");
        Reservation r3 = new Reservation("R103", "Charlie", "Single Room");
        Reservation r4 = new Reservation("R104", "David", "Double Room");
        Reservation r5 = new Reservation("R105", "Eve", "Suite Room");

        // Create threads simulating multiple guests booking at the same time
        Thread t1 = new Thread(new GuestBookingTask(service, r1));
        Thread t2 = new Thread(new GuestBookingTask(service, r2));
        Thread t3 = new Thread(new GuestBookingTask(service, r3));
        Thread t4 = new Thread(new GuestBookingTask(service, r4));
        Thread t5 = new Thread(new GuestBookingTask(service, r5));

        // Start threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        // Wait for all threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nFinal Booking Status:");
        service.displayConfirmedBookings();
        inventory.displayInventory();

        System.out.println("✅ All concurrent booking requests processed safely.");
    }
}