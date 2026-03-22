package com.yildiz.teamsync.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yildiz.teamsync.dto.BoardColumnCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardColumnCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardColumnUpdateRequestDTO;
import com.yildiz.teamsync.dto.BoardColumnUpdateResponseDTO;
import com.yildiz.teamsync.services.IBoardColumnService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/columns")
public class BoardColumnController {

    private final IBoardColumnService boardColumnService;

    public BoardColumnController(IBoardColumnService boardColumnService) {
        this.boardColumnService = boardColumnService;
    }

    @PostMapping("/create")
    public ResponseEntity<BoardColumnCreateResponseDTO> createColumn(@RequestBody @Valid BoardColumnCreateRequestDTO request) {
        return ResponseEntity.ok(boardColumnService.createColumn(request));
    }

    @PatchMapping("/update")
    public ResponseEntity<BoardColumnUpdateResponseDTO> updateColumn(@RequestBody @Valid BoardColumnUpdateRequestDTO request) {
        return ResponseEntity.ok(boardColumnService.updateColumn(request));
    }

    @DeleteMapping("/{columnID}")
    public ResponseEntity<Void> deleteColumn(@PathVariable Long columnID) {
        boardColumnService.deleteColumn(columnID);
        return ResponseEntity.ok().build();
    }
}
