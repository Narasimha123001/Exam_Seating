package com.techtricks.Exam_Seating.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class AllocationResponse {

    private String slotDate;
    private String slotTime;
    private int sessionsAllocated;
    private List<Long> sessionIds;
    private List<Long> selectedRooms;
    private int assignedTotal;
    private int seatsTotal;
    private int unassignedSeats;
    private Map<Long, Integer> perSessionAssigned;
    private Map<String, Object> allocationMetrics;
}
