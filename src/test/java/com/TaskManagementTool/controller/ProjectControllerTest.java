package com.TaskManagementTool.controller;

import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.service.ProjectService;
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
    static ObjectMapper objectMapper;
    static ObjectWriter objectWriter;

    @BeforeAll
    static void setup(){

        project = Project.builder()
                .id(1L)
                .projectName("test")
                .build();

        objectMapper = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
    }

    @DisplayName("saveProject -> Given project name does not exist")
    @Test
    public void givenProjectName_whenSaveProject_thenReturnOkStatus() throws Exception {
        String projectName = "test";

        when(projectService.saveProject(isA(String.class))).thenReturn(project);

        mockMvc.perform(post("/project/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(projectName)))
                .andExpectAll(
                        status().isOk(),
                        content().string(projectName + " is created successfully")
                );
    }

    @DisplayName("saveProject -> Given project name already exists")
    @Test
    public void givenProjectName_whenSaveProject_thenReturnBadRequestStatus() throws Exception{
        String projectName = "test";

        when(projectService.saveProject(isA(String.class))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/project/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(projectName)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().string("Given name already exists")
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

        when(projectService.getProjectById(isA(Long.class))).thenReturn(null);

        mockMvc.perform(get("/project/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isNoContent(),
                        content().string(id + " does not exists")
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
        Project secProject = Project.builder()
                .id(2L)
                .projectName("secTest")
                .build();

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

        doThrow(NoSuchElementException.class).when(projectService).deleteProjectById(id);

        mockMvc.perform(delete("/project/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().string(id + " does not exists")
                );
    }

}