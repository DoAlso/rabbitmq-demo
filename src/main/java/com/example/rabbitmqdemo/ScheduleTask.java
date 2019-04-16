package com.example.rabbitmqdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {

    @Autowired
    private Sender sender;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        sender.send();
    }
}
