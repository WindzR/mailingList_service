package ru.job4j.model;

import lombok.Data;

import java.util.List;

@Data
public class Post {

    private int id;

    private String message;

    private List<String> emails;

    public static Post of(int id, String message, List<String> emails) {
        Post post = new Post();
        post.setId(id);
        post.setMessage(message);
        post.setEmails(emails);
        return post;
    }

}
