package ticket.booking;

import java.util.ArrayList;
import java.util.List;
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
            System.err.println("There is something wrong");
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
                    break;

                default:
                    break;
            }
        }
        s.close();
    }
}
