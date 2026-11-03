package com.jobportal.backend.service;

import com.jobportal.backend.entity.Company;
import com.jobportal.backend.repository.CompanyRepository;
import com.jobportal.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository repository;
    private final UserRepository userRepository;

    public CompanyService(
        CompanyRepository repository,
        UserRepository userRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Company createCompany(Company company) {

        var recruiter = userRepository.findById(company.getRecruiterId())
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        if (!"RECRUITER".equals(recruiter.getRole())) {
            throw new RuntimeException("Only recruiters can create companies");
        }

        return repository.save(company);
    }

    public List<Company> getAllCompanies() {
        return repository.findAll();
    }

    public Company getCompany(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }
}