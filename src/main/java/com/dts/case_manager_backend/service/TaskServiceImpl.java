package com.dts.case_manager_backend.service;

import com.dts.case_manager_backend.exception.InvalidDTOException;
import com.dts.case_manager_backend.exception.TaskNotFoundException;
import com.dts.case_manager_backend.model.Task;
import com.dts.case_manager_backend.model.TaskDTO;
import com.dts.case_manager_backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Override
    public Task createTask(TaskDTO taskDTO) {

        if (containsNullFields(taskDTO)) {
            throw new InvalidDTOException("Task could not be created because supplied tasks cannot have null fields.");
        }

        if (containsEmptyFields(taskDTO)) {
            throw new InvalidDTOException("Task could not be created because supplied tasks cannot have empty string fields.");
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
    public Task updateTaskStatus(Long id, String status) {
        return null;
    }

    @Override
    public void deleteTask(Long id) {

    }

    public Task taskDTOToTask(TaskDTO taskDTO) {
        return Task.builder()
                .title(taskDTO.title())
                .description(taskDTO.description())
                .status(taskDTO.status())
                .createdDate(taskDTO.createdDate())
                .dueDate(taskDTO.dueDate())
                .build();
    }

    public boolean containsNullFields(TaskDTO taskDTO) {
        try {
            return taskDTO.title() == null ||
                    taskDTO.description() == null ||
                    taskDTO.status() == null ||
                    taskDTO.createdDate() == null ||
                    taskDTO.dueDate() == null;}
        catch (NullPointerException e) {
            return true;
        }
    }

    public boolean containsEmptyFields(TaskDTO taskDTO) {
        return taskDTO.title().isEmpty() ||
                taskDTO.description().isEmpty() ||
                taskDTO.status().isEmpty();
    }


}
