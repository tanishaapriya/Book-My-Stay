/**
 * UseCase7AddOnServiceSelection
 *
 * This program demonstrates how add-on services can be attached
 * to a reservation without modifying core booking logic.
 *
 * It uses Map<String, List<Service>> to maintain a one-to-many
 * relationship between reservation and services.
 *
 * @author Admin
 * @version 7.0
 */

import java.util.*;

// Service class (Add-On)
class Service {
    private String serviceName;
    private double price;

    public Service(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void displayService() {
        System.out.println("Service : " + serviceName + " | Cost : $" + price);
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<Service>> serviceMap = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, Service service) {

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added service '" + service.getServiceName()
                + "' to Reservation ID: " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {

        List<Service> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services for Reservation ID: " + reservationId);
            return;
        }

        System.out.println("\n--- Add-On Services for Reservation ID: " + reservationId + " ---");

        for (Service s : services) {
            s.displayService();
        }
    }

    // Calculate total cost of services
    public double calculateTotalCost(String reservationId) {

        List<Service> services = serviceMap.get(reservationId);

        double total = 0;

        if (services != null) {
            for (Service s : services) {
                total += s.getPrice();
            }
        }

        return total;
    }
}

// Main Class
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     Welcome to Book My Stay App      ");
        System.out.println("======================================");
        System.out.println("Version : v7.0");
        System.out.println("--------------------------------------");

        // Simulated reservation IDs (from Use Case 6)
        String reservation1 = "SR1";
        String reservation2 = "DR1";

        // Create service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Define available services
        Service breakfast = new Service("Breakfast", 200.0);
        Service wifi = new Service("WiFi", 100.0);
        Service spa = new Service("Spa Access", 500.0);

        // Add services to reservations
        manager.addService(reservation1, breakfast);
        manager.addService(reservation1, wifi);

        manager.addService(reservation2, spa);

        // Display services
        manager.displayServices(reservation1);
        System.out.println("Total Add-On Cost : $" +
                manager.calculateTotalCost(reservation1));

        System.out.println("--------------------------------------");

        manager.displayServices(reservation2);
        System.out.println("Total Add-On Cost : $" +
                manager.calculateTotalCost(reservation2));

        System.out.println("\nAdd-on services processed successfully.");
        System.out.println("Core booking and inventory remain unchanged.");
    }
}