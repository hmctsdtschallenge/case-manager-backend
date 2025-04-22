package com.dts.case_manager_backend.repository;

import com.dts.case_manager_backend.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
