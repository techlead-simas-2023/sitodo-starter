package com.example.sitodo.service.impl;

import com.example.sitodo.service.MotivationMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MotivationMessageServiceImpl implements MotivationMessageService {

    @Value("${sitodo.motivation.empty}")
    private String emptyListMessage;

    @Value("${sitodo.motivation.noFinished}")
    private String noFinishedMessage;

    @Value("${sitodo.motivation.halfFinished}")
    private String halfFinishedMessage;

    @Value("${sitodo.motivation.someFinished}")
    private String someFinishedMessage;

    @Value("${sitodo.motivation.allFinished}")
    private String allFinishedMessage;

    @Value("${sitodo.motivation.fewItems}")
    private String fewItemsMessage;

    @Value("${sitodo.motivation.manyItems}")
    private String manyItemsMessage;

    @Value("${sitodo.motivation.fewItemsThreshold:5}")
    private int fewItemsThreshold;

    @Value("${sitodo.motivation.manyItemsThreshold:10}")
    private int manyItemsThreshold;

    @Override
    public String computeMotivationMessage(long total, long finished) {
        log.debug("Total Items: {}; Total Finished Items: {}", total, finished);

        String output = "";

        if (total == 0) {
            output += "";
        } else if (total < manyItemsThreshold) {
            output += fewItemsMessage;

            if (finished == total) {
                output += " " + allFinishedMessage;
            } else if (finished == 0) {
                output += " " + noFinishedMessage;
            } else if (finished < total && finished >= total / 2) {
                output += " " + halfFinishedMessage;
            } else {
                output += someFinishedMessage;
            }
        } else {
            output += manyItemsMessage;

            if (finished == total) {
                output += " " + allFinishedMessage;
            } else if (finished == 0) {
                output += " " + noFinishedMessage;
            } else if (finished < total && finished >= total / 2) {
                output += " " + halfFinishedMessage;
            } else {
                output += someFinishedMessage;
            }
        }

        log.debug("Resulting output: {}", output);

        return output;
    }
}
