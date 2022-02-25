package com.example.winninglog.Services;

import com.example.winninglog.Models.LogEntity;
import com.example.winninglog.Models.Project;
import com.example.winninglog.Repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DatabaseProjectService implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public DatabaseProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project findById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()){
            return project.get();
        }
        return null;
    }

    @Override
    public void delete(Project project) {
        projectRepository.delete(project);
    }

}
