package com.charitan.statistics.statistics.internal;

import com.charitan.statistics.kafka.producer.StatisticsProducerExterrnalAPI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class StatisticsService implements StatisticsInternalAPI{

    StatisticsProducerExterrnalAPI statisticsProducer;

    @Override
    public double getTotalDonationsForUser(UUID userId) {
        return 0;
    }

    @Override
    public long getTotalProjectsForUser(UUID userId) {
        return 0;
    }

    @Override
    public void recalculateStatistics() {

    }

    @Override
    public void updateDonationStats(String donor, double amount, String category, String continent, String country, String timestamp) {

    }

    @Override
    public void updateProjectStats(String category, String continent, String country, String startTime, String username) {

    }

    @Override
    public Map<Object, Object> getTotalDonationsByCategory() {
        return Map.of();
    }

    @Override
    public Map<Object, Object> getTotalProjectsByCategory() {
        return Map.of();
    }

    @Override
    public Map<Object, Object> getTotalDonationsByContinent() {
        return Map.of();
    }

    @Override
    public Map<Object, Object> getTotalProjectsByContinent() {
        return Map.of();
    }

    @Override
    public Map<Object, Object> getTotalDonationsByCountry() {
        return Map.of();
    }

    @Override
    public Map<Object, Object> getTotalProjectsByCountry() {
        return Map.of();
    }

    @Override
    public Map<Object, Object> getTotalDonationsByDate() {
        return Map.of();
    }

    @Override
    public Map<Object, Object> getTotalProjectsByDate() {
        return Map.of();
    }

    @Override
    public Map<Object, Object> getDonationsByCategoryForUser(String username) {
        return Map.of();
    }

    @Override
    public Map<Object, Object> getProjectsByCategoryForUser(String username) {
        return Map.of();
    }

    @Override
    public double getTotalDonations() {
        return 0;
    }

    @Override
    public long getTotalProjects() {
        return 0;
    }

    @Override
    public Map<String, Object> getFilteredStatistics(Optional<String> continent, Optional<String> country, Optional<String> category, Optional<String> startDate, Optional<String> endDate, Optional<String> username) {
        return Map.of();
    }
}
