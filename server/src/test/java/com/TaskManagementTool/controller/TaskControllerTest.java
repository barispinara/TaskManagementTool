package com.TaskManagementTool.controller;

import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.model.Task;
import com.TaskManagementTool.model.TaskStatus;
import com.TaskManagementTool.payload.request.CreateTaskRequest;
import com.TaskManagementTool.payload.request.UpdateTaskRequest;
import com.TaskManagementTool.service.ProjectService;
import com.TaskManagementTool.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    static Project project;
    static Task task;
    static CreateTaskRequest createTaskRequest;
    static UpdateTaskRequest updateTaskRequest;
    static ObjectMapper objectMapper;
    static ObjectWriter objectWriter;

    @BeforeAll
    static void setup(){
        project = Project.builder()
                .id(1L)
                .projectName("testProject")
                .build();
        task = Task.builder()
                .id(1L)
                .taskName("testTask")
                .taskStatus(TaskStatus.TODO)
                .createdDate(new Date())
                .updatedDate(new Date())
                .project(project)
                .build();
        updateTaskRequest = UpdateTaskRequest.builder()
                .id(1L)
                .taskName("updateTask")
                .taskStatus(TaskStatus.PROGRESS)
                .build();
        createTaskRequest = CreateTaskRequest.builder()
                .projectId(1L)
                .taskName("testProject")
                .build();

        objectMapper = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
    }

    @DisplayName("saveTask -> Given task name and project id are valid")
    @Test
    public void givenCreateTaskRequest_whenSaveTask_thenReturnOkStatus() throws Exception {
        when(taskService.saveTask(createTaskRequest.getTaskName(), createTaskRequest.getProjectId())).thenReturn(task);

        mockMvc.perform(post("/task/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(createTaskRequest)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("taskName", Matchers.is("testTask")),
                        jsonPath("taskStatus", Matchers.is("TODO")),
                        jsonPath("id", Matchers.is(1)),
                        jsonPath("project.id", Matchers.is(1)),
                        jsonPath("project.projectName", Matchers.is("testProject"))
                );
    }

    @DisplayName("saveTask -> Given task name is not valid")
    @Test
    public void givenCreateTaskRequest_whenSaveTask_thenReturnBadRequestStatus() throws Exception{
        when(taskService.saveTask(createTaskRequest.getTaskName(), createTaskRequest.getProjectId()))
                .thenThrow(new IllegalArgumentException("The given task name is empty"));

        mockMvc.perform(post("/task/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(createTaskRequest)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().string("The given task name is empty")
                );
    }

    @DisplayName("saveTask -> Given task name is valid but project id does not exist")
    @Test
    public void givenCreateTaskRequest_whenSaveTask_thenReturnNoContentStatus() throws Exception{
        when(taskService.saveTask(createTaskRequest.getTaskName(), createTaskRequest.getProjectId()))
                .thenThrow(new NoSuchElementException(
                        "The given project id does not exist, please check " + createTaskRequest.getProjectId()
                ));

        mockMvc.perform(post("/task/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(createTaskRequest)))
                .andExpectAll(
                        status().isNoContent(),
                        content().string(
                                "The given project id does not exist, please check " +
                                        createTaskRequest.getProjectId()
                        )
                );
    }

    @DisplayName("getTaskById -> Given task id exists")
    @Test
    public void givenLongObject_whenGetTaskById_thenReturnOkStatus() throws Exception{
        long taskId = 1L;

        when(taskService.getTaskById(taskId)).thenReturn(task);

        mockMvc.perform(get("/task/"+taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("taskName", Matchers.is("testTask")),
                        jsonPath("taskStatus", Matchers.is("TODO")),
                        jsonPath("id", Matchers.is(1)),
                        jsonPath("project.id", Matchers.is(1)),
                        jsonPath("project.projectName", Matchers.is("testProject"))
                );
    }

    @DisplayName("getTaskById -> Given task id does not exist")
    @Test
    public void givenLongObject_whenGetTaskById_thenReturnNoContentStatus() throws Exception{
        long taskId = 1L;

        when(taskService.getTaskById(taskId)).thenThrow(new NoSuchElementException(
                "The given task id does not exist " + taskId
        ));

        mockMvc.perform(get("/task/"+taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isNoContent(),
                        content().string(
                                "The given task id does not exist " + taskId
                        )
                );
    }

    @DisplayName("getAllTasks -> When 2 tasks exists in the given project")
    @Test
    public void givenLongObject_whenGetAllTasks_thenReturnOkStatus() throws Exception{
        long projectId = 1L;

        Task secTask = Task.builder()
                .id(2L)
                .taskName("testTask2")
                .taskStatus(TaskStatus.TODO)
                .createdDate(new Date())
                .updatedDate(new Date())
                .project(project)
                .build();

        when(taskService.getAllTasksByProjectId(projectId)).thenReturn(List.of(task, secTask));

        mockMvc.perform(get("/task/all/"+projectId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].taskName", Matchers.is("testTask")),
                        jsonPath("$[0].taskStatus", Matchers.is("TODO")),
                        jsonPath("$[0].id", Matchers.is(1)),
                        jsonPath("$[0].project.id", Matchers.is(1)),
                        jsonPath("$[0].project.projectName", Matchers.is("testProject")),
                        jsonPath("$[1].taskName", Matchers.is("testTask2")),
                        jsonPath("$[1].taskStatus", Matchers.is("TODO")),
                        jsonPath("$[1].id", Matchers.is(2)),
                        jsonPath("$[1].project.id", Matchers.is(1)),
                        jsonPath("$[1].project.projectName", Matchers.is("testProject"))
                );
    }

    @DisplayName("getAllTasks -> When given project doesn't have any task")
    @Test
    public void givenLongObject_whenGetAllTasks_thenReturnOkStatusWithEmptyList() throws Exception{
        long projectId = 1L;

        when(taskService.getAllTasksByProjectId(projectId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/task/all/"+projectId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].taskName").doesNotExist(),
                        jsonPath("$[0].id").doesNotExist(),
                        jsonPath("$[0].project").doesNotExist(),
                        jsonPath("$[1].taskName").doesNotExist(),
                        jsonPath("$[1].taskName").doesNotExist(),
                        jsonPath("$[1].project").doesNotExist()
                );
    }

    @DisplayName("deleteTaskById -> When given task id exists")
    @Test
    public void givenLongObject_whenDeleteTaskById_thenReturnOkStatus() throws Exception{
        long taskId = 1L;

        doNothing().when(taskService).deleteTaskById(taskId);

        mockMvc.perform(delete("/task/"+taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().string(taskId + " is successfully deleted")
                );
    }

    @DisplayName("deleteTaskById -> When given task id does not exist")
    @Test
    public void givenLongObject_whenDeleteTaskById_thenReturnBadRequestStatus() throws Exception{
        long taskId = 1L;

        doThrow(new NoSuchElementException(
                "Given task id does not exist, please check " + taskId
        )).when(taskService).deleteTaskById(taskId);

        mockMvc.perform(delete("/task/"+taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().string(
                                "Given task id does not exist, please check " + taskId
                        )
                );
    }
}
