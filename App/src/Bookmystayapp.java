/**
 * UseCase6RoomAllocationService
 *
 * This program processes booking requests from a queue,
 * allocates rooms, ensures unique room IDs, and updates inventory.
 *
 * It prevents double-booking using Set and maintains consistency.
 *
 * @author Admin
 * @version 6.0
 */

import java.util.*;

// Reservation (same as UC5)
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
}

// Inventory Service
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Updated Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking Request Queue
class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Booking Service (Core Logic)
class BookingService {

    private RoomInventory inventory;

    // Track all allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type -> assigned room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Generate unique room ID
    private String generateRoomId(String roomType, int number) {
        return roomType.replace(" ", "").substring(0, 2).toUpperCase() + number;
    }

    public void processBooking(Reservation reservation) {

        String type = reservation.getRoomType();

        // Check availability
        if (inventory.getAvailability(type) <= 0) {
            System.out.println("❌ No rooms available for " + type +
                    " (Guest: " + reservation.getGuestName() + ")");
            return;
        }

        // Generate unique ID
        String roomId;
        int counter = 1;

        do {
            roomId = generateRoomId(type, counter++);
        } while (allocatedRoomIds.contains(roomId));

        // Store allocation
        allocatedRoomIds.add(roomId);

        roomAllocations
                .computeIfAbsent(type, k -> new HashSet<>())
                .add(roomId);

        // Update inventory immediately
        inventory.decrement(type);

        // Confirm booking
        System.out.println("✅ Booking Confirmed!");
        System.out.println("Guest  : " + reservation.getGuestName());
        System.out.println("Room   : " + type);
        System.out.println("RoomID : " + roomId);
        System.out.println("----------------------------------");
    }
}

// Main Class
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     Welcome to Book My Stay App      ");
        System.out.println("======================================");
        System.out.println("Version : v6.0");
        System.out.println("--------------------------------------");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingService bookingService = new BookingService(inventory);

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("Diana", "Double Room"));

        // Process queue
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processBooking(r);
        }

        // Show final inventory
        inventory.displayInventory();

        System.out.println("\nAll bookings processed with no double-booking.");
    }
}