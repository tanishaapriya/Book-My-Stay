/**
 * UseCase10BookingCancellation
 *
 * This program demonstrates safe booking cancellation in the Book My Stay App.
 * It restores inventory, tracks released room IDs, and updates booking history.
 *
 * @author Admin
 * @version 10.0
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

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory service
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
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

    private RoomInventory inventory;
    private Map<String, Reservation> confirmedBookings = new HashMap<>();
    private Stack<String> rollbackStack = new Stack<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Confirm a booking
    public void confirmBooking(Reservation reservation) {
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            System.out.println("❌ Invalid room type: " + reservation.getRoomType());
            return;
        }

        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            System.out.println("❌ Room not available: " + reservation.getRoomType());
            return;
        }

        // Allocate room
        inventory.decrement(reservation.getRoomType());
        confirmedBookings.put(reservation.getReservationId(), reservation);
        rollbackStack.push(reservation.getReservationId());

        System.out.println("✅ Booking Confirmed for " + reservation.getGuestName() +
                " (" + reservation.getRoomType() + ")");
    }

    // Cancel a booking
    public void cancelBooking(String reservationId) {
        if (!confirmedBookings.containsKey(reservationId)) {
            System.out.println("❌ Cannot cancel: Reservation ID not found: " + reservationId);
            return;
        }

        Reservation res = confirmedBookings.get(reservationId);

        // Restore inventory
        inventory.increment(res.getRoomType());

        // Remove from confirmed bookings
        confirmedBookings.remove(reservationId);

        // Track rollback
        rollbackStack.push(reservationId);

        System.out.println("🔄 Booking Cancelled for " + res.getGuestName() +
                " (" + res.getRoomType() + ")");
    }

    public void displayConfirmedBookings() {
        System.out.println("Confirmed Bookings:");
        if (confirmedBookings.isEmpty()) {
            System.out.println("None");
        } else {
            for (Reservation r : confirmedBookings.values()) {
                System.out.println(r.getReservationId() + " | " +
                        r.getGuestName() + " | " +
                        r.getRoomType());
            }
        }
        System.out.println();
    }

    public void displayRollbackStack() {
        System.out.println("Rollback Stack (recent cancellations/reservations): " + rollbackStack);
        System.out.println();
    }
}

// Main Class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     Welcome to Book My Stay App      ");
        System.out.println("======================================");
        System.out.println("Version : v10.0");
        System.out.println("--------------------------------------");

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Confirm some bookings
        service.confirmBooking(new Reservation("R001", "Alice", "Single Room"));
        service.confirmBooking(new Reservation("R002", "Bob", "Double Room"));
        service.confirmBooking(new Reservation("R003", "Charlie", "Suite Room"));

        inventory.displayInventory();
        service.displayConfirmedBookings();

        // Cancel a booking
        service.cancelBooking("R002");

        inventory.displayInventory();
        service.displayConfirmedBookings();
        service.displayRollbackStack();

        // Attempt invalid cancellation
        service.cancelBooking("R999");

        System.out.println("System continues running safely after cancellations.");
    }
}