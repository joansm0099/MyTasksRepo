package com.nttdata.everisdarmytasksms.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.nttdata.everisdarmytasksms.model.Status;
import com.nttdata.everisdarmytasksms.model.Task;

public class TaskRepositoryImpl implements TaskRepositoryCustom {

	@Autowired
	private TaskRepository repository;

	@Override
	public List<Task> findAllByStatus(Status status) {
		List<Task> tasksFound = new ArrayList<Task>();
		List<Task> tasks = repository.findAll();
		for (Task t : tasks) {
			if (t.getStatus().equals(status)) {
				tasksFound.add(t);
			}
		}
		return tasksFound;
	}

}
