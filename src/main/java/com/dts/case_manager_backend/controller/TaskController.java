package com.dts.case_manager_backend.controller;

import com.dts.case_manager_backend.model.Task;
import com.dts.case_manager_backend.model.TaskDTO;
import com.dts.case_manager_backend.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> postTask(@RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<Task>(taskService.createTask(taskDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable long id) {
        return new ResponseEntity<>(taskService.retrieveTaskById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(taskService.retrieveAllTasks(), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> patchTaskStatus(@PathVariable long id, @RequestBody String status) {
        return new ResponseEntity<>(taskService.updateTaskStatus(id, status), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
        return HttpStatus.NO_CONTENT;
    }
}
