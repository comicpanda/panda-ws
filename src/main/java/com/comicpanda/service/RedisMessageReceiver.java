package com.comicpanda.service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Yoon
 */
public class RedisMessageReceiver {

    @Autowired
    private RedisMessagingDelegate redisMessagingDelegate;

    public void pong(String message) {
        redisMessagingDelegate.execute(message);
    }
}
