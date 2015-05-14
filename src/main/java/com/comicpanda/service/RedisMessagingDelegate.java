package com.comicpanda.service;

import com.comicpanda.enumeration.RedisMessageType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yoon
 */
public class RedisMessagingDelegate {
    private static final Gson gson = new GsonBuilder().create();

    @Autowired(required = false)
    private SimpMessagingTemplate template;

    public void execute(String message) {
        Map<String, String> messageMap = gson.fromJson(message, HashMap.class);
        RedisMessageType messageType = RedisMessageType.valueOf(messageMap.get("messageType"));

        switch (messageType) {
            case READING_COUNT:
                template.convertAndSend("/popular/reading-count", messageMap.get("count"));
                break;
            default:
                break;
        }
    }
}
