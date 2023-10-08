package com.TaskManagementTool.repository;

import com.TaskManagementTool.model.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProjectRepository extends CrudRepository<Project, Long> {

    Optional<Project> findProjectByProjectName(String projectName);

}
