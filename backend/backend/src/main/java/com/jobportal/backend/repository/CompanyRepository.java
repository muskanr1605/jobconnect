package com.jobportal.backend.repository;

import com.jobportal.backend.entity.Company;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    List<Company> findByRecruiterId(Integer recruiterId);
}