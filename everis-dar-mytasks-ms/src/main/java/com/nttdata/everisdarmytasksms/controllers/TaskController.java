package com.nttdata.everisdarmytasksms.controllers;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping("/tasks/searchByStatus")
	public List<Task> getTasksByStatus(@RequestParam(value="status") String status) {
		return repository.findAllByStatus(status);
	}

	@PutMapping("/tasks/{id}")
	public Task updateTask(@PathVariable(value="id") int id, @RequestBody Task task) {
		try {
			Task existingTask = repository.findById(id).get();
			existingTask.setTitle(task.getTitle());
			existingTask.setDescription(task.getDescription());
			existingTask.setStatus(task.getStatus());
			repository.save(existingTask);
			return existingTask;
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
		}
	}

}
