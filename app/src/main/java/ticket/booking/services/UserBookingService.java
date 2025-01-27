package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.utils.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserBookingService {
    private User user;
    private List<User> userList;
    private ObjectMapper object_mapper = new ObjectMapper();
    private final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json";

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUsers();
    }

    public UserBookingService() throws IOException {
        loadUsers();
    }

    public List<User> loadUsers() throws IOException {
        File users = new File(USERS_PATH);
        return userList = object_mapper.readValue(users, new TypeReference<List<User>>() {
        });
    }

    public boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user -> {
            return user.getName().equals(user.getName())
                    && UserServiceUtil.checkPassword(user.getPassword(), user.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public boolean signUp(User user) {
        try {
            userList.add(user);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    public void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        object_mapper.writeValue(usersFile, userList);
    }

    public void fetchBooking() {
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter your ticket Id to cancel");
        ticketId = s.next();
        s.close();
        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("TicketId cannot be empty");
            return Boolean.FALSE;
        }
        String finalTicketId = ticketId;
        boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId));
        if (removed) {
            return Boolean.TRUE;
        } else {
            System.out.println("No ticket found with id: " + ticketId);
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (Exception e) {
            return null;
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int column) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && column >= 0 && column < seats.get(row).size()) {
                if (seats.get(row).get(column) == 0) {
                    seats.get(row).set(column, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

}
