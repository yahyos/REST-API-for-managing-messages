package com.qlik.messagingservice.persistence;

import com.qlik.messagingservice.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.isPalindrome = :isPalindrome")
    List<Message> findAllByPalindrome(@Param("isPalindrome") boolean isPalindrome);

}