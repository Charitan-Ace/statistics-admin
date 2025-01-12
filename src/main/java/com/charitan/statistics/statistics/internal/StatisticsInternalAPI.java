package com.charitan.statistics.statistics.internal;

import java.util.UUID;

public interface StatisticsInternalAPI {
    public InternalStatisticsDto getStatisticsForDonor(UUID donorId);
    public InternalStatisticsDto getStatisticsForCharity(UUID charityId);
    public InternalAllStatisticsDto getStatisticsAll();
    public InternalStatisticsDto getMyStatistics();
}
