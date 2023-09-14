package com.TaskManagementTool.controller;


import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping(value = "/new")
    public ResponseEntity<?> saveProject(@RequestBody String projectName){
        Project newProject = projectService.saveProject(projectName);
        return ResponseEntity
                .ok()
                .body(projectName + " is created successfully");
    }

    @GetMapping(value = "/{project_id}")
    public ResponseEntity<?> getProjectById(@PathVariable("project_id") Long projectId){
        Project currProject = projectService.getProjectById(projectId);
        if (currProject == null) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body("The given id does not exists");
        }
        return ResponseEntity
                .ok()
                .body(currProject);
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects(){
        List<Project> projectList = projectService.getAllProjects();
        return ResponseEntity
                .ok()
                .body(projectList);
    }

    @DeleteMapping(value = "/{project_id")
    public ResponseEntity<?> deleteProjectById(@PathVariable("project_id") Long projectId){
        try{
            projectService.deleteProjectById(projectId);
        } catch (NoSuchElementException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e);
        }
        return ResponseEntity
                .ok()
                .body(projectId + " is successfully deleted");
    }
}
