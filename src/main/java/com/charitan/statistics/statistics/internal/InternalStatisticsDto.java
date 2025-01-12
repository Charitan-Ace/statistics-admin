package com.charitan.statistics.statistics.internal;

import java.util.Map;
import java.util.UUID;

import ace.charitan.common.dto.donation.GetDonationStatisticsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InternalStatisticsDto {
    private final UUID userId;
    // private final long totalProject;
    // private final double totalValue;

    private Map<String, Double> donationStatistics;
}
