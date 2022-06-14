package com.losacabaos.skillsharing.student;

import lombok.Data;

import java.util.List;

@Data
public class Statistics {
    private Integer balanceHours;
    private Double avgAssessmentScore;
    private Double totalHours;
    private Double avgCollaborationHours;
    private Integer totalOffers;
    private Integer totalRequests;
    private Integer totalCollaborations;
    private List<String> skillsTakenPart;

}