package org.identityshelf.jobsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "org.identityshelf.jobsystem",
    "org.identityshelf.core"
})
public class JobSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobSystemApplication.class, args);
    }
}
