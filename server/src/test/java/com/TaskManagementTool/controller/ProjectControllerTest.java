package com.TaskManagementTool.controller;

import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.model.TaskStatus;
import com.TaskManagementTool.payload.request.CreateProjectRequest;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    static Project project;
    static CreateProjectRequest createProjectRequest;

    static ObjectMapper objectMapper;
    static ObjectWriter objectWriter;

    @BeforeAll
    static void setup(){

        project = Project.builder()
                .id(1L)
                .projectName("test")
                .build();

        createProjectRequest = CreateProjectRequest.builder()
                .projectName("test")
                .build();

        objectMapper = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
    }

    @DisplayName("saveProject -> Given project name does not exist")
    @Test
    public void givenCreateProjectRequest_whenSaveProject_thenReturnOkStatus() throws Exception {
        when(projectService.saveProject(createProjectRequest.getProjectName()))
                .thenReturn(project);

        mockMvc.perform(post("/project/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(createProjectRequest)))
                .andExpectAll(
                        status().isOk(),
                        content().string(
                                createProjectRequest.getProjectName() + " is created successfully"
                        )
                );
    }

    @DisplayName("saveProject -> Given project name already exists")
    @Test
    public void givenCreateProjectRequest_whenSaveProject_thenReturnBadRequestStatus() throws Exception{
        when(projectService.saveProject(createProjectRequest.getProjectName()))
                .thenThrow(new DataIntegrityViolationException(
                        "The given project name does already exist " + createProjectRequest.getProjectName()
                ));

        mockMvc.perform(post("/project/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(createProjectRequest)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().string(
                                "The given project name does already exist " +
                                        createProjectRequest.getProjectName()
                        )
                );
    }
    @DisplayName("saveProject -> Given project name is null")
    @Test
    public void givenNullString_whenSaveProject_thenReturnNoContentStatus() throws Exception{
        CreateProjectRequest tmpCreateProjectRequest = CreateProjectRequest.builder()
                        .projectName(null)
                        .build();

        when(projectService.saveProject(tmpCreateProjectRequest.getProjectName()))
                .thenThrow(new IllegalArgumentException(
                        "The given project name is empty"
                ));

        mockMvc.perform(post("/project/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(tmpCreateProjectRequest)))
                .andExpectAll(
                        status().isNoContent(),
                        content().string("The given project name is empty")
                );
    }

    @DisplayName("saveProject -> Given project name just has white space")
    @Test
    public void givenWhiteSpaceString_whenSaveProject_thenReturnNoContentStatus() throws Exception{
        CreateProjectRequest tmpCreateProjectRequest = CreateProjectRequest.builder()
                .projectName(" ")
                .build();

        when(projectService.saveProject(tmpCreateProjectRequest.getProjectName()))
                .thenThrow(new IllegalArgumentException("The given project name is empty"));

        mockMvc.perform(post("/project/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(tmpCreateProjectRequest)))
                .andExpectAll(
                        status().isNoContent(),
                        content().string("The given project name is empty")
                );
    }

    @DisplayName("getProjectById -> Given Id exists")
    @Test
    public void givenId_whenGetProjectById_thenReturnOkStatus() throws Exception {
        long id = 1L;

        when(projectService.getProjectById(isA(Long.class))).thenReturn(project);

        mockMvc.perform(get("/project/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("projectName", Matchers.is("test"))
                );
    }

    @DisplayName("getProjectById -> Given Id does not exist")
    @Test
    public void givenId_whenGetProjectById_thenReturnNoContentStatus() throws Exception {
        long id = 1L;

        when(projectService.getProjectById(isA(Long.class)))
                .thenThrow(new NoSuchElementException("The given project id does not exist, " +
                        "please check " + id));

        mockMvc.perform(get("/project/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().string(
                                "The given project id does not exist, please check " + id
                        )
                );
    }

    @DisplayName("getAllProjects -> When 2 project exists")
    @Test
    public void whenGetAllProjects_thenReturnOkStatus() throws Exception {
        Project secProject = Project.builder()
                .id(2L)
                .projectName("secTest")
                .build();

        when(projectService.getAllProjects()).thenReturn(List.of(project,secProject));

        mockMvc.perform(get("/project")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].projectName", Matchers.is("test")),
                        jsonPath("$[1].projectName", Matchers.is("secTest"))
                );
    }

    @DisplayName("getAllProjects -> When no project exists")
    @Test
    public void whenGetAllProjects_thenReturnOkStatusWithEmptyBody() throws Exception{
        when(projectService.getAllProjects()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/project")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].projectName").doesNotExist(),
                        jsonPath("$[1].projectName").doesNotExist()
                );
    }

    @DisplayName("deleteProjectById -> Given Id exists")
    @Test
    public void givenId_whenDeleteProjectById_thenReturnStatusOk() throws Exception{
        Long id = 1L;


        doNothing().when(projectService).deleteProjectById(id);

        mockMvc.perform(delete("/project/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().string(id + " is successfully deleted")
                );
    }

    @DisplayName("deleteProjectById -> Given Id does not exist")
    @Test
    public void givenId_whenDeleteProjectById_thenReturnStatusBadRequest() throws Exception{
        Long id = 1L;

        doThrow(new NoSuchElementException(
                "The given project id does not exist, please check " + id
        )).when(projectService).deleteProjectById(id);

        mockMvc.perform(delete("/project/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().string(
                                "The given project id does not exist, please check " + id
                        )
                );
    }

}
