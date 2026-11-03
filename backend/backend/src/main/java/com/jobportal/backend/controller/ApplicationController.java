package com.jobportal.backend.controller;

import com.jobportal.backend.entity.Application;
import com.jobportal.backend.dto.ApplicationDetailsResponse;
import com.jobportal.backend.service.ApplicationService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(
        ApplicationService service
    ) {
        this.service = service;
    }

    // Apply for a job
    @PostMapping
    public ResponseEntity<?> applyForJob(
        @RequestBody Application application
    ) {
        try {
            return ResponseEntity.ok(
                service.applyForJob(application)
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                e.getMessage()
            );
        }
    }

    // Get all applications
    @GetMapping
    public List<Application> getAllApplications() {
        return service.getAllApplications();
    }

    // Get applications of a particular seeker
    @GetMapping("/seeker/{seekerId}")
    public List<Application> getSeekerApplications(
        @PathVariable Integer seekerId
    ) {
        return service.getSeekerApplications(seekerId);
    }

    // Get applications for a particular job
    @GetMapping("/job/{jobId}")
    public List<Application> getJobApplications(
        @PathVariable Integer jobId
    ) {
        return service.getJobApplications(jobId);
    }

    // Get all applicants for a recruiter's jobs
    @GetMapping("/recruiter/{recruiterId}")
    public List<ApplicationDetailsResponse> getRecruiterApplications(
        @PathVariable Integer recruiterId
    ) {
        return service.getRecruiterApplications(recruiterId);
    }

    // Update application status
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
        @PathVariable Integer id,
        @RequestBody Map<String, String> request
    ) {
        try {
            return ResponseEntity.ok(
                service.updateStatus(
                    id,
                    request.get("status")
                )
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                e.getMessage()
            );
        }
    }
}