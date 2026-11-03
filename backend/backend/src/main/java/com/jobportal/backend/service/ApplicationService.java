package com.jobportal.backend.service;

import com.jobportal.backend.entity.Application;
import com.jobportal.backend.dto.ApplicationDetailsResponse;

import com.jobportal.backend.repository.ApplicationRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository repository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationService(
        ApplicationRepository repository,
        JobRepository jobRepository,
        UserRepository userRepository
    ) {
        this.repository = repository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    // Apply for a job
    public Application applyForJob(Application application) {

        var job = jobRepository.findById(application.getJobId())
                .orElseThrow(() ->
                    new RuntimeException("Job not found")
                );

        if (!"OPEN".equals(job.getStatus())) {
            throw new RuntimeException("This job is closed");
        }

        var seeker = userRepository.findById(application.getSeekerId())
                .orElseThrow(() ->
                    new RuntimeException("User not found")
                );

        if (!"SEEKER".equals(seeker.getRole())) {
            throw new RuntimeException(
                "Only job seekers can apply"
            );
        }

        if (repository.existsByJobIdAndSeekerId(
            application.getJobId(),
            application.getSeekerId()
        )) {
            throw new RuntimeException(
                "Already applied for this job"
            );
        }

        application.setStatus("PENDING");

        return repository.save(application);
    }

    // Get all applications
    public List<Application> getAllApplications() {
        return repository.findAll();
    }

    // Get applications of a particular seeker
    public List<Application> getSeekerApplications(
        Integer seekerId
    ) {
        return repository.findBySeekerId(seekerId);
    }

    // Get applications for a particular job
    public List<Application> getJobApplications(
        Integer jobId
    ) {
        return repository.findByJobId(jobId);
    }

    // Get all applicants for a recruiter's jobs
    public List<ApplicationDetailsResponse> getRecruiterApplications(
        Integer recruiterId
    ) {
        return repository.findApplicantsByRecruiterId(recruiterId);
    }

    // Update application status
    public Application updateStatus(
        Integer id,
        String status
    ) {

        Application application = repository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Application not found")
                );

        if (status == null) {
            throw new RuntimeException(
                "Application status is required"
            );
        }

        String updatedStatus = status.trim().toUpperCase();

        if (!List.of(
            "PENDING",
            "REVIEWED",
            "SHORTLISTED",
            "REJECTED"
        ).contains(updatedStatus)) {

            throw new RuntimeException(
                "Invalid application status"
            );
        }

        application.setStatus(updatedStatus);

        return repository.save(application);
    }
}