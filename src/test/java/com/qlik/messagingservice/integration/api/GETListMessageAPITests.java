package com.qlik.messagingservice.integration.api;

import com.qlik.messagingservice.util.TestUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class GETListMessageAPITests extends MessageBaseAPITests {


    @Test
    public void givenMessagesExists_whenMessagesAreRetrieved_then200IsReceived() throws Exception {

        long id = TestUtils.createMessage(mvc, "madam");

        MvcResult getResult = mvc.perform(get("/api/messages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();
        JSONArray messagesArray = (JSONArray) new JSONObject(responseBody).get("data");
        assertEquals(messagesArray.length(), 5);
        JSONObject newCreatedMessage = (JSONObject) messagesArray.get(4);
        assertEquals(newCreatedMessage.get("content"), "madam");
        assertTrue((boolean) newCreatedMessage.get("palindrome"));

        TestUtils.deleteMessage(mvc, id);
    }

    //Filtering with isPalindrome
    @Test
    public void givenMessagesExists_whenFilteredMessagesAreRetrieved_then200IsReceived() throws Exception {

        long id = TestUtils.createMessage(mvc, "notPalindrome");

        //Filter for palindrome messages
        MvcResult getResult = mvc.perform(get("/api/messages?isPalindrome=true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();
        JSONArray messagesArray = (JSONArray) new JSONObject(responseBody).get("data");
        assertEquals(messagesArray.length(), 1);
        JSONObject existingPalindromeMessage = (JSONObject) messagesArray.get(0);
        assertEquals(existingPalindromeMessage.get("content"), "level");
        assertTrue((boolean) existingPalindromeMessage.get("palindrome"));

        //Filter for non palindrome messages
        getResult = mvc.perform(get("/api/messages?isPalindrome=false")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        responseBody = getResult.getResponse().getContentAsString();
        messagesArray = (JSONArray) new JSONObject(responseBody).get("data");
        assertEquals(messagesArray.length(), 4);
        JSONObject existingNonPalindromeMessage = (JSONObject) messagesArray.get(0);
        assertFalse((boolean) existingNonPalindromeMessage.get("palindrome"));
        JSONObject newCreatedMessage = (JSONObject) messagesArray.get(3);
        assertEquals(newCreatedMessage.get("content"), "notPalindrome");
        assertFalse((boolean) newCreatedMessage.get("palindrome"));

        TestUtils.deleteMessage(mvc, id);
    }

}
