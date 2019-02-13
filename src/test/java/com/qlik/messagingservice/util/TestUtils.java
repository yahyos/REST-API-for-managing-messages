package com.qlik.messagingservice.util;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUtils {

    public static long createMessage(MockMvc mvc, String content) throws Exception {
        MvcResult postResult = mvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\" : " + "\"" + content + "\"" + "}"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        long id = ((Integer)(new JSONObject(responseBody).get("id"))).longValue();
        return id;
    }

    public static void deleteMessage(MockMvc mvc, long id) throws Exception {
        mvc.perform(delete("/api/messages/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
