package com.dts.case_manager_backend.service;

import com.dts.case_manager_backend.exception.InvalidDTOException;
import com.dts.case_manager_backend.exception.TaskNotFoundException;
import com.dts.case_manager_backend.model.StatusDTO;
import com.dts.case_manager_backend.model.Task;
import com.dts.case_manager_backend.model.TaskDTO;
import com.dts.case_manager_backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;

    private List<String> validStatuses = List.of("Not yet started", "In progress", "Complete");

    @Override
    public Task createTask(TaskDTO taskDTO) {

        if (containsNullFields(taskDTO)) {
            throw new InvalidDTOException("Task could not be created because a mandatory field was not supplied.");
        }

        if (containsEmptyFields(taskDTO)) {
            throw new InvalidDTOException("Task could not be created because mandatory fields cannot be empty.");
        }

        if (!isValidStatus(taskDTO.status())) {
            throw new InvalidDTOException("Task cannot be updated because supplied status is not valid.  Valid statuses are: ".concat(validStatuses.toString()));
        }

        return taskRepository.save(taskDTOToTask(taskDTO));
    }

    @Override
    public Task retrieveTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow( () -> new TaskNotFoundException("Task cannot be retrieved because no task could be found with the supplied id"));
    }

    @Override
    public List<Task> retrieveAllTasks() {

        List<Task> tasks = new ArrayList<>();
        taskRepository.findAll().forEach(tasks::add);

        return tasks;
    }

    @Override
    public Task updateTaskStatus(Long id, StatusDTO statusDTO) {
        Task taskToUpdate = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task cannot be updated because no task could be found with the supplied id"));

        String status = statusDTO.status();

        if (!isValidStatus(status)) {
            throw new InvalidDTOException("Task cannot be updated because supplied status is not valid.  Valid statuses are: ".concat(validStatuses.toString()));
        }

        taskToUpdate.setStatus(status);

        return taskRepository.save(taskToUpdate);
    }

    @Override
    public void deleteTask(Long id) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new TaskNotFoundException("Task could not be deleted because no task could be found with the supplied id");
        }
        taskRepository.deleteById(id);
    }

    public Task taskDTOToTask(TaskDTO taskDTO) {
        return Task.builder()
                .title(taskDTO.title())
                .description(Objects.requireNonNullElse(taskDTO.description(), ""))
                .status(taskDTO.status())
                .createdDate(taskDTO.createdDate())
                .dueDate(taskDTO.dueDate())
                .build();
    }

    private boolean containsNullFields(TaskDTO taskDTO) {
        try {
            return taskDTO.title() == null ||
                    taskDTO.status() == null ||
                    taskDTO.createdDate() == null ||
                    taskDTO.dueDate() == null;}
        catch (NullPointerException e) {
            return true;
        }
    }

    private boolean containsEmptyFields(TaskDTO taskDTO) {
        return taskDTO.title().isEmpty() ||
                taskDTO.status().isEmpty();
    }

    private boolean isValidStatus(String status) {
        return validStatuses.contains(status);
    }
}
