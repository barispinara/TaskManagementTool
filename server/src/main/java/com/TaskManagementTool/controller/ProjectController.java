package com.TaskManagementTool.controller;


import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.payload.request.CreateProjectRequest;
import com.TaskManagementTool.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping(value = "/new")
    public ResponseEntity<?> saveProject(@RequestBody CreateProjectRequest createProjectRequest){
        Project newProject;
        try{
            newProject = projectService.saveProject(createProjectRequest.getProjectName());
        } catch(DataIntegrityViolationException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch(IllegalArgumentException e){
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(e.getMessage());
        }
        return ResponseEntity
                .ok()
                .body(newProject.getProjectName() + " is created successfully");
    }

    @GetMapping(value = "/{project_id}")
    public ResponseEntity<?> getProjectById(@PathVariable("project_id") Long projectId){
        Project currProject;
        try{
            currProject = projectService.getProjectById(projectId);
        } catch(NoSuchElementException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(currProject);
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects(){
        List<Project> projectList = projectService.getAllProjects();
        return ResponseEntity
                .ok()
                .body(projectList);
    }

    @DeleteMapping(value = "/{project_id}")
    public ResponseEntity<?> deleteProjectById(@PathVariable("project_id") Long projectId){
        try{
            projectService.deleteProjectById(projectId);
        } catch (NoSuchElementException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        return ResponseEntity
                .ok()
                .body(projectId + " is successfully deleted");
    }
}
