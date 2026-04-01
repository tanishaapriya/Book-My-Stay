/**
 * UseCase9ErrorHandlingValidation
 *
 * This program demonstrates validation and error handling
 * using custom exceptions in a booking system.
 *
 * It ensures invalid inputs are caught early and handled gracefully.
 *
 * @author Admin
 * @version 9.0
 */

import java.util.*;

// Custom Exception for invalid booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
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

    public String getGuestName() {
        return guestName;
    }

// Inventory Service with validation
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0); // unavailable
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) throws InvalidBookingException {

        int available = getAvailability(roomType);

        if (available <= 0) {
            throw new InvalidBookingException(
                    "No rooms available for: " + roomType
            );
        }

        inventory.put(roomType, available - 1);
    }
}

// Validator class
class BookingValidator {

    public static void validate(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (reservation.getGuestName() == null ||
                reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException(
                    "Invalid room type: " + reservation.getRoomType()
            );
        }

        // Validate availability
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException(
                    "Room not available: " + reservation.getRoomType()
            );
        }
    }
}

// Booking Service with error handling
class BookingService {

    private BookingHistory history;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation) {

        try {
            // Step 1: Validate input (Fail-Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Allocate room (safe)
            inventory.decrement(reservation.getRoomType());

            // Step 3: Confirm booking
            System.out.println("✅ Booking Confirmed for " +
                    reservation.getGuestName() +
                    " (" + reservation.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("❌ Booking Failed: " + e.getMessage());
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
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     Welcome to Book My Stay App      ");
        System.out.println("======================================");
        System.out.println("Version : v9.0");
        System.out.println("--------------------------------------");

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Test cases (valid + invalid)

        // Valid booking
        service.processBooking(new Reservation("Alice", "Single Room"));

        // Invalid: empty name
        service.processBooking(new Reservation("", "Double Room"));

        // Invalid: wrong room type
        service.processBooking(new Reservation("Bob", "Luxury Room"));

        // Invalid: no availability
        service.processBooking(new Reservation("Charlie", "Suite Room"));

        // Invalid: exceeding availability
        service.processBooking(new Reservation("David", "Single Room"));

        System.out.println("\nSystem continues running safely after errors.");
    }
}