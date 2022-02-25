package com.example.winninglog.Services;

import com.example.winninglog.Models.LogEntity;
import com.example.winninglog.Models.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    List<Project> findAll();

    Project save(Project project);
    Project findById(Long id);

    void delete(Project project);
}
