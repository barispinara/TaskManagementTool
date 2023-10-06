package com.TaskManagementTool.payload.response;


import com.TaskManagementTool.model.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponse {
    private Project project;
    private Integer totalTasks;
    private Integer completedTasks;
}
