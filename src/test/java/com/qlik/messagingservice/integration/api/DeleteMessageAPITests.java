package com.qlik.messagingservice.integration.api;

import com.qlik.messagingservice.util.TestUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteMessageAPITests extends MessageBaseAPITests {

    @Test
    public void givenMessageExists_whenMessageIsDeleted_then204IsReceived() throws Exception {

        long id = TestUtils.createMessage(mvc, "aabaa");

        MvcResult getResult = mvc.perform(get("/api/messages/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        MvcResult deleteResult = mvc.perform(delete("/api/messages/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void givenMessageDoesNotExists_whenMessageIsDeleted_then404IsReceived() throws Exception {

        long WRONG_ID = 999999999;
        MvcResult getResult = mvc.perform(delete("/api/messages/" + WRONG_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();
        JSONObject errorObject = (JSONObject) new JSONObject(responseBody).get("error");
        assertEquals(errorObject.get("message"), "Could not find message with id: " + WRONG_ID);

    }
}
