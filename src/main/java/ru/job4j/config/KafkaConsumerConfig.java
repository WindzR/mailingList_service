package ru.job4j.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.job4j.model.MessageDTO;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServer;

    @Value("${spring.kafka.consumer.group-id}")
    private String kafkaGroupId;

    @Bean
    public KafkaListenerContainerFactory<?> messageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, MessageDTO> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messageConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, MessageDTO> messageConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new IntegerDeserializer(),
                new JsonDeserializer<>(MessageDTO.class, false)
        );
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.KEY_DEFAULT_TYPE, "java.lang.Integer");
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS,
                JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.job4j.model.MessageDTO");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.job4j");
        return props;
    }
}
