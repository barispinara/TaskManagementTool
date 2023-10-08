package com.TaskManagementTool.service;

import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.model.Task;
import com.TaskManagementTool.payload.request.UpdateTaskRequest;
import com.TaskManagementTool.repository.TaskRepository;
import com.TaskManagementTool.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final ProjectService projectService;

    public Task saveTask(String taskName, Long projectId){
        if (StringUtil.isNullOrWhiteSpace(taskName)){
            throw new IllegalArgumentException("The given task name is empty");
        }
        Project givenProject = projectService.getProjectById(projectId);
        Task newTask = new Task(taskName, givenProject);
        return taskRepository.save(newTask);
    }

    public Task getTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("The given task id does not exist " + id)
        );
    }

    public List<Task> getAllTasksByProjectId(Long projectId){
        return taskRepository.findTasksByProjectId(projectId);
    }

    public void deleteTaskById(Long id){
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("Given task id does not exist, please check " + id);
        }
        taskRepository.deleteById(id);
    }

    public Task updateTask(UpdateTaskRequest updateTaskRequest){
        if (StringUtil.isNullOrWhiteSpace(updateTaskRequest.getTaskName())){
            throw new IllegalArgumentException("The given task name is empty");
        }
        Task currTask = getTaskById(updateTaskRequest.getId());
        currTask.setUpdatedDate(new Date());
        currTask.setTaskName(updateTaskRequest.getTaskName());
        currTask.setTaskStatus(updateTaskRequest.getTaskStatus());
        return taskRepository.save(currTask);
    }
}
