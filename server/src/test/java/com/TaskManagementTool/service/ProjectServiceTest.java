package com.TaskManagementTool.service;

import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;


import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;

    @BeforeEach
    public void setup(){
        project = Project.builder()
                .id(1L)
                .projectName("test")
                .build();
    }

    @DisplayName("saveProject -> Given project name does not exist")
    @Test
    public void givenProjectObject_whenSaveProject_thenReturnProjectObject(){
        String projectName = "test";

        when(projectRepository.findProjectByProjectName(isA(String.class))).thenReturn(Optional.empty());
        when(projectRepository.save(isA(Project.class))).thenReturn(project);

        Project newProject = projectService.saveProject(projectName);

        assertNotNull(newProject);
        assertEquals(newProject, project);
        assertEquals(newProject.getProjectName(), projectName);
    }

    @DisplayName("saveProject -> Given project name already exists")
    @Test
    public void givenProjectObject_whenSaveProject_thenThrowDataIntegrityViolationException(){
        String projectName = "test";

        when(projectRepository.findProjectByProjectName(isA(String.class))).thenReturn(Optional.of(project));

        assertThrows(DataIntegrityViolationException.class, () -> {
            projectService.saveProject(projectName);
        });

        verify(projectRepository, times(0)).save(project);
    }

    @DisplayName("getProjectById -> When given Id exists")
    @Test
    public void givenLongObject_whenGetProjectById_thenReturnProjectObject(){
        Long projectId = 1L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        Project currProject = projectService.getProjectById(projectId);

        assertNotNull(currProject);
        assertEquals(currProject, project);
    }

    @DisplayName("getProjectById -> When given Id does not exist")
    @Test
    public void givenLongObject_whenGetProjectById_thenReturnNull(){
        Long projectId = 1L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            projectService.getProjectById(projectId);
        });

        assertEquals(
                exception.getMessage(),
                "The given project id does not exist, please check " + projectId
        );
    }

    @DisplayName("getAllProjects -> When 2 project exists")
    @Test
    public void whenGetAllProjects_thenReturnProjectList(){
        Project secProject = Project.builder()
                .id(2L)
                .projectName("test2")
                .build();
        when(projectRepository.findAll()).thenReturn(List.of(project,secProject));
        List<Project> projectList = projectService.getAllProjects();

        assertFalse(projectList.isEmpty());
        assertEquals(2, projectList.size());
    }

    @DisplayName("getAllProjects -> When 0 project exists")
    @Test
    public void whenGetAllProjects_thenReturnEmptyProjectList(){
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());
        List<Project> projectList = projectService.getAllProjects();

        assertTrue(projectList.isEmpty());
    }

    @DisplayName("deleteProjectById -> When given Id exists")
    @Test
    public void givenLongObject_whenDeleteProjectById_thenReturnNothing(){
        Long projectId = 1L;

        when(projectRepository.existsById(projectId)).thenReturn(true);
        doNothing().when(projectRepository).deleteById(projectId);

        projectService.deleteProjectById(projectId);

        verify(projectRepository, times(1)).deleteById(projectId);
    }

    @DisplayName("deleteProjectById -> When given Id does not exist")
    @Test
    public void givenLongObject_whenDeleteProjectById_thenThrowNoSuchElementException(){
        Long projectId = 1L;

        when(projectRepository.existsById(projectId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> {
            projectService.deleteProjectById(projectId);
        });

        verify(projectRepository, times(0)).deleteById(projectId);
    }
}
