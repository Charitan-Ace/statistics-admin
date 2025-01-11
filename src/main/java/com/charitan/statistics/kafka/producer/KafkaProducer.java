package com.charitan.statistics.kafka.producer;

import ace.charitan.common.dto.donation.GetDonationStatisticsResponseDto;
import ace.charitan.common.dto.donation.GetDonorDonationStatisticsRequestDto;
import ace.charitan.common.dto.project.GetProjectByCharitanIdDto;
import ace.charitan.common.dto.statistics.project.GetProjectsCountResponse;
import ace.charitan.common.dto.statistics.project.GetTotalValueResponse;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class KafkaProducer implements KafkaProducerExterrnalAPI {
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Object send(StatisticsProducerTopic topic, Object data) throws InterruptedException, ExecutionException {
        if (!replyingKafkaTemplate.waitForAssignment(Duration.ofSeconds(10))) {
            throw new RuntimeException("Template container hasn't been initialized");
        }

        ProducerRecord<String, Object> record = new ProducerRecord<>(topic.getTopic(), data);

        RequestReplyFuture<String, Object, Object> request = replyingKafkaTemplate.sendAndReceive(record, Duration.ofSeconds(10));

        // Wait for the response (blocking)
        var result = request.get();
        logger.info("Request to {} has been replied, value size {}", topic.getTopic(), result.serializedValueSize());

        return result.value();
    }

    public GetDonationStatisticsResponseDto sendGetDonorDonationRequest(GetDonorDonationStatisticsRequestDto request) throws ExecutionException, InterruptedException {
        return (GetDonationStatisticsResponseDto) send(StatisticsProducerTopic.DONOR_GET_DONATION, request);
    }

    public GetProjectByCharitanIdDto.GetProjectByCharitanIdResponseDto sendGetProjectByCharitanId(GetProjectByCharitanIdDto.GetProjectByCharitanIdRequestDto request) throws ExecutionException, InterruptedException {
        return (GetProjectByCharitanIdDto.GetProjectByCharitanIdResponseDto) send(StatisticsProducerTopic.CHARITY_GET_PROJECT, request);
    }

    //All
    public GetProjectsCountResponse sendProjectCountRequest() throws ExecutionException, InterruptedException {
        return (GetProjectsCountResponse) send(StatisticsProducerTopic.PROJECT_COUNT, "");
    }

    public GetTotalValueResponse sendTotalValueRequest() throws ExecutionException, InterruptedException {
        return (GetTotalValueResponse) send(StatisticsProducerTopic.DONATION_VALUE, "");
    }
}
