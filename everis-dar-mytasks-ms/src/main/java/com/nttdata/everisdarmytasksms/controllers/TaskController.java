package com.nttdata.everisdarmytasksms.controllers;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

	@GetMapping("/tasks/{id}")
	public Task getTaskById(@PathVariable(value="id") int id) {
		try {
			Task task = repository.findById(id).get();
			return task;
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
		}
	}

}