package com.jobportal.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {

        SpringApplication.run(BackendApplication.class, args);

        System.out.println("==================================");
        System.out.println("     JOBCONNECT BACKEND STARTED   ");
        System.out.println("==================================");
    }
}