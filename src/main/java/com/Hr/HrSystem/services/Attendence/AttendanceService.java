//package com.Hr.HrSystem.services.Attendence;
//
//import com.Hr.HrSystem.dto.AttendanceDTO;
//import com.Hr.HrSystem.entity.Attendance;
//import com.Hr.HrSystem.entity.User;
//import com.Hr.HrSystem.repository.AttendanceRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class AttendanceService {
//    @Autowired
//    private AttendanceRepository attendanceRepository;
//
//    public Attendance recordAttendance(AttendanceDTO attendanceDTO) {
//        Attendance attendance = new Attendance();
//        attendance.setUser(new User(attendanceDTO.getUserId()));
//        attendance.setCheckInTime(attendanceDTO.getCheckInTime());
//        attendance.setCheckOutTime(attendanceDTO.getCheckOutTime());
//        return attendanceRepository.save(attendance);
//    }
//
//    public List<Attendance> getAttendanceByUser(Long userId) {
//        return attendanceRepository.findByUserId(userId);
//    }
//}
