package com.dts.case_manager_backend.service;

import com.dts.case_manager_backend.exception.InvalidDTOException;
import com.dts.case_manager_backend.exception.TaskNotFoundException;
import com.dts.case_manager_backend.model.Task;
import com.dts.case_manager_backend.model.TaskDTO;
import com.dts.case_manager_backend.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@DataJpaTest
class TaskServiceImplTest {

    @Mock
    private TaskRepository mockTaskRepository;

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    @Test
    @DisplayName("createTask returns created task when passed valid TaskDTO")
    void createTaskValidDTO() {
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

        when(taskServiceImpl.taskDTOToTask(Mockito.any(TaskDTO.class))).thenReturn(expectedTask);
        when(mockTaskRepository.save(expectedTask)).thenReturn(expectedTask);

        //Act
        Task returnedTask = taskServiceImpl.createTask(inputTaskDTO);

        //Assert
        assertAll(
                () -> assertEquals(1L, returnedTask.getId()),
                () -> assertEquals(inputTaskDTO.title(), returnedTask.getTitle()),
                () -> assertEquals(inputTaskDTO.description(), returnedTask.getDescription()),
                () -> assertEquals(inputTaskDTO.status(), returnedTask.getStatus()),
                () -> assertEquals(inputTaskDTO.createdDate(), returnedTask.getCreatedDate()),
                () -> assertEquals(inputTaskDTO.dueDate(), returnedTask.getDueDate()));
    }

    @Test
    @DisplayName("createTask throws InvalidDTOException when passed TaskDTO with null fields")
    void createTaskInvalidDTONull() {
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

        //Act & Assert
        assertAll(
                () -> assertThrows(InvalidDTOException.class, () -> taskServiceImpl.createTask(inputTaskDTO1)),
                () -> assertThrows(InvalidDTOException.class, () -> taskServiceImpl.createTask(inputTaskDTO2)),
                () -> assertThrows(InvalidDTOException.class, () -> taskServiceImpl.createTask(inputTaskDTO3)),
                () -> assertThrows(InvalidDTOException.class, () -> taskServiceImpl.createTask(inputTaskDTO4)),
                () -> assertThrows(InvalidDTOException.class, () -> taskServiceImpl.createTask(inputTaskDTO5)),
                () -> assertThrows(InvalidDTOException.class, () -> taskServiceImpl.createTask(inputTaskDTO6)));
    }

    @Test
    @DisplayName("createTask throws InvalidDTOException when passed TaskDTO with empty String fields")
    void createTaskInvalidDTOEmptyStrings() {
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


        //Act & Assert
        assertAll(
                () -> assertThrows(InvalidDTOException.class, () -> taskServiceImpl.createTask(inputTaskDTO1)),
                () -> assertThrows(InvalidDTOException.class, () -> taskServiceImpl.createTask(inputTaskDTO2)),
                () -> assertThrows(InvalidDTOException.class, () -> taskServiceImpl.createTask(inputTaskDTO3)),
                () -> assertThrows(InvalidDTOException.class, () -> taskServiceImpl.createTask(inputTaskDTO4)));
    }

