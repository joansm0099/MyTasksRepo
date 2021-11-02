package com.nttdata.everisdarmytasksms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nttdata.everisdarmytasksms.model.Task;

public interface TaskRepository extends JpaRepository<Task, Integer>, TaskRepositoryCustom {

}
