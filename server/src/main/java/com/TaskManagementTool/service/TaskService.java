package com.TaskManagementTool.service;

import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.model.Task;
import com.TaskManagementTool.payload.request.UpdateTaskRequest;
import com.TaskManagementTool.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task saveTask(String taskName, Project project){
        Task newTask = new Task(taskName, project);
        return taskRepository.save(newTask);
    }

    public Task getTaskById(Long id){
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getAllTasksByProjectId(Long projectId){
        return taskRepository.findTasksByProjectId(projectId).orElse(Collections.emptyList());
    }

    public void deleteTaskById(Long id){
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("Given id does not exists, please check " + id);
        }
        taskRepository.deleteById(id);
    }

    public Task updateTask(UpdateTaskRequest updateTaskRequest){
        Optional<Task> optionalTask = taskRepository.findById(updateTaskRequest.getId());
        if (optionalTask.isEmpty()) {
            throw new NoSuchElementException("Given task does not exists, please check " + updateTaskRequest.getId());
        }
        Task currTask = optionalTask.get();
        currTask.setUpdatedDate(new Date());
        currTask.setTaskName(updateTaskRequest.getTaskName());
        currTask.setTaskStatus(updateTaskRequest.getTaskStatus());
        return taskRepository.save(currTask);
    }
}
