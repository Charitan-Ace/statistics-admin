package com.charitan.statistics.statistics.internal;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface StatisticsInternalAPI {
    public InternalStatisticsDto getStatisticsForDonor(UUID donorId);
    public InternalStatisticsDto getStatisticsForCharity(UUID charityId);
    public Map<String, Double> getStatisticsAll(String category, String isoCode, String continent, String status, String time);
    public InternalStatisticsDto getMyStatistics();
    public List<UUID> getNewUsers(String time);
    public Map<String, Double> getTopDonors();
    public Map<String, Double> getTopDonorsOfCharity();
}
