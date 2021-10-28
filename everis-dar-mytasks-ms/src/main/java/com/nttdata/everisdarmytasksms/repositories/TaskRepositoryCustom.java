package com.nttdata.everisdarmytasksms.repositories;

import java.util.List;

import com.nttdata.everisdarmytasksms.controllers.Task;

public interface TaskRepositoryCustom {

	List<Task> findAllByStatus(String status);

}
