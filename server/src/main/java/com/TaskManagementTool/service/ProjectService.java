package com.TaskManagementTool.service;

import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project saveProject(String projectName){
        Optional<Project> currProject = projectRepository.findProjectByProjectName(projectName);
        if (currProject.isPresent()) {
            throw new DataIntegrityViolationException("The given project name does already exists " + projectName);
        }
        Project newProject = new Project(projectName);
        return projectRepository.save(newProject);
    }

    public Project getProjectById(Long id){
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> getAllProjects(){
        return (List<Project>) projectRepository.findAll();
    }

    public void deleteProjectById(Long id){
        if (!projectRepository.existsById(id)) {
            throw new NoSuchElementException("Given id does not exists, please check " + id);
        }
        projectRepository.deleteById(id);
    }
}