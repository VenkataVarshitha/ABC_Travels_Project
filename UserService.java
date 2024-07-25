package com.service;
//Manages user-related operations including new admin registration and user login. It also handles the tracking of invalid login attempts and locks the account after 5 failed attempts.
import com.model.User;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserService {
    private List<User> users;
    private Map<String, Integer> userInvalidLoginAttempt;

    public UserService(List<User> users,Map<String, Integer> userInvalidLoginAttempt) {
    	this.users = users;
    	this.userInvalidLoginAttempt = userInvalidLoginAttempt;
    	}
    	public void registerNewAdmin() {
    	    Scanner scanner = new Scanner(System.in);
    	    System.out.println("\nNew Admin User Registration");

    	    System.out.print("Enter first name: ");
    	    String firstName = scanner.nextLine();

    	    System.out.print("Enter last name: ");
    	    String lastName = scanner.nextLine();

    	    System.out.print("Enter mobile number: ");
    	    String mobileNumber = scanner.nextLine();

    	    System.out.print("Enter gender: ");
    	    String gender = scanner.nextLine();

    	    System.out.print("Enter email: ");
    	    String email = scanner.nextLine();

    	    System.out.print("Enter password: ");
    	    String password = scanner.nextLine();

    	   
    	    if (isUserExists(email)) {
    	        System.out.println("User with this email: " + email + " already exists");
    	        return;
    	    }

    	    User newUser = new User(firstName, lastName, mobileNumber, gender, email, password, 0, "Active");
    	    users.add(newUser);
    	    System.out.println("Registration successful!");
    	}

    	private boolean isUserExists(String email) {
    	    for (User user : users) {
    	        if (user.getEmail().equals(email)) {
    	            return true;
    	        }
    	    }
    	    return false;
    	}

    	
    	public User login() {
    	    Scanner scanner = new Scanner(System.in);
    	    System.out.println("\n User Login");

    	    System.out.print("Enter email: ");
    	    String email = scanner.nextLine();

    	    System.out.print("Enter password: ");
    	    String password = scanner.nextLine();

    	    for (User user : users) {
    	        if (user.getEmail().equals(email)) {
    	            Integer existingCount = userInvalidLoginAttempt.getOrDefault(email, 0);
    	            if (existingCount >= 5) {
    	                System.out.println("User account is locked due to multiple invalid login attempts.");
    	                return null;
    	            }
    	            if (user.getPassword().equals(password)) {
    	                System.out.println("\n Login Successful");
    	                userInvalidLoginAttempt.put(email, 0); // Reset count after successful login
    	                return user;
    	            } else {
    	                userInvalidLoginAttempt.put(email, existingCount + 1);
    	                System.out.println("\n Invalid Credentials. Attempt: " + (existingCount + 1) + " for email: " + email);
    	       
    	            }
    	        }
    	    }
    	    System.out.println("No user found with email: " + email);
    	    return null;
    	}
    	}

