package com.dts.case_manager_backend.service;

import com.dts.case_manager_backend.model.StatusDTO;
import com.dts.case_manager_backend.model.Task;
import com.dts.case_manager_backend.model.TaskDTO;

import java.util.List;

public interface TaskService {
    Task createTask(TaskDTO taskDTO);
    Task retrieveTaskById(Long id);
    List<Task> retrieveAllTasks();
    Task updateTaskStatus(Long id, StatusDTO statusDTO);
    void deleteTask(Long id);
}
