package com.TaskManagementTool.service;

import com.TaskManagementTool.model.Project;
import com.TaskManagementTool.model.Task;
import com.TaskManagementTool.model.TaskStatus;
import com.TaskManagementTool.payload.request.CreateTaskRequest;
import com.TaskManagementTool.payload.request.UpdateTaskRequest;
import com.TaskManagementTool.repository.TaskRepository;
import com.TaskManagementTool.util.StringUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
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
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private ProjectService projectService;

    private Task task;

    private UpdateTaskRequest updateTaskRequest;

    private Project project;


    @BeforeEach
    public void setup(){
        updateTaskRequest = UpdateTaskRequest.builder()
                .id(1L)
                .taskName("updateTest")
                .taskStatus(TaskStatus.PROGRESS)
                .build();

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


    }

    @DisplayName("saveTask -> Given task name is null")
    @Test
    public void givenTaskNameAndId_whenSaveTask_thenThrowIllegalException(){
        try(MockedStatic<StringUtil> mockedStringUtil = mockStatic(StringUtil.class)){
            mockedStringUtil.when(() -> StringUtil.isNullOrWhiteSpace(null)).thenReturn(true);
            Exception exception = assertThrows(IllegalArgumentException.class,() -> {
                taskService.saveTask(null, project.getId());
            });
            assertEquals(exception.getMessage(), "The given task name is empty");
        }
    }

    @DisplayName("saveTask -> Given task name is valid and project id exists")
    @Test
    public void givenTaskNameAndId_whenSaveTask_thenReturnTaskObject(){
        try(MockedStatic<StringUtil> mockedStringUtil = mockStatic(StringUtil.class)){
            mockedStringUtil.when(() -> StringUtil.isNullOrWhiteSpace(isA(String.class))).thenReturn(false);
            when(projectService.getProjectById(isA(Long.class))).thenReturn(project);
            when(taskRepository.save(isA(Task.class))).thenReturn(task);

            Task newTask = taskService.saveTask(task.getTaskName(), project.getId());

            assertEquals(newTask.getTaskName(), task.getTaskName());
            assertEquals(newTask.getProject(), task.getProject());
            assertEquals(newTask.getTaskStatus(), TaskStatus.TODO);
            assertNotNull(newTask.getCreatedDate());
            assertNotNull(newTask.getUpdatedDate());
        }
    }

    @DisplayName("getTaskById -> When given task id exists")
    @Test
    public void givenLongObject_whenGetTaskById_thenReturnTaskObject(){
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Task currTask = taskService.getTaskById(taskId);

        assertNotNull(currTask);
        assertEquals(currTask, task);
    }

    @DisplayName("getTaskById -> When given task id does not exist")
    @Test
    public void givenLongObject_whenGetTaskById_thenReturnNull(){
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            taskService.getTaskById(taskId);
        });

        assertEquals(exception.getMessage(), "The given task id does not exist " + taskId);
    }

    @DisplayName("getAllTasksByProjectId -> When 2 Tasks exists")
    @Test
    public void givenLongObject_whenGetAllTasksByProjectId_thenReturnTaskList(){
        Task secTask = Task.builder()
                .id(2L)
                .taskName("testTask2")
                .taskStatus(TaskStatus.TODO)
                .createdDate(new Date())
                .updatedDate(new Date())
                .project(project)
                .build();

        Long projectId = 1L;

        when(taskRepository.findTasksByProjectId(projectId)).thenReturn(List.of(secTask, task));

        List<Task> taskList = taskService.getAllTasksByProjectId(projectId);
        assertFalse(taskList.isEmpty());
        assertEquals(2, taskList.size());
    }

    @DisplayName("getAllTasksByProjectId -> When 0 tasks exists")
    @Test
    public void givenLongObject_whenGetAllTasksByProjectId_thenReturnEmptyTaskList(){
        Long projectId = 1L;

        when(taskRepository.findTasksByProjectId(projectId)).thenReturn(Collections.emptyList());

        List<Task> taskList = taskService.getAllTasksByProjectId(projectId);

        assertTrue(taskList.isEmpty());
    }

    @DisplayName("deleteTaskById -> When given Id exists")
    @Test
    public void givenLongObject_whenDeleteTaskById_thenReturnNothing(){
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(true);

        taskService.deleteTaskById(taskId);

        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @DisplayName("deleteTaskById -> When given Id does not exist")
    @Test
    public void givenLongObject_whenDeleteTaskById_thenThrowNoSuchElementException(){
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(false);

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            taskService.deleteTaskById(taskId);
        });

        verify(taskRepository, times(0)).deleteById(taskId);
        assertEquals(exception.getMessage(), "Given task id does not exist, please check " + taskId);
    }

    @DisplayName("updateTask -> When given parameters are valid")
    @Test
    public void givenUpdateTaskRequestObject_whenUpdateTask_thenReturnTaskObject(){
        try(MockedStatic<StringUtil> mockedStringUtil = mockStatic(StringUtil.class)){
            mockedStringUtil.when(() -> StringUtil.isNullOrWhiteSpace(updateTaskRequest.getTaskName()))
                    .thenReturn(false);
            when(taskRepository.findById(updateTaskRequest.getId())).thenReturn(Optional.of(task));
            when(taskRepository.save(isA(Task.class))).thenReturn(task);

            task = taskService.updateTask(updateTaskRequest);

            verify(taskRepository, times(1)).findById(updateTaskRequest.getId());
            verify(taskRepository, times(1)).save(task);
            assertNotNull(task);
            assertEquals(task.getId(), 1L);
            assertEquals(task.getTaskName(), "updateTest");
            assertEquals(task.getTaskStatus(), TaskStatus.PROGRESS);
        }
    }
    @DisplayName("updateTask -> When given task name is empty")
    @Test
    public void givenUpdateTaskRequestObject_whenUpdateTask_thenThrowNoSuchElementException(){
        try(MockedStatic<StringUtil> mockedStringUtil = mockStatic(StringUtil.class)){
            mockedStringUtil.when(() -> StringUtil.isNullOrWhiteSpace(updateTaskRequest.getTaskName()))
                    .thenReturn(true);
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                taskService.updateTask(updateTaskRequest);
            });
            assertEquals(exception.getMessage(), "The given task name is empty");
        }
    }
}
