package com.charitan.statistics.statistics.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsInternalAPI statisticsInternalAPI;

    /**
     * Retrieve total donations and total projects.
     * If 'username' is specified, admin can retrieve totals for that user.
     * Users can retrieve their own totals.
     */
    @GetMapping("/totals")
    public Map<String, Object> getTotals(@RequestParam(required = false) UUID userId, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        String authenticatedUsername = authentication.getName();

        if (userId != null) {
            // Admin retrieving totals for specified user
            double userTotalDonations = statisticsInternalAPI.getTotalDonationsForUser(userId);
            long userTotalProjects = statisticsInternalAPI.getTotalProjectsForUser(userId);
            return Map.of(
                    "userTotalDonations", userTotalDonations,
                    "userTotalProjects", userTotalProjects
            );
        } else {
            // Retrieve global totals
            double globalTotalDonations = statisticsInternalAPI.getTotalDonations();
            long globalTotalProjects = statisticsInternalAPI.getTotalProjects();

            return Map.of(
                    "totalDonations", globalTotalDonations,
                    "totalProjects", globalTotalProjects
            );
        }
    }

    /**
     * Admins can retrieve all donations by category.
     * Users can retrieve only their own donations by category.
     */
    @GetMapping("/donations/category")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Map<Object, Object> getDonationsByCategory(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return statisticsInternalAPI.getTotalDonationsByCategory();
        } else {
            String username = authentication.getName();
            return statisticsInternalAPI.getDonationsByCategoryForUser(username);
        }
    }

    /**
     * Admins can retrieve all projects by category.
     * Users can retrieve only their own projects by category.
     */
    @GetMapping("/projects/category")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Map<Object, Object> getProjectsByCategory(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return statisticsInternalAPI.getTotalProjectsByCategory();
        } else {
            String username = authentication.getName();
            return statisticsInternalAPI.getProjectsByCategoryForUser(username);
        }
    }

    /**
     * Admins can retrieve statistics for any user.
     * Users can retrieve only their own statistics.
     */
//    @GetMapping("/user/{username}/stats")
//    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
//    public Map<String, Object> getUserStats(@PathVariable String username, Authentication authentication) {
//        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//        if (!isAdmin && !username.equals(authentication.getName())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Users can only retrieve their own statistics.");
//        }
//
//        double userTotalDonations = statisticsInternalAPI.getTotalDonationsForUser();
//        long userTotalProjects = statisticsInternalAPI.getTotalProjectsForUser(username);
//
//        // Optionally, include other user-specific stats
//        // e.g., donations by category, projects by category, etc.
//
//        return Map.of(
//                "userTotalDonations", userTotalDonations,
//                "userTotalProjects", userTotalProjects
//        );
//    }

    /**
     * Retrieve filtered statistics.
     * Admins can filter across all users.
     * Users can filter only their own statistics.
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Map<String, Object> getFilteredStatistics(
            @RequestParam Optional<String> continent,
            @RequestParam Optional<String> country,
            @RequestParam Optional<String> category,
            @RequestParam Optional<String> startDate,
            @RequestParam Optional<String> endDate,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Optional<String> username = isAdmin ? Optional.empty() : Optional.of(authentication.getName());

        return statisticsInternalAPI.getFilteredStatistics(continent, country, category, startDate, endDate, username);
    }
}
