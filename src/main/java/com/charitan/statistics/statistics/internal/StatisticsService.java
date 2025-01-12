package com.charitan.statistics.statistics.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.charitan.statistics.jwt.internal.CustomUserDetails;
import com.charitan.statistics.kafka.producer.KafkaProducerExterrnalAPI;

import ace.charitan.common.dto.donation.GetCharityDonationStatisticsRequestDto;
import ace.charitan.common.dto.donation.GetCharityDonationStatisticsWrapperDto;
import ace.charitan.common.dto.donation.GetDonationStatisticsResponseDto;
import ace.charitan.common.dto.donation.GetDonorDonationStatisticsRequestDto;
import ace.charitan.common.dto.project.ExternalProjectDto;
import ace.charitan.common.dto.project.GetProjectByCharityIdDto.GetProjectByCharityIdRequestDto;
import ace.charitan.common.dto.project.GetProjectByCharityIdDto.GetProjectByCharityIdResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class StatisticsService implements StatisticsInternalAPI {

    private final KafkaProducerExterrnalAPI statisticsProducer;
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @Override
    public InternalStatisticsDto getStatisticsForDonor(UUID donorId) {
        try {
            GetDonationStatisticsResponseDto response;
            try {
                response = statisticsProducer
                        .sendGetDonorDonationRequest(new GetDonorDonationStatisticsRequestDto(donorId.toString()));
            } catch (RuntimeException e) { // Catch general runtime exceptions if relevant
                log.error("An error occurred while fetching donation statistics: {}", e.getMessage(), e);
                throw e;
            }
            GetDonorDonationStatisticsRequestDto request = new GetDonorDonationStatisticsRequestDto(
                    "d2bd087c-3a6a-4179-91c2-b8595ebc92d3");
            ProducerRecord<String, Object> record = new ProducerRecord<>("donor-donation-statistics", request);
            System.out.println(response.getDonorStatistics().toString());
            // Map<String, Double> donorStatistics = response.getDonorStatistics();

            // int count = donorStatistics.size();
            // double totalValue = donorStatistics.values().stream().mapToDouble(Double::doubleValue).sum();

            // return new InternalStatisticsDto(donorId, count, totalValue);

            return new InternalStatisticsDto(donorId, response.getDonorStatistics());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InternalStatisticsDto getStatisticsForCharity(UUID charityId) {
        try {
            GetProjectByCharityIdResponseDto response = statisticsProducer
                    .sendGetProjectByCharitanId(new GetProjectByCharityIdRequestDto(charityId.toString(),
                            Arrays.asList("PROJECT_DELETED", "PROJECT_COMPLETED")));
            List<ExternalProjectDto> projectDtos = response.getProjectDtoList();

            List<String> projectIdList = projectDtos.stream().map(pDto -> pDto.getId()).toList();

            // Get list of donation values for each project
            GetDonationStatisticsResponseDto donationStatisticsResponseDto = statisticsProducer
                    .sendGetDonorDonationStatistics(
                            new GetCharityDonationStatisticsRequestDto(new GetCharityDonationStatisticsWrapperDto(
                                    projectIdList)));

            return new InternalStatisticsDto(charityId, donationStatisticsResponseDto.getDonorStatistics());
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

        if (getCurrentDonorRole().equalsIgnoreCase(("role_donor"))) {
            return getStatisticsForDonor(getCurrentUserId());
        }
        return getStatisticsForCharity(getCurrentUserId());
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof CustomUserDetails) {
                System.out.println(Arrays.toString(((CustomUserDetails) principal).getAuthorities().toArray()));
                return ((CustomUserDetails) principal).getUserId();
            }
        }

        throw new RuntimeException("Current user id is not found");
    }

    private String getCurrentDonorRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof CustomUserDetails) {
                // Get the first authority in the list
                return ((CustomUserDetails) principal).getAuthorities().stream()
                        .findFirst()
                        .map(GrantedAuthority::getAuthority)
                        .orElseThrow(() -> new RuntimeException("No authorities found for current user"));
            }
        }

        throw new RuntimeException("Current user id is not found");
    }
}
