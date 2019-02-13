package com.qlik.messagingservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.qlik.messagingservice.model.Message;
import com.qlik.messagingservice.persistence.MessageRepository;
import com.qlik.messagingservice.service.MessageService;
import com.qlik.messagingservice.util.RESTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


@RestController
class MessageController {

    private final MessageRepository repository;

    @Autowired
    private MessageService messageService;

    MessageController(MessageRepository repository) {
        this.repository = repository;
    }


    //GET endpoint
    @GetMapping("/api/messages/{id}")
    Map<String, Object>  getMessage(@PathVariable Long id) {
        Map<String, Object> responseMap = new HashMap<>();
        Message m = messageService.getMessageById(id);
        responseMap.put("data", m);
        return responseMap;
    }

    //GET-LIST endpoint
    @GetMapping("/api/messages")
    Map<String, Object> getAllMessages(@RequestParam Optional<Boolean> isPalindrome) {
        Map<String, Object> responseMap = new HashMap<>();
        List<Message> messageList;
        if(isPalindrome.isPresent()){
            messageList = repository.findAllByPalindrome(isPalindrome.get());
        } else {
            messageList = repository.findAll();
        }
        responseMap.put("data", messageList);
        return responseMap;
    }

    //POST endpoint
    @RequestMapping(value = "/api/messages", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    Message createMessage(HttpEntity<String> httpEntity) {
        String jsonRequestBody = httpEntity.getBody();
        Map<String,Object> mapRequestBody = RESTUtil.extractJsonMapFromJsonString(jsonRequestBody);
        messageService.validateRequestBody(mapRequestBody);
        String content = mapRequestBody.get("content").toString();
        messageService.validateContent(content);
        Message createdMessage = new Message(content);
        messageService.saveNewMessage(createdMessage);
        return createdMessage;
    }

    //PUT endpoint
    @RequestMapping(value = "/api/messages/{id}", method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    Message updateMessage(HttpEntity<String> httpEntity, @PathVariable Long id) {
        Message oldMessage = messageService.getMessageById(id);
        String jsonRequestBody = httpEntity.getBody();
        Map<String,Object> mapRequestBody = RESTUtil.extractJsonMapFromJsonString(jsonRequestBody);
        messageService.validateRequestBody(mapRequestBody);
        String newContent = mapRequestBody.get("content").toString();
        if(oldMessage.getContent().equals(newContent)) {
            return oldMessage;
        }
        messageService.validateContent(newContent);
        return messageService.updateMessage(oldMessage, newContent);
    }



    //DELETE endpoint
    @DeleteMapping("/api/messages/{id}")
    ResponseEntity deleteMessage(@PathVariable Long id) {
        Message messageToDelete = messageService.getMessageById(id);
        messageService.deleteMessage(messageToDelete);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}