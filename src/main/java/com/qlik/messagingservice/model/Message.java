package com.qlik.messagingservice.model;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Message {

    @Id @GeneratedValue
    private Long id;

    @NonNull
    @Column(columnDefinition="LONGTEXT")
    private String content;

    private LocalDateTime dateCreated;

    //last update date
    private LocalDateTime dateUpdated;

    private boolean isPalindrome;

    public final static int MAX_LENGTH = 500;


    //Declare default constructor for Hibernate
    public Message(){ }

    public Message(String content) {
        this.content = content;
    }

}