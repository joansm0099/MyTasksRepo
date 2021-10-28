package com.nttdata.everisdarmytasksms.controllers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="list_tasks")
public class Task {

	@Id
	@Column(name="id")
	private int id;

	@Column(name="title")
	private String title;

	@Column(name="description")
	private String description;

	@Column(name="status")
	private String status;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public boolean statusIsValid() {
		return (status.equals("Pending") || status.equals("In progress") || status.equals("Completed"));
	}

}
