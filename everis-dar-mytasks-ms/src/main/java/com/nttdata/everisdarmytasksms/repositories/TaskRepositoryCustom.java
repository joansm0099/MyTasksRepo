package com.nttdata.everisdarmytasksms.repositories;

import java.util.List;

import com.nttdata.everisdarmytasksms.model.Status;
import com.nttdata.everisdarmytasksms.model.Task;

public interface TaskRepositoryCustom {

	List<Task> findAllByStatus(Status status);

}
