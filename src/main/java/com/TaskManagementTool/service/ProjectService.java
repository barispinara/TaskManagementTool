package com.TaskManagementTool.service;

import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.payload.request.ProjectRequest;
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

    public Project saveProject(ProjectRequest projectRequest){
        Optional<Project> currProject = projectRepository.getProjectByProjectName(projectRequest.getProjectName());
        if (currProject.isPresent()) {
            throw new DataIntegrityViolationException("The given project name does already exists " + projectRequest.getProjectName());
        }
        Project newProject = Project.builder()
                .projectName(projectRequest.getProjectName())
                .build();

        return projectRepository.save(newProject);
    }

    public Project getProjectById(Long id){
        return projectRepository.getProjectById(id).orElse(null);
    }

    public List<Project> getAllProjects(){
        return (List<Project>) projectRepository.findAll();
    }

    public void deleteProjectById(Long id){
        projectRepository.deleteById(id);
    }

    public Project updateProject(ProjectRequest projectRequest){
        Optional<Project> currProject = projectRepository.getProjectByProjectName(projectRequest.getProjectName());
        if (currProject.isEmpty()) {
            throw new NoSuchElementException("Given project information does not exists " + projectRequest.getProjectName());
        }
        currProject.get().setProjectName(projectRequest.getProjectName());
        return projectRepository.save(currProject.get());
    }
}
