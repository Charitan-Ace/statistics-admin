package com.charitan.statistics.kafka.producer;

import java.util.concurrent.ExecutionException;

import ace.charitan.common.dto.auth.GetNewUserByTimeRequestDto;
import ace.charitan.common.dto.auth.GetNewUserByTimeResponseDto;
import ace.charitan.common.dto.donation.GetCharityDonationStatisticsRequestDto;
import ace.charitan.common.dto.donation.GetDonationStatisticsResponseDto;
import ace.charitan.common.dto.donation.GetDonorDonationStatisticsRequestDto;
import ace.charitan.common.dto.project.GetProjectByCharityIdDto.GetProjectByCharityIdRequestDto;
import ace.charitan.common.dto.project.GetProjectByCharityIdDto.GetProjectByCharityIdResponseDto;
import ace.charitan.common.dto.project.GetProjectsByFilterRequestDto;
import ace.charitan.common.dto.project.GetProjectsByFilterResponseDto;
import ace.charitan.common.dto.statistics.project.GetProjectsCountResponse;
import ace.charitan.common.dto.statistics.project.GetTotalValueResponse;

public interface KafkaProducerExterrnalAPI {

    // Charity
    public GetProjectByCharityIdResponseDto sendGetProjectByCharitanId(GetProjectByCharityIdRequestDto request)
            throws ExecutionException, InterruptedException;

    public GetDonationStatisticsResponseDto sendGetCharityDonationStatistics(GetCharityDonationStatisticsRequestDto dto)
            throws ExecutionException, InterruptedException;

    // public GetCharityProjectCountResponseDto
    // sendCharityProjectCountRequest(GetCharityProjectCountRequestDto request)
    // throws ExecutionException, InterruptedException;
    // public GetCharityTotalValueResponseDto
    // sendCharityTotalValueRequest(GetCharityTotalValueRequestDto request) throws
    // ExecutionException, InterruptedException;
    // Donor
    public GetDonationStatisticsResponseDto sendGetDonorDonationRequest(GetDonorDonationStatisticsRequestDto request)
            throws ExecutionException, InterruptedException;

    // All
    public GetProjectsCountResponse sendProjectCountRequest() throws ExecutionException, InterruptedException;

    public GetTotalValueResponse sendTotalValueRequest() throws ExecutionException, InterruptedException;


    public GetProjectsByFilterResponseDto sendGetProjectIdByFilter(GetProjectsByFilterRequestDto request)
            throws ExecutionException, InterruptedException;

    public GetNewUserByTimeResponseDto sendGetNewUserByTime(GetNewUserByTimeRequestDto request)
            throws ExecutionException, InterruptedException;
}
