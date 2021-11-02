package com.nttdata.everisdarmytasksms;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.everisdarmytasksms.controllers.AddResponse;
import com.nttdata.everisdarmytasksms.controllers.TaskController;
import com.nttdata.everisdarmytasksms.model.Status;
import com.nttdata.everisdarmytasksms.model.Task;
import com.nttdata.everisdarmytasksms.repositories.TaskRepository;

@SpringBootTest
@AutoConfigureMockMvc
class EverisDarMytasksMsApplicationTests {

	@Autowired
	private TaskController controller;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskRepository repository;

	@Test
	void contextLoads() {
	}

	@Test
	public void testCreateTask() {
		Task t = buildTask();
		when(repository.save(any())).thenReturn(t);
		AddResponse response = controller.createTask(t);
		assertEquals("Task added succesfully", response.getMessage());
		assertEquals(t.getId(), response.getId());
	}
	
	@Test
	public void testCreateTaskMvc() throws Exception {
		Task t = buildTask();
		ObjectMapper map = new ObjectMapper();
		String json = map.writeValueAsString(t);
		when(repository.save(any())).thenReturn(t);
		
		this.mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").value(1)); //first task always has id=1
	}

	@Test
	public void testGetAllTasks() throws Exception {
		List<Task> allTasks = new ArrayList<Task>();
		allTasks.add(buildTask());
		when(repository.findAll()).thenReturn(allTasks);
		
		this.mockMvc.perform(get("/tasks"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(1))
		.andExpect(jsonPath("$.[0].id").value(allTasks.get(0).getId()));
	}

	@Test
	public void testGetTaskById() throws Exception {
		Task t = buildTask();
		when(repository.findById(t.getId())).thenReturn(Optional.of(t));
		
		this.mockMvc.perform(get("/tasks/"+t.getId()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(t.getId()))
		.andExpect(jsonPath("$.title").value(t.getTitle()));
	}

	@Test
	public void testGetTasksByStatus() throws Exception {
		List<Task> tasksFound = new ArrayList<Task>();
		tasksFound.add(buildTask());
		when(repository.findAllByStatus(any())).thenReturn(tasksFound);
		
		this.mockMvc.perform(get("/tasks/searchByStatus").param("status", Status.PENDING.toString()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(1))
		.andExpect(jsonPath("$.[0].id").value(tasksFound.get(0).getId()));
	}

	@Test
	public void testUpdateTask() throws Exception {
		Task t = buildTask();
		when(repository.findById(t.getId())).thenReturn(Optional.of(t));
		
		t.setStatus(Status.IN_PROGRESS);
		ObjectMapper map = new ObjectMapper();
		String json = map.writeValueAsString(t);
		
		this.mockMvc.perform(put("/tasks/"+t.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(t.getId()))
		.andExpect(jsonPath("$.status").value(Status.IN_PROGRESS.toString()));
	}

	@Test
	public void testDeleteTask() throws Exception {
		Task t = buildTask();
		doNothing().when(repository).deleteById(t.getId());
		
		this.mockMvc.perform(delete("/tasks/"+t.getId()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.message").value("Task deleted"));
	}

	//class builders
	public Task buildTask() {
		Task t = new Task();
		t.setId(3);
		t.setTitle("Task for unit testing");
		t.setDescription("Testing with JUnit");
		t.setStatus(Status.PENDING);
		return t;
	}

}
