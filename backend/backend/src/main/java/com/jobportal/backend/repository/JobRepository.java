package com.jobportal.backend.repository;

import com.jobportal.backend.entity.Job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {

    List<Job> findByRecruiterId(Integer recruiterId);

    @Query("""
        SELECT j FROM Job j
        WHERE (:keyword IS NULL OR
        LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:location IS NULL OR
        LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
        AND j.status = 'OPEN'
    """)
    List<Job> searchJobs(
        @Param("keyword") String keyword,
        @Param("location") String location
    );
}