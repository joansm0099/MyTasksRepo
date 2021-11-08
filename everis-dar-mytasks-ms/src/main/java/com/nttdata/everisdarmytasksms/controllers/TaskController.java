package com.nttdata.everisdarmytasksms.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nttdata.everisdarmytasksms.model.Status;
import com.nttdata.everisdarmytasksms.model.Task;
import com.nttdata.everisdarmytasksms.repositories.TaskRepository;

@RestController
@CrossOrigin(origins = "*")
public class TaskController {

	private AtomicLong counter = new AtomicLong();

	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	
	@Autowired
	private TaskRepository repository;

	@PostMapping("/tasks")
	@ResponseStatus(HttpStatus.CREATED)
	public AddResponse createTask(@RequestBody Task task) {
		if (task.getDescription().length() > 256) {
			logger.warn("Description size > 256 characters");
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Description must have a maximum size of 256 characters");
		}
		
		task.setId((int) counter.incrementAndGet());
		repository.save(task);
		
		AddResponse resp = new AddResponse();
		resp.setMessage("Task added succesfully");
		resp.setId(task.getId());
		return resp;
	}

	@GetMapping("/tasks")
	public List<Task> getAllTasks() {
		return repository.findAll();
	}

	@GetMapping("/tasks/{id}")
	public Task getTaskById(@PathVariable(value="id") int id) {
		Task task = repository.findById(id).get();
		return task;
	}

	@GetMapping("/tasks/searchByStatus")
	public List<Task> getTasksByStatus(@RequestParam(value="status") String statusStr) {
		try {
			Status status = (Status.valueOf(statusStr));
			return repository.findAllByStatus(status);
		} catch (IllegalArgumentException e) {
			logger.warn("Searching for an incorrect status");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status must be PENDING, IN_PROGRESS or COMPLETED");
		}
	}

	@PutMapping("/tasks/{id}")
	public Task updateTask(@PathVariable(value="id") int id, @RequestBody Task task) {
		Task existingTask = repository.findById(id).get();
		
		if (task.getDescription().length() > 256) {
			logger.warn("Description size > 256 characters");
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Description must have a maximum size of 256 characters");
		}
		existingTask.setTitle(task.getTitle());
		existingTask.setDescription(task.getDescription());
		existingTask.setStatus(task.getStatus());
		repository.save(existingTask);
		return existingTask;
	}

	@DeleteMapping("/tasks/{id}")
	public MsgResponse deleteTask(@PathVariable(value="id") int id) {
		repository.deleteById(id);

		MsgResponse resp = new MsgResponse();
		resp.setMessage("Task deleted");
		return resp;
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public MsgResponse taskNotFoundException(NoSuchElementException e) {
		logger.warn("Trying to get a task that does not exist");
		MsgResponse resp = new MsgResponse();
		resp.setMessage("Task not found");
		return resp;
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public MsgResponse taskToDeleteNotFoundException(EmptyResultDataAccessException e) {
		logger.warn("Trying to delete a task that does not exist");
		MsgResponse resp = new MsgResponse();
		resp.setMessage("Task not found");
		return resp;
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MsgResponse JSONParseException(HttpMessageNotReadableException e) {
		logger.warn("Invalid task format");
		MsgResponse resp = new MsgResponse();
		resp.setMessage("Allowed fields are title, description and status. Status must be PENDING, IN_PROGRESS or COMPLETED.");
		return resp;
	}

}
