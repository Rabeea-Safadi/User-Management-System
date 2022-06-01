package com.rabeeasafadi.MongoDB.Application.user;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
public class UserService {

    /**
     * Connects to MongoDB Atlas
     * Container of all REST end-points
    **/

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private String connectionString;

    public UserService() {
        /**
         * Makes the connection to MongoDB using the connection string
        **/

        // TODO: 06/01/2022 (Rabeea)
        // Add connection string to a local file to hide it from public use (GitHub)
        // Create a new url.txt file
        // Read url.txt using a Scanner object
        // Add url.txt to .gitignore

        try {
            // Gets current working directory and accesses the file `connectionString.txt` which contains the connection url to MongoAtlas
            String path = System.getProperty("user.dir") + "\\connectionString.txt";

            try (Scanner in = new Scanner(new File(path))) {
                connectionString = in.nextLine();
            }

            client = MongoClients.create(connectionString);
            database = client.getDatabase("springdb");
            collection = database.getCollection("users");
        }
        catch (Exception e) {

        }
    }

    // TODO: 06/01/2022 (Rabeea)
    // Create new ResponseMessage class
    // Contains the message to return with the JSON response
    // ------------------------
    // ____ResponseMessage_____
    // - sender: String
    // - message: String
    // - status: int
    // ------------------------

    @GetMapping("/users")
    @CrossOrigin
    public List<User> getUsers() {
        /**
         * Returns a List of users in JSON format, using Spring's Jackson library
        **/

        List<User> users = new ArrayList<>();

        try {
            collection.find()
                    .map(document -> new User(document.get("_id").toString(), document.get("name").toString()))
                    .forEach(user -> users.add(user));
        }
        catch (Exception e) {
            return null;
        }

        return users;
    }

    @PostMapping("/users/add")
    @CrossOrigin
    public String addUser(@RequestBody User user) {
        User newUser = new User(user.getName());

        // Checks the validity of the POST body object
        if (newUser.getName().length() == 0) {
            return "Invalid name";
        }

        try {
            collection.insertOne(newUser.toBsonDocument());

            return "Added successfully";
        }
        catch (Exception e) {
            return "There was an error, " + e.getMessage();
        }
    }

    @DeleteMapping("/users/delete")
    @CrossOrigin
    public String deleteUser(@RequestParam(value = "id", defaultValue = "") String id) {
        // in case we got a blank id, we return the following string.
        if (id.isBlank()) {
            return "There was an error, invalid id";
        }

        // if the id is valid, we look it up and try to delete it.
        try {
            collection.deleteOne(Filters.eq("_id", new ObjectId(id)));

            return "Deleted successfully";
        }
        catch (Exception e) {
            return "There was an error, " + e.getMessage();
        }
    }
}
