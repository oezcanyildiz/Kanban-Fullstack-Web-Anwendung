package com.yildiz.teamsync.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yildiz.teamsync.dto.TaskCreateRequestDTO;
import com.yildiz.teamsync.dto.TaskCreateResponseDTO;
import com.yildiz.teamsync.dto.TaskUpdateRequestDTO;
import com.yildiz.teamsync.dto.TaskUpdateResponseDTO;
import com.yildiz.teamsync.services.ITaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final ITaskService taskService;

    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<TaskCreateResponseDTO> createTask(@RequestBody @Valid TaskCreateRequestDTO request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @PatchMapping("/update")
    public ResponseEntity<TaskUpdateResponseDTO> updateTask(@RequestBody @Valid TaskUpdateRequestDTO request) {
        return ResponseEntity.ok(taskService.updateTask(request));
    }

    @DeleteMapping("/{taskID}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskID) {
        taskService.deleteTask(taskID);
        return ResponseEntity.ok().build(); // oder ResponseEntity.noContent().build()
    }

}
