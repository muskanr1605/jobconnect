package com.jobportal.backend.controller;

import com.jobportal.backend.entity.SavedJob;
import com.jobportal.backend.service.SavedJobService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
@CrossOrigin(origins = "*")
public class SavedJobController {

    private final SavedJobService service;

    public SavedJobController(SavedJobService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> saveJob(
        @RequestBody SavedJob savedJob
    ) {

        try {
            return ResponseEntity.ok(service.saveJob(savedJob));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/seeker/{seekerId}")
    public List<SavedJob> getSavedJobs(
        @PathVariable Integer seekerId
    ) {
        return service.getSavedJobs(seekerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeSavedJob(
        @PathVariable Integer id
    ) {

        try {
            service.removeSavedJob(id);
            return ResponseEntity.ok("Saved job removed successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}