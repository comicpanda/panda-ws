package com.comicpanda.enumeration;

/**
 * @author Yoon
 */
public enum RedisMessageType {
    READING_COUNT("READING_COUNT"),
    NONE("none");

    String desc;

    RedisMessageType(String desc) {
        this.desc = desc;
    }

    public static RedisMessageType value(String desc) {
        for(RedisMessageType messageType : values()) {
            if(messageType.name().equals(desc)) {
                return messageType;
            }
        }
        return NONE;
    }
}