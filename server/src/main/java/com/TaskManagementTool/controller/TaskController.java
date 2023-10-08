package com.TaskManagementTool.controller;


import com.TaskManagementTool.model.Task;
import com.TaskManagementTool.payload.request.CreateTaskRequest;
import com.TaskManagementTool.payload.request.UpdateTaskRequest;
import com.TaskManagementTool.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    @PostMapping(value = "/new")
    public ResponseEntity<?> saveTask(@RequestBody CreateTaskRequest createTaskRequest){
        Task newTask;
        try{
            newTask = taskService.saveTask(
                    createTaskRequest.getTaskName(),
                    createTaskRequest.getProjectId()
            );
        } catch(IllegalArgumentException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch(NoSuchElementException e){
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(e.getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newTask);
    }

    @GetMapping(value = "/{task_id}")
    public ResponseEntity<?> getTaskById(@PathVariable("task_id") Long taskId){
        Task currTask;
        try{
            currTask = taskService.getTaskById(taskId);
        } catch(NoSuchElementException e){
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(currTask);
    }

    @GetMapping(value = "/all/{project_id}")
    public ResponseEntity<?> getAllTasks(@PathVariable("project_id") Long projectId){
        List<Task> taskList = taskService.getAllTasksByProjectId(projectId);
        return ResponseEntity
                .ok()
                .body(taskList);
    }

    @DeleteMapping(value = "/{task_id}")
    public ResponseEntity<?> deleteTaskById(@PathVariable("task_id") Long taskId){
        try{
            taskService.deleteTaskById(taskId);
        } catch (NoSuchElementException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskId + " is successfully deleted");
    }

    @PutMapping
    public ResponseEntity<?> updateTask(@RequestBody UpdateTaskRequest updateTaskRequest) {
        Task updatedTask;
        try {
            updatedTask = taskService.updateTask(updateTaskRequest);
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedTask);
    }
}
