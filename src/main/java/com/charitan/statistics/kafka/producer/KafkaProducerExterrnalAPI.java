package com.charitan.statistics.kafka.producer;

import ace.charitan.common.dto.donation.GetDonationStatisticsResponseDto;
import ace.charitan.common.dto.donation.GetDonorDonationStatisticsRequestDto;
import ace.charitan.common.dto.statistics.project.GetProjectsCountResponse;
import ace.charitan.common.dto.statistics.project.GetTotalValueResponse;
import ace.charitan.common.dto.project.GetProjectByCharityIdDto.GetProjectByCharityIdRequestDto;
import ace.charitan.common.dto.project.GetProjectByCharityIdDto.GetProjectByCharityIdResponseDto;

import java.util.concurrent.ExecutionException;

public interface KafkaProducerExterrnalAPI {

    //Charity
    public GetProjectByCharityIdResponseDto sendGetProjectByCharitanId(GetProjectByCharityIdRequestDto request) throws ExecutionException, InterruptedException;
//    public GetCharityProjectCountResponseDto sendCharityProjectCountRequest(GetCharityProjectCountRequestDto request) throws ExecutionException, InterruptedException;
//    public GetCharityTotalValueResponseDto sendCharityTotalValueRequest(GetCharityTotalValueRequestDto request) throws ExecutionException, InterruptedException;
    //Donor
    public GetDonationStatisticsResponseDto sendGetDonorDonationRequest(GetDonorDonationStatisticsRequestDto request) throws ExecutionException, InterruptedException;
    //All
    public GetProjectsCountResponse sendProjectCountRequest() throws ExecutionException, InterruptedException;
    public GetTotalValueResponse sendTotalValueRequest() throws ExecutionException, InterruptedException;
}
