package com.rabeeasafadi.MongoDB.Application.user;

import lombok.Data;
import org.bson.Document;

@Data
public class User {
    /**
     * User class to represent a User object from MongoDB
    **/

    private String _id;
    private String name;

    public User(String name) {
        this.name = name;
    }

    public User(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public Document toBsonDocument() {
        return new Document("name", name);
    }
}
