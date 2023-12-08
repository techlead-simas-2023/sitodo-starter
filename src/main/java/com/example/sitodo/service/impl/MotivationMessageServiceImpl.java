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

        StringBuilder output = new StringBuilder();

        // The following conditional blocks are intentionally bad to illustrate code example with high cognitive complexity.
        if (total == 0) {
            output.append(emptyListMessage);
        } else if (total < manyItemsThreshold) {
            output.append(fewItemsMessage);

            if (finished == total) {
                output.append(" ").append(allFinishedMessage);
            } else if (finished == 0) {
                output.append(" ").append(noFinishedMessage);
            } else if (finished < total) {
                if (finished >= total / 2) {
                    output.append(" ").append(halfFinishedMessage);
                } else {
                    output.append(someFinishedMessage);
                }
            } else {
                output.append(someFinishedMessage);
            }
        } else {
            output.append(manyItemsMessage);

            if (finished == total) {
                output.append(allFinishedMessage);
            } else if (finished == 0) {
                output.append(" ").append(noFinishedMessage);
            } else if (finished < total) {
                if (finished >= total / 2) {
                    output.append(" ").append(halfFinishedMessage);
                } else {
                    output.append(someFinishedMessage);
                }
            } else {
                output.append(someFinishedMessage);
            }
        }

        log.debug("Resulting output: {}", output.toString());

        return output.toString();
    }
}
