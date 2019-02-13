package com.qlik.messagingservice.integration.api;

import com.qlik.messagingservice.util.TestUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateMessageAPITests extends MessageBaseAPITests{

    @Test
    public void givenRequestBodyWithValidContent_whenMessageIsUpdated_then200IsReceived() throws Exception {
        long id = TestUtils.createMessage(mvc, "original content");
        MvcResult putResult = mvc.perform(put("/api/messages/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\" : \"updated content\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = putResult.getResponse().getContentAsString();
        JSONObject errorObject = new JSONObject(responseBody);
        assertEquals(errorObject.get("content"), "updated content");
        TestUtils.deleteMessage(mvc, id);
    }

    @Test
    public void givenMessageDoesNotExists_whenMessageIsUpdated_then404IsReceived() throws Exception {
        long WRONG_ID = 999999999;
        MvcResult putResult = mvc.perform(put("/api/messages/" + WRONG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\" : \"updated content\"}"))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = putResult.getResponse().getContentAsString();
        JSONObject errorObject = (JSONObject) new JSONObject(responseBody).get("error");
        assertEquals(errorObject.get("message"), "Could not find message with id: " + WRONG_ID);
    }

    @Test
    public void givenRequestBodyWithInvalidContentCharacters_whenMessageIsUpdated_then404IsReceived() throws Exception {

        long id = TestUtils.createMessage(mvc, "original content");

        MvcResult postResult = mvc.perform(put("/api/messages/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\" : \"qwerty!&^123?\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        JSONObject errorObject = (JSONObject) new JSONObject(responseBody).get("error");
        assertEquals(errorObject.get("message"), "Special characters are not allowed!");
        assertEquals(errorObject.get("code"), "400 Bad Request");

        TestUtils.deleteMessage(mvc, id);

    }

    @Test
    public void givenRequestBodyWithTooLongContent_whenMessageIsUpdated_then404IsReceived() throws Exception {
        long id = TestUtils.createMessage(mvc, "original content");

        String tooLongMessage = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        MvcResult postResult = mvc.perform(put("/api/messages/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\" : " + "\"" + tooLongMessage + "\"" + "}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        JSONObject errorObject = (JSONObject) new JSONObject(responseBody).get("error");
        assertEquals(errorObject.get("message"), "Content length should be between 1 and 500 characters.");
        assertEquals(errorObject.get("code"), "400 Bad Request");

        TestUtils.deleteMessage(mvc, id);

    }

}
