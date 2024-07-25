package com;

import com.service.UserService;
import com.service.JourneyService;
import com.model.User;
import com.model.Route;
import com.model.Order;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static List<User> users = new ArrayList<>();
    private static List<Route> routes = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();
    private static Map<String, Integer> userInvalidLoginAttempt = new HashMap<>();
    private static UserService userService = new UserService(users, userInvalidLoginAttempt);
    private static JourneyService journeyService = new JourneyService(routes, orders);

    public static void main(String[] args) {
        // Pre-populate the services with some data if necessary
    	initializeRoutes();

        if (displayCompanyLogo()) {
            showMenuOptions();
        } else {
            System.out.println("Failed to load company logo. Exiting...");
        }
    }

    private static boolean displayCompanyLogo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\company_logo.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            return true; // Logo loaded successfully
        } catch(IOException e) {
            System.err.println("Error reading company logo file: " + e.getMessage());
            return false; // Logo loading failed
        }
    }
    private static void initializeRoutes() {
        routes.add(new Route(1, "Nellore", "Hyderabad", LocalDate.parse("2024-01-20", DateTimeFormatter.ISO_LOCAL_DATE), 1000, 40));
        routes.add(new Route(2, "Hyderabad", "Goa", LocalDate.parse("2024-01-19", DateTimeFormatter.ISO_LOCAL_DATE), 1500, 40));
        // ... add more routes as needed
    }


    private static void showMenuOptions() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        boolean running = true;

        while (running) {
            System.out.println("\nMenu Options:");
            System.out.println("1. New Admin User Registration");
            System.out.println("2. Login");
            System.out.println("3. Plan journey");
            System.out.println("4. Reschedule booking date");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
            case 1:
                userService.registerNewAdmin();
                break;
            case 2:
                userService.login();
                break;
            case 3:
                journeyService.planJourney();
                break;
            case 4:
                journeyService.reScheduleJourney();
                break;
            case 5:
                System.out.println("Exiting...");
                running = false;
                break;
            default:
                System.out.println("Invalid choice. Please enter a correct option.");
                break;
        }
    }
        

    scanner.close();  // Close the scanner when we're done with it
}

}
