package com.yildiz.teamsync.dto;


import com.yildiz.teamsync.enums.TaskPriority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateResponseDTO {
	
	private Long taskID; 

    private String title;

    private String description;
    
    private TaskPriority priority; 
    
    private Long columnID; 
}

