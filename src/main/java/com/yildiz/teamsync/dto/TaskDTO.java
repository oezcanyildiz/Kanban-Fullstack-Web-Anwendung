package com.yildiz.teamsync.dto;

import com.yildiz.teamsync.enums.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long taskID;
    private String title;
    private String description;
    private TaskPriority priority;
    private Integer position;
    private Long assigneeID;
    private String assigneeName;
}
