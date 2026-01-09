package com.techtricks.Exam_Seating.exception;

public class DuplicateRegistrationException extends AllocationAlreadyExistsException {

    public DuplicateRegistrationException(String message) {
        super(message);
    }
}
