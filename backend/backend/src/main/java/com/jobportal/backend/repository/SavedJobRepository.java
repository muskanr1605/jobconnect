package com.jobportal.backend.repository;

import com.jobportal.backend.entity.SavedJob;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SavedJobRepository
        extends JpaRepository<SavedJob, Integer> {

    List<SavedJob> findBySeekerId(Integer seekerId);

    boolean existsBySeekerIdAndJobId(
        Integer seekerId,
        Integer jobId
    );
}