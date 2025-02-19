package com.Hr.HrSystem.repository;


import com.Hr.HrSystem.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByUserId(Long userId);

    Optional<Attendance> findByUserIdAndCheckOutTimeIsNull(Long userId);
}

