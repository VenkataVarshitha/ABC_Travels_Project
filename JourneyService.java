package com.service;
//Manages journey-related operations like planning a journey, generating bills, and rescheduling journeys. It leverages the Route and Order classes to facilitate these operations.
import com.model.Journey;
import com.model.Order;
import com.model.Route;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.DayOfWeek;

public class JourneyService {
    private List<Route> routes;
    private List<Order> orders;

    public JourneyService(List<Route> routes, List<Order> orders) {
        this.routes = routes;
        this.orders = orders;
    }

        public void planJourney() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n Plan Journey");

            // Getting journey details from the user
            System.out.print("Enter source: ");
            String source = scanner.nextLine();

            System.out.print("Enter destination: ");
            String destination = scanner.nextLine();

            System.out.print("Enter journey date (YYYY-MM-DD): ");
            String journeyDateString = scanner.nextLine();
            LocalDate journeyDate = LocalDate.parse(journeyDateString, DateTimeFormatter.ISO_LOCAL_DATE);

            System.out.print("Enter number of passengers: ");
            int noOfPassengers = scanner.nextInt();
            scanner.nextLine(); // Consume the leftover newline

            // Finding matching routes
            List<Route> matchingRoutes = getRoutes(source, destination, journeyDate, noOfPassengers);
            if (!matchingRoutes.isEmpty()) {
                System.out.println("Available Routes: ");
                for (int i = 0; i < matchingRoutes.size(); i++) {
                    System.out.println((i + 1) + ": " + matchingRoutes.get(i));
                }
                System.out.print("Select a route (number): ");
                int routeNumber = scanner.nextInt();
                Route selectedRoute = matchingRoutes.get(routeNumber - 1);
                
                // Creating an order
                Order newOrder = createOrder(journeyDate, noOfPassengers, selectedRoute);
                orders.add(newOrder);
                System.out.println("Journey planned successfully. Order details: " + newOrder);
            } else {
                System.out.println("No available routes found for the given details.");
            }
        }

        public void reScheduleJourney() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nRe-Schedule Journey");

            System.out.print("Enter your Order ID: ");
            int orderId = scanner.nextInt();
            scanner.nextLine(); // Consume the leftover newline

            // Find the order by ID
            Order orderToReschedule = findOrderById(orderId);
            if (orderToReschedule != null) {
                System.out.print("Enter new journey date (YYYY-MM-DD): ");
                String newDateStr = scanner.nextLine();
                LocalDate newDate = LocalDate.parse(newDateStr, DateTimeFormatter.ISO_LOCAL_DATE);

                // Check if the route is available for the new date
                List<Route> availableRoutes = getRoutes(orderToReschedule.getRoute().getSource(), 
                                                        orderToReschedule.getRoute().getDestination(), 
                                                        newDate, 
                                                        orderToReschedule.getRequestedJourneyPlan().getNumberOfPassengers());

                if (!availableRoutes.isEmpty()) {
                    // Update the journey date
                    orderToReschedule.getRequestedJourneyPlan().setJourneyDate(newDate);
                    System.out.println("Journey rescheduled successfully. Updated order details: " + orderToReschedule);
                } else {
                    System.out.println("No available routes for the new journey date.");
                }
            } else {
                System.out.println("Order not found.");
            }
        }


   
        private List<Route> getRoutes(String source, String destination, LocalDate date, int numberOfPassengers) {
            List<Route> matchingRoutes = new ArrayList<>();
            for (Route route : this.routes) {
                if (route.getSource().equals(source) &&
                    route.getDestination().equals(destination) &&
                    route.getJourneyDate().isEqual(date) &&
                    route.getNoOfSeatsAvailable() >= numberOfPassengers) {
                    matchingRoutes.add(route);
                }
            }
            return matchingRoutes;
        }

        private Order createOrder(LocalDate date, int passengers, Route selectedRoute) {
            Order newOrder = new Order();
            double bookingCost = selectedRoute.getTicketPricePerPassenger() * passengers;

            // Adding surge pricing for weekends
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                bookingCost += 200; // Weekend surge
                bookingCost += bookingCost * 0.1; // Adding 10% GST
            }

            newOrder.setOrderAmount(bookingCost);
            newOrder.setRoute(selectedRoute);
            newOrder.setRequestedJourneyPlan(new Journey(date, passengers)); // Assuming Journey constructor takes date and passengers
            newOrder.setOrderStatus("created");
            newOrder.setOrderId(orders.size() + 1); // Simple way to generate a unique order ID

            return newOrder;
        }
        private Order findOrderById(int orderId) {
            for (Order order : orders) {
                if (order.getOrderId() == orderId) {
                    return order;
                }
            }
            return null; // Return null if no order is found with the given ID
        }
}
