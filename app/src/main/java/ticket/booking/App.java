package ticket.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.utils.UserServiceUtil;

public class App {
    public static void main(String[] args) {
        System.out.println("Welcome To Train Booking System: ");
        Scanner s = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        try {
            userBookingService = new UserBookingService();
        } catch (Exception e) {
            System.err.println("There is something wrong"+e.getMessage());
            e.printStackTrace();
            s.close();
            return;
        }
        while (option != 7) {
            System.out.println("Choose an option");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Booking");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a seat");
            System.out.println("6. Cancel My Booking");
            System.out.println("7. Exit the App");
            option = s.nextInt();
            Train trainSelectedForBooking = new Train();
            switch (option) {
                case 1:
                    System.out.println("Enter Username to signup");
                    String nameToSignUp = s.next();
                    System.out.println("Enter password");
                    String passwordToSignUp = s.next();
                    User userToSignUp = new User(nameToSignUp, passwordToSignUp,
                            UserServiceUtil.hashPassword(passwordToSignUp), new ArrayList<>(),
                            UUID.randomUUID().toString());
                    userBookingService.signUp(userToSignUp);
                    break;
                case 2:
                    System.out.println("Enter username to login");
                    String usernameToLogin = s.next();
                    System.out.println("Enter your password");
                    String passwordToLogin = s.next();
                    User userToLogin = new User(usernameToLogin, passwordToLogin,
                            UserServiceUtil.hashPassword(passwordToLogin), new ArrayList<>(),
                            UUID.randomUUID().toString());
                    try {
                        userBookingService = new UserBookingService(userToLogin);
                    } catch (Exception e) {
                        System.err.println("Maybe there are some error bruh!");
                        return;
                    }
                    break;
                case 3:
                    System.out.println("These are your bookings");
                    userBookingService.fetchBooking();
                    break;
                case 4:
                    System.out.println("Booking: ");
                    System.out.println("Enter your source station: ");
                    String source = s.next();
                    System.out.println("Enter your destination station: ");
                    String destination = s.next();
                    List<Train> trains = userBookingService.getTrains(source, destination);
                    int index = 1;
                    for (Train t : trains) {
                        System.out.println(index + " Train id: " + t.getTrainId());
                        for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                            System.out.println("Station " + entry.getKey() + " time: " + entry.getValue());
                        }
                    }
                    System.out.println("Select a train by typing 1,2,3....");
                    trainSelectedForBooking = trains.get(s.nextInt());
                    break;
                case 5:
                    System.out.println("Select a seat out of these seats");
                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                    for (List<Integer> row : seats) {
                        for (Integer val : row) {
                            System.out.println(val + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("Select the seat by typing the row and column");
                    System.out.println("Enter the row");
                    int row = s.nextInt();
                    System.out.println("Great! Now enter the column");
                    int column = s.nextInt();
                    System.out.println("Thanks.. Booking your seat....");
                    Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, column);
                    if (booked.equals(Boolean.TRUE)) {
                        System.out.println("Tickets Booked Successfully, Happy Journey<3");
                    } else {
                        System.out.println("Uh Ohh!! Can't Book this seat, try selecting another one! ");
                    }
                    break;
                case 6:
                    System.out.println("Are you sure about cancelling your ticket ?? (y/n)");
                    char choice = s.next().charAt(1);
                    if (choice == 'y' || choice == 'Y') {
                        System.out.println("Enter your ticket ID:");
                        String ticketId = s.next();
                        boolean cancelled = userBookingService.cancelBooking(ticketId);
                        if (cancelled) {
                            System.out.println("Ticket cancelled successfully!");
                        } else {
                            System.out.println("Failed to cancel ticket. Please verify the ticket ID.");
                        }
                    }
                    break;
                case 7:
                    System.err.println("Ending your session.! ");
                    break;
                default:
                    break;
            }
        }
        s.close();
    }

    public String getGreeting() {
        return "Hello, Gradle!";
    }
}
