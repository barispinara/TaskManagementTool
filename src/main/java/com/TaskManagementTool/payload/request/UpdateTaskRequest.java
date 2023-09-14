package com.TaskManagementTool.payload.request;

import com.TaskManagementTool.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UpdateTaskRequest {
    private Long id;
    private String taskName;
    private TaskStatus taskStatus;
}
