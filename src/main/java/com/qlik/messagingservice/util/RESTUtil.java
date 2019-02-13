package com.qlik.messagingservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qlik.messagingservice.exception.InvalidJsonException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RESTUtil {

    public static Map<String, Object> extractJsonMapFromJsonString(String jsonString){
        HashMap<String,Object> mapRequestBody;
        try{
            mapRequestBody = new ObjectMapper().readValue(jsonString, HashMap.class);
        } catch(IOException ex){
            throw new InvalidJsonException();
        }

        return mapRequestBody;
    }
}
