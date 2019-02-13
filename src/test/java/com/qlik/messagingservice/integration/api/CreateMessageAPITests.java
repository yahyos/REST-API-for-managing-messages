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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateMessageAPITests extends MessageBaseAPITests {

    @Test
    public void givenValidRequestBody_whenMessageIsCreated_then200IsReceived() throws Exception {

        MvcResult getResult = mvc.perform(get("/api/messages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();
        JSONArray messagesArray = (JSONArray) new JSONObject(responseBody).get("data");
        int originalSize = messagesArray.length();

        String content = "testMessage";
        MvcResult postResult = mvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\" : " + "\"" + content + "\"" + "}"))
                .andExpect(status().isOk())
                .andReturn();

        responseBody = postResult.getResponse().getContentAsString();


        long messageCreatedId = ((Integer)(new JSONObject(responseBody).get("id"))).longValue();

        JSONObject messageObject = (JSONObject) new JSONObject(responseBody);
        assertEquals(messageObject.get("content"), "testMessage");
        assertFalse((boolean) messageObject.get("palindrome"));

        getResult = mvc.perform(get("/api/messages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        responseBody = getResult.getResponse().getContentAsString();
        messagesArray = (JSONArray) new JSONObject(responseBody).get("data");
        int newSize = messagesArray.length();

        assertTrue(newSize == (originalSize + 1));

        TestUtils.deleteMessage(mvc, messageCreatedId);
    }

    @Test
    public void givenRequestBodyMissingContent_whenMessageIsCreated_then404IsReceived() throws Exception {

        MvcResult postResult = mvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"key\" : \"value\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        JSONObject errorObject = (JSONObject) new JSONObject(responseBody).get("error");
        assertEquals(errorObject.get("message"), "Request body should be a json containing only one key: 'content'");
        assertEquals(errorObject.get("code"), "400 Bad Request");
    }

    @Test
    public void givenRequestBodyWithInvalidContentCharacters_whenMessageIsCreated_then404IsReceived() throws Exception {

        MvcResult postResult = mvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\" : \"qwerty!&^123?\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        JSONObject errorObject = (JSONObject) new JSONObject(responseBody).get("error");
        assertEquals(errorObject.get("message"), "Special characters are not allowed!");
        assertEquals(errorObject.get("code"), "400 Bad Request");
    }

    @Test
    public void givenRequestBodyWithTooLongContent_whenMessageIsCreated_then404IsReceived() throws Exception {

        String tooLongMessage = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        MvcResult postResult = mvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\" : " + "\"" + tooLongMessage + "\"" + "}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        JSONObject errorObject = (JSONObject) new JSONObject(responseBody).get("error");
        assertEquals(errorObject.get("message"), "Content length should be between 1 and 500 characters.");
        assertEquals(errorObject.get("code"), "400 Bad Request");
    }
}
