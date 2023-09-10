package com.TaskManagementTool.service;

import com.TaskManagementTool.model.Task;
import com.TaskManagementTool.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task saveTask(Task task){
        Optional<Task> currTask = taskRepository.findTaskByTaskName(task.getTaskName());
        if (currTask.isPresent()) {
            throw new DataIntegrityViolationException("The given task already exists " + task.getTaskName());
        }

        return taskRepository.save(task);
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

    public Task updateTask(Task task){
        if(!taskRepository.existsById(task.getId())){
            throw new NoSuchElementException("Given task does not exists, please check " + task.getId());
        }
        return taskRepository.save(task);
    }
}
