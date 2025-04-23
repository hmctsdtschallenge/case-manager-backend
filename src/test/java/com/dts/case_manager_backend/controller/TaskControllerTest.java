package com.dts.case_manager_backend.controller;

import com.dts.case_manager_backend.exception.InvalidDTOException;
import com.dts.case_manager_backend.exception.TaskNotFoundException;
import com.dts.case_manager_backend.model.Task;
import com.dts.case_manager_backend.model.TaskDTO;
import com.dts.case_manager_backend.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Autowired
    private MockMvc mockMvcController;

    private ObjectMapper mapper;
    private JavaTimeModule timeModule;

    @BeforeEach
    public void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(taskController).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("postTask returns correct Task and CREATEd (201) when passed valid TaskDTO")
    void postTaskValidDTO() throws Exception {
        //Arrange
        TaskDTO inputTaskDTO = new TaskDTO(
                "test title",
                "test description",
                "In progress",
                LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1),
                LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2));

        Task expectedTask = Task.builder()
                .id(1L)
                .title("test title")
                .description("test description")
                .status("In progress")
                .createdDate(LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1))
                .dueDate(LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2))
                .build();

        when(taskService.createTask(inputTaskDTO)).thenReturn(expectedTask);

        //Act
        ResultActions response = mockMvcController.perform(
                post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputTaskDTO))
        );

        //Assert
        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedTask.getId()))
                .andExpect(jsonPath("$.title").value(inputTaskDTO.title()))
                .andExpect(jsonPath("$.description").value(inputTaskDTO.description()))
                .andExpect(jsonPath("$.status").value(inputTaskDTO.status()))
                .andExpect(jsonPath("$.createdDate").value(inputTaskDTO.createdDate()))
                .andExpect(jsonPath("$.dueDate").value(inputTaskDTO.dueDate()));
    }

    @Test
    @DisplayName("postTask returns Unprocessable (422) when passed TaskDTO with null fields")
    void postTaskDTONullFields() throws Exception {
        //Arrange
        TaskDTO inputTaskDTO1 = new TaskDTO(
                null,
                "test description",
                "In progress",
                LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1),
                LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2));

        TaskDTO inputTaskDTO2 = new TaskDTO(
                "test title",
                null,
                "In progress",
                LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1),
                LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2));

        TaskDTO inputTaskDTO3 = new TaskDTO(
                "test title",
                "test description",
                null,
                LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1),
                LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2));

        TaskDTO inputTaskDTO4 = new TaskDTO(
                "test title",
                "test description",
                "In progress",
                null,
                LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2));

        TaskDTO inputTaskDTO5 = new TaskDTO(
                "test title",
                "test description",
                "In progress",
                LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1),
                null);

        TaskDTO inputTaskDTO6 = new TaskDTO(
                null,
                null,
                null,
                null,
                null);

        when(taskService.createTask(inputTaskDTO1)).thenThrow(InvalidDTOException.class);
        when(taskService.createTask(inputTaskDTO2)).thenThrow(InvalidDTOException.class);
        when(taskService.createTask(inputTaskDTO3)).thenThrow(InvalidDTOException.class);
        when(taskService.createTask(inputTaskDTO4)).thenThrow(InvalidDTOException.class);
        when(taskService.createTask(inputTaskDTO5)).thenThrow(InvalidDTOException.class);
        when(taskService.createTask(inputTaskDTO6)).thenThrow(InvalidDTOException.class);

        //Act
        ResultActions response1 = mockMvcController.perform(
                post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputTaskDTO1))
        );

        ResultActions response2 = mockMvcController.perform(
                post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputTaskDTO2))
        );

        ResultActions response3 = mockMvcController.perform(
                post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputTaskDTO3))
        );

        ResultActions response4 = mockMvcController.perform(
                post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputTaskDTO4))
        );

        ResultActions response5 = mockMvcController.perform(
                post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputTaskDTO5))
        );

        ResultActions response6 = mockMvcController.perform(
                post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputTaskDTO6))
        );

        //Assert
        assertAll(
                () -> response1.andExpect(status().isUnprocessableEntity()),
                () -> response2.andExpect(status().isUnprocessableEntity()),
                () -> response3.andExpect(status().isUnprocessableEntity()),
                () -> response4.andExpect(status().isUnprocessableEntity()),
                () -> response5.andExpect(status().isUnprocessableEntity()),
                () -> response6.andExpect(status().isUnprocessableEntity()));
    }

    @Test
    @DisplayName("postTask returns Unprocessable (422) when passed TaskDTO with empty String fields")
    void postTaskDTOEmptyStringFields() throws Exception {
        //Arrange
        TaskDTO inputTaskDTO1 = new TaskDTO(
                "",
                "test description",
                "In progress",
                LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1),
                LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2));

        TaskDTO inputTaskDTO2 = new TaskDTO(
                "test title",
                "",
                "In progress",
                LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1),
                LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2));

        TaskDTO inputTaskDTO3 = new TaskDTO(
                "test title",
                "test description",
                "",
                LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1),
                LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2));

        TaskDTO inputTaskDTO4 = new TaskDTO(
                "",
                "",
                "",
                LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1),
                LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2));


        when(taskService.createTask(inputTaskDTO1)).thenThrow(InvalidDTOException.class);
        when(taskService.createTask(inputTaskDTO2)).thenThrow(InvalidDTOException.class);
        when(taskService.createTask(inputTaskDTO3)).thenThrow(InvalidDTOException.class);
        when(taskService.createTask(inputTaskDTO4)).thenThrow(InvalidDTOException.class);

        //Act
        ResultActions response1 = mockMvcController.perform(
                post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputTaskDTO1))
        );

        ResultActions response2 = mockMvcController.perform(
                post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputTaskDTO2))
        );

        ResultActions response3 = mockMvcController.perform(
                post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputTaskDTO3))
        );

        ResultActions response4 = mockMvcController.perform(
                post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(inputTaskDTO4))
        );

        //Assert
        assertAll(
                () -> response1.andExpect(status().isUnprocessableEntity()),
                () -> response2.andExpect(status().isUnprocessableEntity()),
                () -> response3.andExpect(status().isUnprocessableEntity()),
                () -> response4.andExpect(status().isUnprocessableEntity()));
    }

    @Test
    @DisplayName("getTaskById returns OK (200) and correct Task when passed valid Id")
    void getTaskById() throws Exception {
        //Arrange
        Task expectedTask = Task.builder()
                .id(1L)
                .title("test title")
                .description("test description")
                .status("In progress")
                .createdDate(LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1))
                .dueDate(LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2))
                .build();

        when(taskService.retrieveTaskById(1L)).thenReturn(expectedTask);

        //Act
        ResultActions response = mockMvcController.perform(get("/api/v1/tasks/1"));

        //Assert
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedTask.getId()))
                .andExpect(jsonPath("$.title").value(expectedTask.getTitle()))
                .andExpect(jsonPath("$.description").value(expectedTask.getDescription()))
                .andExpect(jsonPath("$.status").value(expectedTask.getStatus()))
                .andExpect(jsonPath("$.createdDate").value(expectedTask.getCreatedDate()))
                .andExpect(jsonPath("$.dueDate").value(expectedTask.getDueDate()));
    }

    @Test
    @DisplayName("getTaskById returns NOT_FOUND (404) when passed id which is not in database")
    void getTaskByIdDoesNotExist() throws Exception {
        //Arrange
        when(taskService.retrieveTaskById(1L)).thenThrow(TaskNotFoundException.class);

        //Act
        ResultActions response = mockMvcController.perform(get("/api/v1/tasks/1"));

        //Assert
        response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getAllTasks returns OK (200) and correct list when one Task in database ")
    void getAllTasksOne() throws Exception {
        //Arrange
        List<Task> expectedTasks = List.of(
                Task.builder()
                        .id(1L)
                        .title("test title")
                        .description("test description")
                        .status("In progress")
                        .createdDate(LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1))
                        .dueDate(LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2))
                        .build());

        when(taskService.retrieveAllTasks()).thenReturn(expectedTasks);

        //Act
        ResultActions response = mockMvcController.perform(get("/api/v1/tasks"));

        //Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedTasks.get(0).getId()))
                .andExpect(jsonPath("$[0].title").value(expectedTasks.get(0).getTitle()))
                .andExpect(jsonPath("$[0].description").value(expectedTasks.get(0).getDescription()))
                .andExpect(jsonPath("$[0].status").value(expectedTasks.get(0).getStatus()))
                .andExpect(jsonPath("$[0].createdDate").value(expectedTasks.get(0).getCreatedDate()))
                .andExpect(jsonPath("$[0].dueDate").value(expectedTasks.get(0).getDueDate()));
    }

    @Test
    @DisplayName("getAllTasks returns OK (200) and correct list when multiple Tasks in database ")
    void getAllTasksMultiple() throws Exception {
        //Arrange
        List<Task> expectedTasks = new ArrayList<>();

        expectedTasks = List.of(
                Task.builder()
                        .id(1L)
                        .title("test title1")
                        .description("test description1")
                        .status("In progress")
                        .createdDate(LocalDateTime.of(2021, Month.JANUARY, 1, 1, 1, 1))
                        .dueDate(LocalDateTime.of(2021, Month.FEBRUARY, 2, 2, 2, 2))
                        .build(),
                Task.builder()
                        .id(2L)
                        .title("test title2")
                        .description("test description2")
                        .status("In progress")
                        .createdDate(LocalDateTime.of(2022, Month.JANUARY, 1, 1, 1, 1))
                        .dueDate(LocalDateTime.of(2022, Month.FEBRUARY, 2, 2, 2, 2))
                        .build(),
                Task.builder()
                        .id(3L)
                        .title("test title3")
                        .description("test description3")
                        .status("In progress")
                        .createdDate(LocalDateTime.of(2023, Month.JANUARY, 1, 1, 1, 1))
                        .dueDate(LocalDateTime.of(2023, Month.FEBRUARY, 2, 2, 2, 2))
                        .build());

        when(taskService.retrieveAllTasks()).thenReturn(expectedTasks);

        //Act
        ResultActions response = mockMvcController.perform(get("/api/v1/tasks"));

        //Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedTasks.get(0).getId()))
                .andExpect(jsonPath("$[0].title").value(expectedTasks.get(0).getTitle()))
                .andExpect(jsonPath("$[0].description").value(expectedTasks.get(0).getDescription()))
                .andExpect(jsonPath("$[0].status").value(expectedTasks.get(0).getStatus()))
                .andExpect(jsonPath("$[0].createdDate").value(expectedTasks.get(0).getCreatedDate()))
                .andExpect(jsonPath("$[0].dueDate").value(expectedTasks.get(0).getDueDate()))

                .andExpect(jsonPath("$[1].id").value(expectedTasks.get(1).getId()))
                .andExpect(jsonPath("$[1].title").value(expectedTasks.get(1).getTitle()))
                .andExpect(jsonPath("$[1].description").value(expectedTasks.get(1).getDescription()))
                .andExpect(jsonPath("$[1].status").value(expectedTasks.get(1).getStatus()))
                .andExpect(jsonPath("$[1].createdDate").value(expectedTasks.get(1).getCreatedDate()))
                .andExpect(jsonPath("$[1].dueDate").value(expectedTasks.get(1).getDueDate()))

                .andExpect(jsonPath("$[2].id").value(expectedTasks.get(2).getId()))
                .andExpect(jsonPath("$[2].title").value(expectedTasks.get(2).getTitle()))
                .andExpect(jsonPath("$[2].description").value(expectedTasks.get(2).getDescription()))
                .andExpect(jsonPath("$[2].status").value(expectedTasks.get(2).getStatus()))
                .andExpect(jsonPath("$[2].createdDate").value(expectedTasks.get(2).getCreatedDate()))
                .andExpect(jsonPath("$[2].dueDate").value(expectedTasks.get(2).getDueDate()));
    }

    @Test
    @DisplayName("getAllTasks returns OK (200) and correct list when no Tasks in database ")
    void getAllTasksNone() throws Exception {
        //Arrange
        List<Task> expectedTasks = new ArrayList<>();

        when(taskService.retrieveAllTasks()).thenReturn(expectedTasks);

        //Act
        ResultActions response = mockMvcController.perform(get("/api/v1/tasks"));

        //Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].title").doesNotExist())
                .andExpect(jsonPath("$[0].description").doesNotExist())
                .andExpect(jsonPath("$[0].status").doesNotExist())
                .andExpect(jsonPath("$[0].createdDate").doesNotExist())
                .andExpect(jsonPath("$[0].dueDate").doesNotExist());
    }

    @Test
    @DisplayName("patchTaskStatus returns OK and correct updated Task when passed id which exists and valid status")
    void patchTaskStatusValidIdAndStatus() throws Exception {
        Task expectedTask1 = Task.builder()
                .id(1L)
                .title("test title")
                .description("test description")
                .status("Not yet started")
                .createdDate(LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1))
                .dueDate(LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2))
                .build();

        Task expectedTask2= Task.builder()
                .id(2L)
                .title("test title")
                .description("test description")
                .status("In progress")
                .createdDate(LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1))
                .dueDate(LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2))
                .build();

        Task expectedTask3 = Task.builder()
                .id(3L)
                .title("test title")
                .description("test description")
                .status("Complete")
                .createdDate(LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1))
                .dueDate(LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2))
                .build();

        when(taskService.updateTaskStatus(1L, "Not yet started")).thenReturn(expectedTask1);
        when(taskService.updateTaskStatus(2L, "In progress")).thenReturn(expectedTask2);
        when(taskService.updateTaskStatus(3L, "Complete")).thenReturn(expectedTask3);

        //Act
        ResultActions response1 = mockMvcController.perform(patch("/api/v1/tasks/1")                        .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString("Not yet started")));
        ResultActions response2 = mockMvcController.perform(patch("/api/v1/tasks/2")                        .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString("In progress")));
        ResultActions response3 = mockMvcController.perform(patch("/api/v1/tasks/3")                        .contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString("Complete")));

        //Assert
        assertAll(
                () -> response1.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedTask1.getId()))
                .andExpect(jsonPath("$.title").value(expectedTask1.getTitle()))
                .andExpect(jsonPath("$.description").value(expectedTask1.getDescription()))
                .andExpect(jsonPath("$.status").value(expectedTask1.getStatus()))
                .andExpect(jsonPath("$.createdDate").value(expectedTask1.getCreatedDate()))
                .andExpect(jsonPath("$.dueDate").value(expectedTask1.getDueDate())),

                () -> response2.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedTask2.getId()))
                .andExpect(jsonPath("$.title").value(expectedTask2.getTitle()))
                .andExpect(jsonPath("$.description").value(expectedTask2.getDescription()))
                .andExpect(jsonPath("$.status").value(expectedTask2.getStatus()))
                .andExpect(jsonPath("$.createdDate").value(expectedTask2.getCreatedDate()))
                .andExpect(jsonPath("$.dueDate").value(expectedTask2.getDueDate())),

                () -> response3.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedTask3.getId()))
                .andExpect(jsonPath("$.title").value(expectedTask3.getTitle()))
                .andExpect(jsonPath("$.description").value(expectedTask3.getDescription()))
                .andExpect(jsonPath("$.status").value(expectedTask3.getStatus()))
                .andExpect(jsonPath("$.createdDate").value(expectedTask3.getCreatedDate()))
                .andExpect(jsonPath("$.dueDate").value(expectedTask3.getDueDate())));
    }

    @Test
    @DisplayName("patchTaskStatus returns UNPROCESSABLE (422) when passed invalid status")
    void patchTaskStatusInvalidStatus() throws Exception {
        //Arrange
        String status1 = "";
        String status2 = "hello";
        String status3 = "1234";
        String status4 = "NotStartedYet";
        String status5 = "Innn progress";
        String status6 = "Complete?!";

        when(taskService.updateTaskStatus(1L, status1)).thenThrow(InvalidDTOException.class);
        when(taskService.updateTaskStatus(1L, status2)).thenThrow(InvalidDTOException.class);
        when(taskService.updateTaskStatus(1L, status3)).thenThrow(InvalidDTOException.class);
        when(taskService.updateTaskStatus(1L, status4)).thenThrow(InvalidDTOException.class);
        when(taskService.updateTaskStatus(1L, status5)).thenThrow(InvalidDTOException.class);
        when(taskService.updateTaskStatus(1L, status6)).thenThrow(InvalidDTOException.class);

        //Act
        ResultActions response1 = mockMvcController.perform(patch("/api/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(status1));

        ResultActions response2 = mockMvcController.perform(patch("/api/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(status2));

        ResultActions response3 = mockMvcController.perform(patch("/api/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(status3));

        ResultActions response4 = mockMvcController.perform(patch("/api/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(status4));

        ResultActions response5 = mockMvcController.perform(patch("/api/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(status5));

        ResultActions response6 = mockMvcController.perform(patch("/api/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(status6));

        //Assert
        assertAll(
                () -> response1.andExpect(status().isUnprocessableEntity()),
                () -> response2.andExpect(status().isUnprocessableEntity()),
                () -> response3.andExpect(status().isUnprocessableEntity()),
                () -> response4.andExpect(status().isUnprocessableEntity()),
                () -> response5.andExpect(status().isUnprocessableEntity()),
                () -> response6.andExpect(status().isUnprocessableEntity()));
    }

    @Test
    @DisplayName("patchTaskStatus returns NOT_FOUND (404) when passed id which is not in database")
    void patchTaskStatusIdDoesNotExist() throws Exception {
        //Arrange
        String status = "Complete";
        when(taskService.updateTaskStatus(1L, status)).thenThrow(TaskNotFoundException.class);

        //Act
        ResultActions response = mockMvcController.perform(patch("/api/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(status));

        //Assert
        response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deleteTask returns NO_CONTENT (204) when passed valid Id")
    void deleteTaskValidId() throws Exception {
        //Arrange
        doNothing().when(taskService).deleteTask(1L);

        //Act
        ResultActions response = mockMvcController.perform(delete("/api/vi/tasks/1"));

        //Assert
        response.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("deleteTask returns NOT_FOUND (404) when passed ID not found in database")
    void deleteTaskIDNotFound() throws Exception {
        //Arrange
        doThrow(TaskNotFoundException.class).when(taskService).deleteTask(1L);

        //Act
        ResultActions response = mockMvcController.perform(delete("/api/vi/tasks/1"));

        //Assert
        response.andExpect(status().isNotFound());
    }
}