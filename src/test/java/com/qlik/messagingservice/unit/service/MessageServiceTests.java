package com.qlik.messagingservice.unit.service;

import com.qlik.messagingservice.exception.InvalidContentCharactersException;
import com.qlik.messagingservice.exception.InvalidContentLengthException;
import com.qlik.messagingservice.exception.MissingContentException;
import com.qlik.messagingservice.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class MessageServiceTests {

    @Autowired
    private MessageService messageService;

    @Test
    public void testValidateValidContent() {
        String validContent = "level";
        assertTrue(messageService.validateContent(validContent));
    }

    @Test(expected = MissingContentException.class)
    public void testValidateMissingContent() {

        //Exception is expected
        messageService.validateContent(null);
    }

    @Test(expected = MissingContentException.class)
    public void testValidateEmptyContent() {
        String emptyContent = "          ";
        messageService.validateContent(emptyContent);
    }

    @Test(expected = InvalidContentCharactersException.class)
    public void testValidateContentWithSpecialChars() {
        String contentWithSpecialChars = "!;hjhef44 5454 ?de@:f)";

        //Exception is expected
        messageService.validateContent(contentWithSpecialChars);
    }


    @Test(expected = InvalidContentLengthException.class)
    public void testValidateTooLongContent() {
        String tooLongContent = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        //Exception is expected
        messageService.validateContent(tooLongContent);
    }

    @Test
    public void testIsPalindrome() {
        String palindromeMessage = "level";
        assertTrue(messageService.isPalindrome(palindromeMessage));

        String palindromeMessageWithSpaces = " l e v el  ";
        assertTrue(messageService.isPalindrome(palindromeMessageWithSpaces));

        String upperCasePalindromeMessage = "lEvEL";
        assertTrue(messageService.isPalindrome(upperCasePalindromeMessage));
    }

    @Test
    public void testIsNotPalindrome() {
        String nonPalindromeMessage = "testMessage";
        assertFalse(messageService.isPalindrome(nonPalindromeMessage));
    }

}
