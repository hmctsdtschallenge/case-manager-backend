package com.dts.case_manager_backend.service;

import com.dts.case_manager_backend.model.Task;
import com.dts.case_manager_backend.model.TaskDTO;
import com.dts.case_manager_backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Override
    public Task createTask(TaskDTO taskDTO) {
        return null;
    }

    @Override
    public Task retrieveTaskById(Long id) {
        return null;
    }

    @Override
    public List<Task> retrieveAllTasks() {
        return List.of();
    }

    @Override
    public Task updateTaskStatus(Long id) {
        return null;
    }

    @Override
    public void deleteTask(Long id) {

    }
}
