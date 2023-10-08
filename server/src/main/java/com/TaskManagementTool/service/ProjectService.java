package com.TaskManagementTool.service;

import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.repository.ProjectRepository;
import com.TaskManagementTool.util.StringUtil;
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

    public Project saveProject(String projectName) {
        if (StringUtil.isNullOrWhiteSpace(projectName)) {
            throw new IllegalArgumentException("The given project name is empty");
        }
        Optional<Project> currProject = projectRepository.findProjectByProjectName(projectName);
        if (currProject.isPresent()) {
            throw new DataIntegrityViolationException("The given project name does already exist " + projectName);
        }
        Project newProject = new Project(projectName);
        return projectRepository.save(newProject);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("The given project id does not exist, please check " + id));
    }

    public List<Project> getAllProjects() {
        return (List<Project>) projectRepository.findAll();
    }

    public void deleteProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new NoSuchElementException("Given project id does not exists, please check " + id);
        }
        projectRepository.deleteById(id);
    }
}
