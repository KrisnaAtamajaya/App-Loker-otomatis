package com.a213310035_krisnaatmajaya.myapplication;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

class MqttConfig {
    private static final String BROKER_URL = "tcp://broker.mqtt-dashboard.com:1883";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";
    private static final String CLIENT_ID = "smart-locker-client";
    private static final String TOPIC_PREFIX = "smart-locker/";

    public static String getBrokerUrl() {
        return BROKER_URL;
    }

    public static MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);
        return options;
    }

    public static MemoryPersistence getMemoryPersistence() {
        return new MemoryPersistence();
    }

    public static String getClientId() {
        return CLIENT_ID;
    }

    public static String getTopicForLocker(String lockerId) {
        return TOPIC_PREFIX + lockerId;
    }
}
