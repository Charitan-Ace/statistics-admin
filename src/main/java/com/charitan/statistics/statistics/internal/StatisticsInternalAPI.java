package com.charitan.statistics.statistics.internal;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface StatisticsInternalAPI {
    public InternalStatisticsDto getStatisticsForDonor(UUID donorId);
    public InternalStatisticsDto getStatisticsForCharity(UUID charityId);
    public InternalAllStatisticsDto getStatisticsAll();
    public InternalStatisticsDto getMyStatistics();
}
