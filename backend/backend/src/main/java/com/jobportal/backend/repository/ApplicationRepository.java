package com.jobportal.backend.repository;

import com.jobportal.backend.entity.Application;
import com.jobportal.backend.dto.ApplicationDetailsResponse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository
        extends JpaRepository<Application, Integer> {

    List<Application> findBySeekerId(Integer seekerId);

    List<Application> findByJobId(Integer jobId);

    boolean existsByJobIdAndSeekerId(
        Integer jobId,
        Integer seekerId
    );

    // Fetch all applicants for jobs posted by a particular recruiter
    @Query("""
        SELECT new com.jobportal.backend.dto.ApplicationDetailsResponse(
            a.applicationId,
            u.name,
            u.email,
            u.phone,
            j.jobTitle,
            a.appliedAt,
            a.status,
            a.resumePath,
            a.coverLetter
        )
        FROM Application a
        JOIN User u ON u.userId = a.seekerId
        JOIN Job j ON j.jobId = a.jobId
        WHERE j.recruiterId = :recruiterId
        ORDER BY a.appliedAt DESC
    """)
    List<ApplicationDetailsResponse> findApplicantsByRecruiterId(
        @Param("recruiterId") Integer recruiterId
    );
}