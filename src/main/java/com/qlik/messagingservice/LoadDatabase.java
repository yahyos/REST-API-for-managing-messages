package com.qlik.messagingservice;

import com.qlik.messagingservice.model.Message;
import com.qlik.messagingservice.persistence.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;


@Configuration
@Slf4j
class LoadDatabase {

    @Autowired
    private MessageRepository messageRepository;

    @Bean
    CommandLineRunner initDatabase(MessageRepository repository) {
        return args -> {

            for (int i = 0; i < 3; i++) {
                Message m = new Message("Message " + (i + 1));
                m.setDateCreated(LocalDateTime.now());
                m.setDateUpdated(LocalDateTime.now());
                messageRepository.save(m);
            }

            Message m = new Message("level");
            m.setDateCreated(LocalDateTime.now());
            m.setDateUpdated(LocalDateTime.now());
            m.setPalindrome(true);
            messageRepository.save(m);

        };
    }
}