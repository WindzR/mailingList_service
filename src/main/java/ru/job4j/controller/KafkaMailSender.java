package ru.job4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.model.MessageDTO;
import ru.job4j.model.Post;
import ru.job4j.service.EmailService;
import ru.job4j.service.RandomId;

import java.util.List;

@RestController
@RequestMapping("mail")
public class KafkaMailSender {

    @Autowired
    private KafkaTemplate<Integer, Post> template;

    private final EmailService emailService;

    private final RandomId randomId;

    public KafkaMailSender(EmailService emailService, RandomId randomId) {
        this.emailService = emailService;
        this.randomId = randomId;
    }

    /**
     * Отправляет письмо Post с тестовым сообщением,
     * использует топик email
     */
    @PostMapping("/test")
    public void sendTest() {
        Post post = emailService.preparationPost();
        ListenableFuture<SendResult<Integer, Post>> future
                = template.send("email", post.getId(), post);
        future.addCallback(System.out :: println, System.err :: println);
        template.flush();
    }

    /**
     * Отправляет письмо Post с сообщением MessageDTO, полученным от 'passport_service'
     * о просроченности паспортов
     * использует топик unavailable_passports
     */
    public void sendMessageToEmail(MessageDTO messageDTO) {
        Post post = emailService.preparationPost();
        post.setMessage(messageDTO.getMessage());
        ListenableFuture<SendResult<Integer, Post>> future
                = template.send("unavailable_passports", post.getId(), post);
        future.addCallback(System.out :: println, System.err :: println);
        template.flush();
    }
}
