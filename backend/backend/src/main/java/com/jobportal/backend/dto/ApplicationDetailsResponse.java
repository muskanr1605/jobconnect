package com.jobportal.backend.dto;

import java.time.LocalDateTime;

public class ApplicationDetailsResponse {

    private Integer applicationId;
    private String applicantName;
    private String email;
    private String phone;
    private String jobTitle;
    private LocalDateTime appliedAt;
    private String status;
    private String resumePath;
    private String coverLetter;

    public ApplicationDetailsResponse(
        Integer applicationId,
        String applicantName,
        String email,
        String phone,
        String jobTitle,
        LocalDateTime appliedAt,
        String status,
        String resumePath,
        String coverLetter
    ) {
        this.applicationId = applicationId;
        this.applicantName = applicantName;
        this.email = email;
        this.phone = phone;
        this.jobTitle = jobTitle;
        this.appliedAt = appliedAt;
        this.status = status;
        this.resumePath = resumePath;
        this.coverLetter = coverLetter;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public String getStatus() {
        return status;
    }

    public String getResumePath() {
        return resumePath;
    }

    public String getCoverLetter() {
        return coverLetter;
    }
}