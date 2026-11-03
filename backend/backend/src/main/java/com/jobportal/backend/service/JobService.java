package com.jobportal.backend.service;

import com.jobportal.backend.entity.Job;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.CompanyRepository;
import com.jobportal.backend.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository repository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public JobService(
        JobRepository repository,
        UserRepository userRepository,
        CompanyRepository companyRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public Job postJob(Job job) {

        var recruiter = userRepository.findById(job.getRecruiterId())
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        if (!"RECRUITER".equals(recruiter.getRole())) {
            throw new RuntimeException("Only recruiters can post jobs");
        }

        companyRepository.findById(job.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (job.getSalaryMin() != null &&
            job.getSalaryMax() != null &&
            job.getSalaryMax().compareTo(job.getSalaryMin()) < 0) {

            throw new RuntimeException("Invalid salary range");
        }

        job.setStatus("OPEN");

        return repository.save(job);
    }

    public List<Job> getAllJobs() {
        return repository.findAll();
    }

    public Job getJob(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public List<Job> searchJobs(String keyword, String location) {

        return repository.searchJobs(
            keyword == null || keyword.isBlank() ? null : keyword,
            location == null || location.isBlank() ? null : location
        );
    }

    public List<Job> getRecruiterJobs(Integer recruiterId) {
        return repository.findByRecruiterId(recruiterId);
    }

    public Job updateJob(Integer id, Job updatedJob) {

        Job job = getJob(id);

        job.setJobTitle(updatedJob.getJobTitle());
        job.setDescription(updatedJob.getDescription());
        job.setLocation(updatedJob.getLocation());
        job.setSalaryMin(updatedJob.getSalaryMin());
        job.setSalaryMax(updatedJob.getSalaryMax());
        job.setJobType(updatedJob.getJobType());
        job.setExperience(updatedJob.getExperience());
        job.setStatus(updatedJob.getStatus());

        return repository.save(job);
    }

    public void deleteJob(Integer id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Job not found");
        }

        repository.deleteById(id);
    }
}