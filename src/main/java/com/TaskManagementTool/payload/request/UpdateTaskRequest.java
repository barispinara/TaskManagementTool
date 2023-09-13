package com.TaskManagementTool.payload.request;

import com.TaskManagementTool.model.TaskStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UpdateTaskRequest {
    private Long id;
    private String taskName;
    private TaskStatus taskStatus;
}
