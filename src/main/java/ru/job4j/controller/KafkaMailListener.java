package ru.job4j.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.job4j.model.MessageDTO;

@Component
@KafkaListener(topics = "passport_service")
public class KafkaMailListener {

    private final KafkaMailSender mailSender;

    public KafkaMailListener(final KafkaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Слушает топик "passport_service" и принимает объект MessageDTO,
     * и отсылает сообщение, которое он содержит на заданные адреса почты
     * @param record объект из Kafka
     */
    @KafkaListener(topics = "passport_service",
        containerFactory = "messageKafkaListenerContainerFactory")
    public void sendUnavailablePassports(ConsumerRecord<Integer, MessageDTO> record) {
        System.out.println(record.partition());
        System.out.println(record.key());
        System.out.println(record.value());
        MessageDTO message = record.value();
        mailSender.sendMessageToEmail(message);
    }

    @KafkaHandler
    public void handleMessageDTO(MessageDTO messageDTO) {
        System.out.println("MessageDTO received: " + messageDTO);
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Unknown type received: " + object);
    }
}
