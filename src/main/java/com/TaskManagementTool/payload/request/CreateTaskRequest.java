package com.TaskManagementTool.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateTaskRequest {
    private String taskName;
    private Long projectId;
}
