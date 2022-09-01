package ru.job4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.model.Post;
import ru.job4j.service.EmailService;
import ru.job4j.service.RandomId;

import java.util.List;

@RestController
@RequestMapping("mail")
public class KafkaMailController {

    @Autowired
    private KafkaTemplate<Integer, Post> template;

    private final EmailService emailService;

    private final RandomId randomId;

    public KafkaMailController(EmailService emailService, RandomId randomId) {
        this.emailService = emailService;
        this.randomId = randomId;
    }

    @PostMapping
    public void sendEmail() {
        List<String> emails = emailService.loadingEmails();
        int id = randomId.randomizeId();
        String message = emailService.getMessage();
        Post post = Post.of(id, message, emails);
        ListenableFuture<SendResult<Integer, Post>> future = template.send("email", id, post);
        future.addCallback(System.out :: println, System.err :: println);
        template.flush();
    }
}
