package com.jobportal.backend.controller;

import com.jobportal.backend.entity.Job;
import com.jobportal.backend.service.JobService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    private final JobService service;

    public JobController(JobService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> postJob(@RequestBody Job job) {

        try {
            return ResponseEntity.ok(service.postJob(job));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<Job> getAllJobs(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String location
    ) {

        if (keyword != null || location != null) {
            return service.searchJobs(keyword, location);
        }

        return service.getAllJobs();
    }

    @GetMapping("/{id}")
    public Job getJob(@PathVariable Integer id) {
        return service.getJob(id);
    }

    @GetMapping("/recruiter/{recruiterId}")
    public List<Job> getRecruiterJobs(
        @PathVariable Integer recruiterId
    ) {
        return service.getRecruiterJobs(recruiterId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(
        @PathVariable Integer id,
        @RequestBody Job job
    ) {

        try {
            return ResponseEntity.ok(service.updateJob(id, job));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Integer id) {

        try {
            service.deleteJob(id);
            return ResponseEntity.ok("Job deleted successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}