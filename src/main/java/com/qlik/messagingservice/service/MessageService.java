package com.qlik.messagingservice.service;

import com.qlik.messagingservice.exception.*;
import com.qlik.messagingservice.model.Message;
import com.qlik.messagingservice.persistence.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    MessageRepository repository;

    public Message getMessageById(long id){
        Optional<Message> m = repository.findById(id);
        if(!m.isPresent()){
            throw new MessageNotFoundException(id);
        }
        return m.get();
    }

    public boolean validateContent(String content) {

        if(content == null || content.trim().length() == 0){
            throw new MissingContentException();
        }

        //Validate content length
        int contentLength = content.length();
        if(contentLength < 1 || (contentLength > Message.MAX_LENGTH)){
            throw new InvalidContentLengthException();
        }

        //Validate content characters
        final Pattern pattern = Pattern.compile("^[a-zA-Z0-9 àâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ]*$");
        if (!pattern.matcher(content).matches()) {
            throw new InvalidContentCharactersException();
        }

        // if it passes all validation layers, return true
        return true;
    }


    public boolean validateRequestBody(Map<String, Object> mapRequestBody) {

        if(mapRequestBody.size() != 1 || mapRequestBody.get("content") == null){
            throw new InvalidRequestBodyException();
        }

        return true;
    }

    public Message saveNewMessage(Message newMessage){
        newMessage.setDateCreated(LocalDateTime.now());
        newMessage.setDateUpdated(LocalDateTime.now());
        boolean isPalindrome = isPalindrome(newMessage.getContent());
        newMessage.setPalindrome(isPalindrome);
        return repository.save(newMessage);
    }

    public Message updateMessage(Message oldMessage, String newContent){
        oldMessage.setDateUpdated(LocalDateTime.now()); //update dateUpdated
        oldMessage.setContent(newContent);
        boolean isPalindrome = isPalindrome(newContent);
        oldMessage.setPalindrome(isPalindrome);
        return repository.save(oldMessage);
    }

    public void deleteMessage(Message messageToDelete){
        repository.delete(messageToDelete);
    }

    public boolean isPalindrome(String messageContent) {
        //Spaces and case sensitivity should not affect palindrome algorithm
        String sanitizedContent = messageContent.replaceAll("\\s+", "").toLowerCase();
        int length = sanitizedContent.length();
        int start = 0;
        int end = length - 1;

        //Palindrome checker algorithm with N/2 --> O(N) time complexity
        while (end > start) {
            char startChar = sanitizedContent.charAt(start++);
            char endChar = sanitizedContent.charAt(end--);

            //Break when there is a mismatch
            if (startChar != endChar){
                return false;
            }
        }
        return true;
    }

}
