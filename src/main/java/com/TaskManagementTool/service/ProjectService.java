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

    public Project saveProject(Project project){
        Optional<Project> currProject = projectRepository.findProjectByProjectName(project.getProjectName());
        if (currProject.isPresent()) {
            throw new DataIntegrityViolationException("The given project name does already exists " + project.getProjectName());
        }

        return projectRepository.save(project);
    }

    public Project getProjectById(Long id){
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> getAllProjects(){
        return (List<Project>) projectRepository.findAll();
    }

    public void deleteProjectById(Long id){
        Optional<Project> currProject = projectRepository.findById(id);
        if (currProject.isEmpty()) {
            throw new NoSuchElementException("Given id does not exists, please check " + id);
        }
        projectRepository.delete(currProject.get());
    }

}
