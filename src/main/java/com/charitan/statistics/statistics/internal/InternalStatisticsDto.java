package com.charitan.statistics.statistics.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class InternalStatisticsDto {
    private final UUID donorId;
    private final long totalProject;
    private final double totalValue;
}
