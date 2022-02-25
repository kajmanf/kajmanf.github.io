package com.example.winninglog;

import com.example.winninglog.Models.User;
import com.example.winninglog.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class WinningLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(WinningLogApplication.class, args);

    }

}
