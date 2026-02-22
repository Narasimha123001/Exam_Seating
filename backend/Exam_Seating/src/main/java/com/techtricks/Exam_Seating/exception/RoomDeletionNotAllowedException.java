package com.techtricks.Exam_Seating.exception;

public class RoomDeletionNotAllowedException extends RuntimeException {
    public RoomDeletionNotAllowedException(String message) {
        super(message);
    }
}