    @Test
    @DisplayName("retrieveTaskById returns correct task when passed Id")
    void retrieveTaskByIdValidId() {
        //Arrange
        Task expectedTask = Task.builder()
                .id(1L)
                .title("test title")
                .description("test description")
                .status("In progress")
                .createdDate(LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1))
                .dueDate(LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2))
                .build();

        when(mockTaskRepository.findById(1L)).thenReturn(Optional.of(expectedTask));

        //Act
        Task returnedTask = taskServiceImpl.retrieveTaskById(1L);

        //Assert
        assertAll(
                () -> assertEquals(1L, returnedTask.getId()),
                () -> assertEquals(expectedTask.getTitle(), returnedTask.getTitle()),
                () -> assertEquals(expectedTask.getDescription(), returnedTask.getDescription()),
                () -> assertEquals(expectedTask.getStatus(), returnedTask.getStatus()),
                () -> assertEquals(expectedTask.getCreatedDate(), returnedTask.getCreatedDate()),
                () -> assertEquals(expectedTask.getDueDate(), returnedTask.getDueDate()));
    }

    @Test
    @DisplayName("retrieveTaskById throws TaskNotFoundException when input id is not found in database")
    void retrieveTaskByIdNotFound() {
        //Arrange
        when(mockTaskRepository.findById(1L)).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.retrieveTaskById(1L));
    }

    @Test
    @DisplayName("retrieveAllTasks returns correct list of tasks when one task in database")
    void retrieveAllTasksOne() {
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

        when (mockTaskRepository.findAll()).thenReturn(expectedTasks);

        //Act
        List<Task> returnedTasks = taskServiceImpl.retrieveAllTasks();

        //Assert
        assertThat(returnedTasks).hasSize(1);
        assertThat(returnedTasks).isEqualTo(expectedTasks);
    }

    @Test
    @DisplayName("retrieveAllTasks returns correct list of tasks when multiple tasks in database")
    void retrieveAllTasksMultiple() {
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

        when(mockTaskRepository.findAll()).thenReturn(expectedTasks);

        //Act
        List<Task> returnedTasks = taskServiceImpl.retrieveAllTasks();

        //Assert
        assertThat(returnedTasks).hasSize(3);
        assertThat(returnedTasks).isEqualTo(expectedTasks);
    }

    @Test
    @DisplayName("retrieveAllTasks returns empty list of tasks when no task in database")
    void retrieveAllTasksNone() {
        //Arrange
        List<Task> expectedTasks = new ArrayList<>();

        when(mockTaskRepository.findAll()).thenReturn(expectedTasks);

        //Act
        List<Task> returnedTasks = taskServiceImpl.retrieveAllTasks();

        //Assert
        assertThat(returnedTasks).hasSize(0);
        assertThat(returnedTasks).isEqualTo(expectedTasks);
    }

    @Test
    @DisplayName("updateTaskStatus returns updated task when passed id which exists, and valid status")
    void updateTaskStatusValidIdAndStatus() {
        //Arrange
        Task expectedTask = Task.builder()
            .id(1L)
            .title("test title")
            .description("test description")
            .status("Not yet started")
            .createdDate(LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1))
            .dueDate(LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2))
            .build();

        when(mockTaskRepository.findById(1L)).thenReturn(Optional.of(expectedTask));

        //Act
        Task returnedTask1 = taskServiceImpl.updateTaskStatus(1L, "Not yet started");
        Task returnedTask2 = taskServiceImpl.updateTaskStatus(1L, "In progress");
        Task returnedTask3 = taskServiceImpl.updateTaskStatus(1L, "Complete");

        //Assert
        assertAll(
                () -> assertEquals(expectedTask.getId(), returnedTask1.getId()),
                () -> assertEquals(expectedTask.getId(), returnedTask2.getId()),
                () -> assertEquals(expectedTask.getId(), returnedTask3.getId()),

                () -> assertEquals(expectedTask.getTitle(), returnedTask1.getTitle()),
                () -> assertEquals(expectedTask.getTitle(), returnedTask2.getTitle()),
                () -> assertEquals(expectedTask.getTitle(), returnedTask3.getTitle()),

                () -> assertEquals(expectedTask.getDescription(), returnedTask1.getDescription()),
                () -> assertEquals(expectedTask.getDescription(), returnedTask2.getDescription()),
                () -> assertEquals(expectedTask.getDescription(), returnedTask3.getDescription()),

                () -> assertEquals(expectedTask.getCreatedDate(), returnedTask1.getCreatedDate()),
                () -> assertEquals(expectedTask.getCreatedDate(), returnedTask2.getCreatedDate()),
                () -> assertEquals(expectedTask.getCreatedDate(), returnedTask3.getCreatedDate()),

                () -> assertEquals(expectedTask.getDueDate(), returnedTask1.getDueDate()),
                () -> assertEquals(expectedTask.getDueDate(), returnedTask2.getDueDate()),
                () -> assertEquals(expectedTask.getDueDate(), returnedTask3.getDueDate()),

                () -> assertEquals("Not yet started", returnedTask1.getStatus()),
                () -> assertEquals("In progress", returnedTask1.getStatus()),
                () -> assertEquals("Complete", returnedTask1.getStatus()));
    }

    @Test
    @DisplayName("updateTaskStatus throws TaskNotFoundException when passed invalid status")
    void updateTaskStatusInvalidStatus() {
        //Arrange
        Task taskToEdit = Task.builder()
                .id(1L)
                .title("test title")
                .description("test description")
                .status("Not yet started")
                .createdDate(LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1))
                .dueDate(LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2))
                .build();

        when(mockTaskRepository.findById(taskToEdit.getId())).thenReturn(Optional.of(taskToEdit));

        //Act & Assert
        assertThrows(InvalidDTOException.class, () -> taskServiceImpl.updateTaskStatus(1L, ""));
        assertThrows(InvalidDTOException.class, () -> taskServiceImpl.updateTaskStatus(1L, "hello"));
        assertThrows(InvalidDTOException.class, () -> taskServiceImpl.updateTaskStatus(1L, "1234"));
        assertThrows(InvalidDTOException.class, () -> taskServiceImpl.updateTaskStatus(1L, "NotYetStarted"));
        assertThrows(InvalidDTOException.class, () -> taskServiceImpl.updateTaskStatus(1L, "Innnnn progress"));
        assertThrows(InvalidDTOException.class, () -> taskServiceImpl.updateTaskStatus(1L, "Complete!?"));
    }

    @Test
    @DisplayName("updateTaskStatus throws TaskNotFoundException when passed id which does not exist in database")
    void updateTaskStatusIdDoesNotExist() {
        //Arrange
        Task taskToEdit = Task.builder()
                .id(1L)
                .title("test title")
                .description("test description")
                .status("Not yet started")
                .createdDate(LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1))
                .dueDate(LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2))
                .build();

        when(mockTaskRepository.findById(1L)).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.updateTaskStatus(1L, "Not yet started"));
    }

    @Test
    @DisplayName("deleteTask returns nothing when task is successfully deleted")
    void deleteTaskValidId() {
        //Arrange
        Task taskToDelete = Task.builder()
                .id(1L)
                .title("test title")
                .description("test description")
                .status("Not yet started")
                .createdDate(LocalDateTime.of(2025, Month.JANUARY, 1, 1, 1, 1))
                .dueDate(LocalDateTime.of(2025, Month.FEBRUARY, 2, 2, 2, 2))
                .build();

        when(mockTaskRepository.findById(1L)).thenReturn(Optional.of(taskToDelete));
        doNothing().when(mockTaskRepository).deleteById(1L);

        //Act & Assert
        assertDoesNotThrow(() -> taskServiceImpl.deleteTask(1L));
    }

    @Test
    @DisplayName("deleteTask throws TaskNotFoundException id does not exist in database")
    void deleteTaskIdDoesNotExist() {
        //Arrange
        when(mockTaskRepository.findById(1L)).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.deleteTask(1L));
    }
}