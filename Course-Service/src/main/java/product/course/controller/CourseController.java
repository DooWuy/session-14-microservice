package product.course.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CourseController {

    private static final List<Map<String, Object>> COURSES = new ArrayList<>();

    static {
        COURSES.add(Map.of("id", 1L, "title", "Spring Boot Microservices căn bản", "duration", "40 hours"));
        COURSES.add(Map.of("id", 2L, "title", "Thiết kế hệ thống High-Performance", "duration", "60 hours"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('STUDENT') or hasAuthority('INSTRUCTOR')")
    public ResponseEntity<List<Map<String, Object>>> getAllCourses() {
        return ResponseEntity.ok(COURSES);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<Map<String, Object>> createCourse(@RequestBody Map<String, Object> newCourse) {
        Map<String, Object> savedCourse = new ConcurrentHashMap<>(newCourse);
        savedCourse.put("id", (long) (COURSES.size() + 1));

        COURSES.add(savedCourse);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }



}
