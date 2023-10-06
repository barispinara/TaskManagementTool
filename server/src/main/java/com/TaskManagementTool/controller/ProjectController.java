package com.TaskManagementTool.controller;


import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.model.TaskStatus;
import com.TaskManagementTool.payload.request.CreateProjectRequest;
import com.TaskManagementTool.payload.response.ProjectResponse;
import com.TaskManagementTool.service.ProjectService;
import com.TaskManagementTool.service.TaskService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    private final TaskService taskService;

    @PostMapping(value = "/new")
    public ResponseEntity<?> saveProject(@RequestBody CreateProjectRequest createProjectRequest){
        Project newProject;
        try{
            newProject = projectService.saveProject(createProjectRequest.getProjectName());
        } catch(DataIntegrityViolationException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Given name already exists");
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
        Project currProject = projectService.getProjectById(projectId);
        Integer countTasks = taskService.getCountOfTasksByProjectId(projectId);
        Integer countDoneTasks = taskService.getCountOfCompletedTasksByProjectId(projectId, TaskStatus.DONE);
        if (currProject == null) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(projectId + " does not exists");
        }
        return ResponseEntity
                .ok()
                .body(new ProjectResponse(
                        currProject,
                        countTasks,
                        countDoneTasks
                ));
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects(){
        List<Project> projectList = projectService.getAllProjects();
        List<ProjectResponse> projectResponseList = new ArrayList<>();
        for (Project project : projectList) {
            Integer countTasks = taskService.getCountOfTasksByProjectId(project.getId());
            Integer countDoneTasks = taskService.getCountOfCompletedTasksByProjectId(
                    project.getId(),
                    TaskStatus.DONE);
            projectResponseList.add(new ProjectResponse(project, countTasks, countDoneTasks));
        }
        return ResponseEntity
                .ok()
                .body(projectResponseList);
    }

    @DeleteMapping(value = "/{project_id}")
    public ResponseEntity<?> deleteProjectById(@PathVariable("project_id") Long projectId){
        try{
            projectService.deleteProjectById(projectId);
        } catch (NoSuchElementException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(projectId + " does not exists");
        }
        return ResponseEntity
                .ok()
                .body(projectId + " is successfully deleted");
    }
}
