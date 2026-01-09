package com.techtricks.Exam_Seating.exception;

public class InsufficientRoomCapacityException extends AllocationAlreadyExistsException {
    public InsufficientRoomCapacityException(String message) {
        super(message);
    }
}
