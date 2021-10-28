package com.nttdata.everisdarmytasksms.controllers;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.everisdarmytasksms.repositories.TaskRepository;

@RestController
public class TaskController {

	private AtomicLong counter = new AtomicLong();
	
	@Autowired
	TaskRepository repository;

	@PostMapping("/tasks")
	public ResponseEntity<AddResponse> createTask(@RequestBody Task task) {
		task.setId((int) counter.incrementAndGet());
		repository.save(task);
		
		AddResponse resp = new AddResponse();
		resp.setMsg("Task is succesfully added");
		resp.setId(task.getId());
		return new ResponseEntity<AddResponse>(resp, HttpStatus.CREATED);
	}

}
