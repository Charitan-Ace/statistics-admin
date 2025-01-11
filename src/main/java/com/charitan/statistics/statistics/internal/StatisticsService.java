package com.charitan.statistics.statistics.internal;

import ace.charitan.common.dto.donation.GetDonationStatisticsResponseDto;
import ace.charitan.common.dto.donation.GetDonorDonationStatisticsRequestDto;
import ace.charitan.common.dto.project.GetProjectByCharitanIdDto;
import com.charitan.statistics.jwt.internal.CustomUserDetails;
import com.charitan.statistics.kafka.producer.KafkaProducerExterrnalAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class StatisticsService implements StatisticsInternalAPI{

    private final KafkaProducerExterrnalAPI statisticsProducer;
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @Override
    public InternalStatisticsDto getStatisticsForDonor(UUID donorId) {
        try {
            GetDonationStatisticsResponseDto response;
            try {
                response = statisticsProducer.sendGetDonorDonationRequest(new GetDonorDonationStatisticsRequestDto(donorId.toString()));
            } catch (RuntimeException e) { // Catch general runtime exceptions if relevant
                log.error("An error occurred while fetching donation statistics: {}", e.getMessage(), e);
                throw e;
            }
            GetDonorDonationStatisticsRequestDto request = new GetDonorDonationStatisticsRequestDto("d2bd087c-3a6a-4179-91c2-b8595ebc92d3");
            ProducerRecord<String, Object> record = new ProducerRecord<>("donor-donation-statistics", request);
            System.out.println(response.getDonorStatistics().toString());
            Map<String, Double> donorStatistics = response.getDonorStatistics();

            int count = donorStatistics.size();
            double totalValue = donorStatistics.values().stream().mapToDouble(Double::doubleValue).sum();

            return new InternalStatisticsDto(donorId, count, totalValue);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InternalStatisticsDto getStatisticsForCharity(UUID charityId) {
        try {
            GetProjectByCharitanIdDto.GetProjectByCharitanIdResponseDto response = statisticsProducer.sendGetProjectByCharitanId(new GetProjectByCharitanIdDto.GetProjectByCharitanIdRequestDto(charityId.toString(), Arrays.asList("PROJECT_DELETED", "PROJECT_COMPLETED")));
            System.out.println(Arrays.toString(response.getProjectDtoList().toArray()));
            return new InternalStatisticsDto(charityId, 0, 0);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InternalAllStatisticsDto getStatisticsAll() {
        try {
            long projectsCount = statisticsProducer.sendProjectCountRequest().totalProjects();
            double totalValue = statisticsProducer.sendTotalValueRequest().totalValue();
            return new InternalAllStatisticsDto(projectsCount, totalValue);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InternalStatisticsDto getMyStatistics() {

        getCurrentDonorId();
        return null;
//        if (userId != null) {
//            if (role.equalsIgnoreCase("DONOR")) {
//                return ResponseEntity.status(HttpStatus.OK).body(statisticsInternalAPI.getStatisticsForDonor(userId));
//            }
//            return ResponseEntity.status(HttpStatus.OK).body(statisticsInternalAPI.getStatisticsForCharity(userId));
//        } else {
//            return ResponseEntity.status(HttpStatus.OK).body(statisticsInternalAPI.getStatisticsAll());
//        }
    }

    private UUID getCurrentDonorId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof CustomUserDetails) {
                // Assuming CustomUserDetails holds the User ID
                System.out.println(Arrays.toString(((CustomUserDetails) principal).getAuthorities().toArray()));
                return ((CustomUserDetails) principal).getUserId();
            }
        }

        throw new RuntimeException("Current user id is not found");
    }
}
