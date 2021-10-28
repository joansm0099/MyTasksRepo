package com.nttdata.everisdarmytasksms.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.nttdata.everisdarmytasksms.controllers.Task;

public class TaskRepositoryImpl implements TaskRepositoryCustom {

	@Autowired
	TaskRepository repository;

	@Override
	public List<Task> findAllByStatus(String status) {
		List<Task> tasksFound = new ArrayList<Task>();
		List<Task> tasks = repository.findAll();
		for (Task t : tasks) {
			if (t.getStatus().equalsIgnoreCase(status)) {
				tasksFound.add(t);
			}
		}
		return tasksFound;
	}

}
