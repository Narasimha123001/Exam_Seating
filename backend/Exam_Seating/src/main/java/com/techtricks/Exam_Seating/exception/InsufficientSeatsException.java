package com.techtricks.Exam_Seating.exception;

public class InsufficientSeatsException extends AllocationException {

    public InsufficientSeatsException(String message) {
        super(message);
    }
}
