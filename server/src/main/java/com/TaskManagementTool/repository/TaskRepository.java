package com.TaskManagementTool.repository;

import com.TaskManagementTool.model.Task;
import com.TaskManagementTool.model.TaskStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findTasksByProjectId(Long projectId);

    Integer countTasksByProjectId(Long projectId);

    Integer countTasksByProjectIdAndTaskStatus(Long projectId, TaskStatus taskStatus);
}
