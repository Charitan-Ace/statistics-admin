package com.charitan.statistics.statistics.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * Retrieve total donation value and project of a user by admin.
     * userId and role for viewing statistics of a user
     * category, isoCode, continent, status for viewing statistics filtering by admin
     */
    @GetMapping("/totals")
    public ResponseEntity<Object> getTotal(
            @RequestParam(value = "userId", defaultValue = "", required = false) UUID userId,
            @RequestParam(value = "role", defaultValue = "", required = false) String role,
            @RequestParam(value = "category", defaultValue = "", required = false) String category,
            @RequestParam(value = "isoCode", defaultValue = "", required = false) String isoCode,
            @RequestParam(value = "continent", defaultValue = "", required = false) String continent,
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            @RequestParam(value = "time", defaultValue = "all", required = false) String time) {
        try {
            if (userId != null) {
                if (role.equalsIgnoreCase("DONOR")) {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(statisticsInternalAPI.getStatisticsForDonor(userId));
                }
                return ResponseEntity.status(HttpStatus.OK).body(statisticsInternalAPI.getStatisticsForCharity(userId));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(statisticsInternalAPI.getStatisticsAll(category, isoCode, continent, status, time));
            }
        } catch (ResponseStatusException e) {
            // If the exception is a ResponseStatusException, return the status and message
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @GetMapping("/totals/me")
    public ResponseEntity<Object> getMyTotal() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(statisticsInternalAPI.getMyStatistics());
        } catch (ResponseStatusException e) {
            // If the exception is a ResponseStatusException, return the status and message
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @GetMapping("/newUser")
    public ResponseEntity<Object> getNewUser(@RequestParam(value = "time", defaultValue = "week", required = false) String time) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(statisticsInternalAPI.getNewUsers(time));
        } catch (ResponseStatusException e) {
            // If the exception is a ResponseStatusException, return the status and message
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    /**
     * Retrieve total donation value and project of an user by themselves.
     */

    /**
     * Admins can retrieve all donations by category + continent.
     */
    // @GetMapping("/donations")
    // public ResponseEntity<Object> getDonationValue(@RequestParam(value =
    // "category", defaultValue = "", required = false) String category,
    //// @RequestParam(value = "country", defaultValue = "", required = false)
    // String country,
    // @RequestParam(value = "continent", defaultValue = "", required = false)
    // String continent) {
    // try {
    //
    // } catch (ResponseStatusException e) {
    // // If the exception is a ResponseStatusException, return the status and
    // message
    // return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    // } catch (Exception e) {
    // // Handle other exceptions
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error
    // occurred.");
    // }
    // }

    // @GetMapping("/project")
    // public ResponseEntity<Object> getProjectCount(@RequestParam(value =
    // "category", defaultValue = "", required = false) String category,
    //// @RequestParam(value = "country", defaultValue = "", required = false)
    // String country,
    // @RequestParam(value = "continent", defaultValue = "", required = false)
    // String continent) {
    // try {
    //
    // } catch (ResponseStatusException e) {
    // // If the exception is a ResponseStatusException, return the status and
    // message
    // return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    // } catch (Exception e) {
    // // Handle other exceptions
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error
    // occurred.");
    // }
    // }

    /**
     * Admins can retrieve all projects by category.
     * Users can retrieve only their own projects by category.
     */
    // @GetMapping("/projects/category")
    // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    // public Map<Object, Object> getProjectsByCategory(Authentication
    // authentication) {
    // if (authentication.getAuthorities().stream().anyMatch(a ->
    // a.getAuthority().equals("ROLE_ADMIN"))) {
    // return statisticsInternalAPI.getTotalProjectsByCategory();
    // } else {
    // String username = authentication.getName();
    // return statisticsInternalAPI.getProjectsByCategoryForUser(username);
    // }
    // }

    /**
     * Admins can retrieve statistics for any user.
     * Users can retrieve only their own statistics.
     */
    // @GetMapping("/user/{username}/stats")
    // @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    // public Map<String, Object> getUserStats(@PathVariable String username,
    // Authentication authentication) {
    // boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a ->
    // a.getAuthority().equals("ROLE_ADMIN"));
    // if (!isAdmin && !username.equals(authentication.getName())) {
    // throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Users can only
    // retrieve their own statistics.");
    // }
    //
    // double userTotalDonations = statisticsInternalAPI.getTotalDonationsForUser();
    // long userTotalProjects =
    // statisticsInternalAPI.getTotalProjectsForUser(username);
    //
    // // Optionally, include other user-specific stats
    // // e.g., donations by category, projects by category, etc.
    //
    // return Map.of(
    // "userTotalDonations", userTotalDonations,
    // "userTotalProjects", userTotalProjects
    // );
    // }

    /**
     * Retrieve filtered statistics.
     * Admins can filter across all users.
     * Users can filter only their own statistics.
     */
    // @GetMapping("/filter")
    // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    // public Map<String, Object> getFilteredStatistics(
    // @RequestParam Optional<String> continent,
    // @RequestParam Optional<String> country,
    // @RequestParam Optional<String> category,
    // @RequestParam Optional<String> startDate,
    // @RequestParam Optional<String> endDate,
    // Authentication authentication
    // ) {
    // boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a ->
    // a.getAuthority().equals("ROLE_ADMIN"));
    // Optional<String> username = isAdmin ? Optional.empty() :
    // Optional.of(authentication.getName());
    //
    // return statisticsInternalAPI.getFilteredStatistics(continent, country,
    // category, startDate, endDate, username);
    // }
}
