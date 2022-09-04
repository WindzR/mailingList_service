package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.model.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    private static final String DEFAULT_MESSAGE = "Test message sent to ";

    private final RandomId randomId;

    public EmailService(final RandomId randomId) {
        this.randomId = randomId;
    }

    public Post preparationPost() {
        List<String> emails = loadingEmails();
        int id = randomId.randomizeId();
        return Post.of(id, DEFAULT_MESSAGE, emails);
    }

    public List<String> loadingEmails() {
        List<String> emails = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(
                new FileReader("src/main/resources/emails.properties"))) {
            in.lines().forEach(emails :: add);
            System.out.println(emails);
        } catch (IOException ex) {
            System.out.println("File not found!");
            ex.printStackTrace();
        }
        return emails;
    }

    public String getMessage() {
        return DEFAULT_MESSAGE;
    }
}
