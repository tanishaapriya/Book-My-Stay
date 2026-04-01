/**
 * UseCase2RoomInitialization
 *
 * This program demonstrates object-oriented modeling using abstraction,
 * inheritance, and polymorphism in a Hotel Booking System.
 *
 * It defines different room types and displays their availability.
 *
 * @author Admin
 * @version 2.1
 */

// Abstract class
abstract class Room {
    protected String roomType;
    protected int beds;
    protected double price;

    // Constructor
    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    // Method to display room details
    public void displayRoomDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Price     : $" + price);
    }
}

// Single Room class
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 1000.0);
    }
}

// Double Room class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 1800.0);
    }
}

// Suite Room class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 3000.0);
    }
}

// Main class
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     Welcome to Book My Stay App      ");
        System.out.println("======================================");
        System.out.println("Version : v2.1");
        System.out.println("--------------------------------------");

        // Creating room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display details
        System.out.println("\n--- Room Details & Availability ---\n");

        single.displayRoomDetails();
        System.out.println("Available : " + singleAvailable);
        System.out.println("----------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available : " + doubleAvailable);
        System.out.println("----------------------------------");

        suite.displayRoomDetails();
        System.out.println("Available : " + suiteAvailable);
        System.out.println("----------------------------------");

        System.out.println("Application execution completed.");
    }
}