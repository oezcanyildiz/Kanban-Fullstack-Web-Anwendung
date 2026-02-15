package com.yildiz.teamsync.services;

import com.yildiz.teamsync.dto.TaskCreateRequestDTO;
import com.yildiz.teamsync.dto.TaskCreateResponseDTO;
import com.yildiz.teamsync.dto.TaskUpdateRequestDTO;
import com.yildiz.teamsync.dto.TaskUpdateResponseDTO;

public interface ITaskService {
	
	public TaskCreateResponseDTO createTask(TaskCreateRequestDTO requestdto);
	
	public TaskUpdateResponseDTO updateTask(TaskUpdateRequestDTO requestdto);
	
	void deleteTask(Long taskID);

}
