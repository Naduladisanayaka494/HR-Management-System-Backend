package com.Hr.HrSystem.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AttendanceDTO {
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    // Constructor, Getters, and Setters
}
