package com.jobportal.backend.service;

import com.jobportal.backend.entity.SavedJob;
import com.jobportal.backend.repository.SavedJobRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedJobService {

    private final SavedJobRepository repository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public SavedJobService(
        SavedJobRepository repository,
        JobRepository jobRepository,
        UserRepository userRepository
    ) {
        this.repository = repository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public SavedJob saveJob(SavedJob savedJob) {

        var seeker = userRepository.findById(savedJob.getSeekerId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"SEEKER".equals(seeker.getRole())) {
            throw new RuntimeException("Only seekers can save jobs");
        }

        jobRepository.findById(savedJob.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (repository.existsBySeekerIdAndJobId(
            savedJob.getSeekerId(),
            savedJob.getJobId()
        )) {
            throw new RuntimeException("Job already saved");
        }

        return repository.save(savedJob);
    }

    public List<SavedJob> getSavedJobs(Integer seekerId) {
        return repository.findBySeekerId(seekerId);
    }

    public void removeSavedJob(Integer id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Saved job not found");
        }

        repository.deleteById(id);
    }
}