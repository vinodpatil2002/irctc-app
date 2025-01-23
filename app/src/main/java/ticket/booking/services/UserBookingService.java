package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.User;

import java.io.File;
import java.util.List;

public class UserBookingService {
    private User user;
    private List<User> userList;
    private ObjectMapper object_mapper = new ObjectMapper();
    private static final String USERS_PATH = "../localDb/users.json";
    public UserBookingService(User user){
        this.user = user;
        File users = new File(USERS_PATH);
        userList = object_mapper.readValue(users, new TypeReference<List<User>>() {});
    }


}
