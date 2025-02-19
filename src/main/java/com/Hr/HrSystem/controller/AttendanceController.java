package com.Hr.HrSystem.controller;

import com.Hr.HrSystem.entity.Attendance;
import com.Hr.HrSystem.entity.User;
import com.Hr.HrSystem.repository.AttendanceRepository;
import com.Hr.HrSystem.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public AttendanceController(AttendanceRepository attendanceRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn(@RequestParam Long userId, @RequestParam double latitude, @RequestParam double longitude) {
        LocalDateTime currentTime = LocalDateTime.now();  // Get the current time
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOptional.get();
        double distance = calculateDistance(latitude, longitude, user.getAccount().getLatitude(), user.getAccount().getLongitude());
        if (distance > 5) {
            return ResponseEntity.badRequest().body("You are too far from the office location.");
        }

        // Combine the current date with the user's start time (LocalTime)
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), user.getAccount().getStartTime());

        if (currentTime.isAfter(startTime)) {
            return ResponseEntity.badRequest().body("You are late");
        }

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setLate(LocalDateTime.now().isAfter(LocalDateTime.of(LocalDate.now(), user.getAccount().getStartTime().plusHours(9))));

        attendanceRepository.save(attendance);
        return ResponseEntity.ok("Checked in successfully.");
    }

    @PostMapping("/check-out")
    public ResponseEntity<String> checkOut(@RequestParam Long userId) {
        Optional<Attendance> attendanceOptional = attendanceRepository.findByUserIdAndCheckOutTimeIsNull(userId);
        if (!attendanceOptional.isPresent()) {
            return ResponseEntity.badRequest().body("No check-in record found.");
        }
        Attendance attendance = attendanceOptional.get();
        if (ChronoUnit.HOURS.between(attendance.getCheckInTime(), LocalDateTime.now()) < 8) {
            return ResponseEntity.badRequest().body("You must work at least 8 hours before checking out.");
        }
        attendance.setCheckOutTime(LocalDateTime.now());
        attendanceRepository.save(attendance);
        return ResponseEntity.ok("Checked out successfully.");
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c * 1000;
    }
}
