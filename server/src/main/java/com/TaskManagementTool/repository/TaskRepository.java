package com.TaskManagementTool.repository;

import com.TaskManagementTool.model.Task;
import com.TaskManagementTool.model.TaskStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    Optional<List<Task>> findTasksByProjectId(Long projectId);

    Integer countTasksByProjectId(Long projectId);

    Integer countTasksByProjectIdAndTaskStatus(Long projectId, TaskStatus taskStatus);
}
