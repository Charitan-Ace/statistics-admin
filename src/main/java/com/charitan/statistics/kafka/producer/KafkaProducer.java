package com.charitan.statistics.kafka.producer;

import static org.springframework.kafka.support.KafkaHeaders.REPLY_TOPIC;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

import ace.charitan.common.dto.auth.GetNewUserByTimeRequestDto;
import ace.charitan.common.dto.auth.GetNewUserByTimeResponseDto;
import ace.charitan.common.dto.donation.*;
import ace.charitan.common.dto.project.GetProjectsByFilterRequestDto;
import ace.charitan.common.dto.project.GetProjectsByFilterResponseDto;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import ace.charitan.common.dto.project.GetProjectByCharityIdDto.GetProjectByCharityIdRequestDto;
import ace.charitan.common.dto.project.GetProjectByCharityIdDto.GetProjectByCharityIdResponseDto;
import ace.charitan.common.dto.statistics.project.GetProjectsCountResponse;
import ace.charitan.common.dto.statistics.project.GetTotalValueResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class KafkaProducer implements KafkaProducerExterrnalAPI {
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Object send(StatisticsProducerTopic topic, Object data) throws InterruptedException, ExecutionException {
        if (!replyingKafkaTemplate.waitForAssignment(Duration.ofSeconds(10))) {
            throw new RuntimeException("Template container hasn't been initialized");
        }

        ProducerRecord<String, Object> record = new ProducerRecord<>(topic.getTopic(), data);
        record.headers().add(REPLY_TOPIC, REPLY_TOPIC.getBytes());

        replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(30));
        RequestReplyFuture<String, Object, Object> request = replyingKafkaTemplate.sendAndReceive(record);

        // Wait for the response (blocking)
        var result = request.get();
        logger.info("Request to {} has been replied, value size {}", topic.getTopic(), result.serializedValueSize());

        return result.value();
    }

    // Get statistic for donor
    public GetDonationStatisticsResponseDto sendGetDonorDonationRequest(GetDonorDonationStatisticsRequestDto request)
            throws ExecutionException, InterruptedException {
        return (GetDonationStatisticsResponseDto) send(StatisticsProducerTopic.DONOR_GET_DONATION, request);
    }

    // Get project by charity Id
    public GetProjectByCharityIdResponseDto sendGetProjectByCharitanId(GetProjectByCharityIdRequestDto request)
            throws ExecutionException, InterruptedException {
        return (GetProjectByCharityIdResponseDto) send(StatisticsProducerTopic.CHARITY_GET_PROJECT, request);
    }

    //Get charity donation
    public GetDonationStatisticsResponseDto sendGetCharityDonationStatistics(GetCharityDonationStatisticsRequestDto dto)
            throws ExecutionException, InterruptedException {
        return (GetDonationStatisticsResponseDto) send(StatisticsProducerTopic.CHARITY_DONATION_STATISTICS, dto);
    }

    // Get project with filter
    public GetProjectsByFilterResponseDto sendGetProjectIdByFilter(GetProjectsByFilterRequestDto request)
            throws ExecutionException, InterruptedException {
        return (GetProjectsByFilterResponseDto) send(StatisticsProducerTopic.CHARITY_GET_PROJECT_BY_FILTER, request);
    }

    // Get new user by time
    public GetNewUserByTimeResponseDto sendGetNewUserByTime(GetNewUserByTimeRequestDto request)
            throws ExecutionException, InterruptedException {
        return (GetNewUserByTimeResponseDto) send(StatisticsProducerTopic.GET_NEW_USER, request);
    }

    // Get top donors
    public GetDonorsOfTheMonthResponseDto sendGetTopDonorOfTheMonth()
            throws ExecutionException, InterruptedException {
        return (GetDonorsOfTheMonthResponseDto) send(StatisticsProducerTopic.TOP_DONOR_MONTH, "");
    }

    // Get top donors of charities
    public GetDonorsOfTheMonthResponseDto sendGetTopDonorOfTheMonth(GetCharityDonorsOfTheMonthRequestDto dto)
            throws ExecutionException, InterruptedException {
        return (GetDonorsOfTheMonthResponseDto) send(StatisticsProducerTopic.TOP_DONOR_MONTH_CHARITY, "");
    }
}
